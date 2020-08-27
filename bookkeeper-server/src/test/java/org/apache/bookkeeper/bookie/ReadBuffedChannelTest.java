package org.apache.bookkeeper.bookie;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import org.apache.bookkeeper.common.allocator.impl.ByteBufAllocatorBuilderImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

@RunWith(value = Parameterized.class)
public class ReadBuffedChannelTest {

    private ByteBuf destinationBuffer;

    private long position;
    private int length; //numero di bytes che leggo

    private Boolean result;
    private Boolean expected;

    private BufferedChannel readerBufferedChannel;

    private byte[] bytes;

    public ReadBuffedChannelTest(ByteBuf destinationBuffer, long position, int length, Boolean expected){

        this.destinationBuffer = destinationBuffer;
        this.length = length;
        this.position = position;
        this.expected = expected;
    }

    @Before
    public void setUp(){

        if(destinationBuffer != null){

            ByteBufAllocator allocator = new ByteBufAllocatorBuilderImpl().build();

            try{

                readerBufferedChannel = new BufferedChannel(allocator, new RandomAccessFile("file.test", "rw").getChannel(), 32);

                if(length>0){

                    ByteBuf d = Unpooled.buffer();
                    bytes = new byte[length+1];
                    new Random().nextBytes(bytes);
                    d.writeBytes(bytes);
                    readerBufferedChannel.write(d);

                } /*else if(position > 1){

                    ByteBuf d = Unpooled.buffer();
                    bytes = new byte[Math.toIntExact(position)];
                    new Random().nextBytes(bytes);
                    d.writeBytes(bytes);
                    readerBufferedChannel.write(d);

                }*/

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Parameterized.Parameters
    public  static Collection getParameters() {

        return Arrays.asList(new Object[][]{

                {Unpooled.buffer(), 1, 1, true},
                {Unpooled.buffer(), -1, 2, false},
                {Unpooled.buffer(), 2, 2, false},
                {Unpooled.buffer().capacity(0), -1, -1, false},
                {null, 0, 0, false}


        });
    }

    @Test
    public void test(){

        try{

            int positionInBuffer = (int) (position - readerBufferedChannel.readBufferStartPosition);
            int bytesToCopy = Math.min(readerBufferedChannel.readBuffer.writerIndex() - positionInBuffer,
                    destinationBuffer.writableBytes());

            int ret = readerBufferedChannel.read(destinationBuffer, position, length);

            byte[] readByte = new byte[length+1];
            byte[] b = Arrays.copyOfRange(bytes, Math.toIntExact(position), bytes.length);

            if(length < 0 && !(new String(readByte).equals(new String(b)))){

                result = false;

            }else {

                result = true;
            }

            if((ret <= 0 && destinationBuffer.capacity() > 0) || (ret != bytesToCopy && bytesToCopy >= 0)){

                Assert.fail();
                return;

            }

            if(length <= 0){

                result = true;
            }


        } catch (NullPointerException | IllegalArgumentException | IOException e) {
            result = false;
            e.printStackTrace();
        }

        Assert.assertEquals(result, expected);

    }

}
