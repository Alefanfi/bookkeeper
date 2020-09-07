package org.apache.bookkeeper.bookie;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import org.apache.bookkeeper.common.allocator.impl.ByteBufAllocatorBuilderImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

@RunWith(value = Parameterized.class)
public class ReadBuffedChannelTest {

    private final ByteBuf destinationBuffer;

    private final long position;
    private final int length; //numero di bytes che leggo

    private final Object expected;

    private BufferedChannel readerBufferedChannel;

    private byte[] bytes;

    public ReadBuffedChannelTest(ByteBuf destinationBuffer, long position, int length, Object expected){

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

                } else if(position > 1){

                    ByteBuf d = Unpooled.buffer();
                    bytes = new byte[Math.toIntExact(position)];
                    new Random().nextBytes(bytes);
                    d.writeBytes(bytes);
                    readerBufferedChannel.write(d);

                }

            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    }
    @After
    public void close(){

        if(length > 0){
            File f = new File("file.test");
            f.delete();

        }
    }

    @Parameterized.Parameters
    public  static Collection getParameters() {

        ByteBuf buf = Unpooled.buffer();
        byte[] temp = new byte[buf.capacity()];
        new Random().nextBytes(temp);
        buf.writeBytes(temp);

        return Arrays.asList(new Object[][]{

                {Unpooled.buffer(), 1, 1, true},
                {Unpooled.buffer(), -1, 2, IllegalArgumentException.class},
                {Unpooled.buffer(), 2, 2, IOException.class},
                {Unpooled.buffer().capacity(0), -1, -1, NullPointerException.class},
                {null, 0, 0, NullPointerException.class},
                {Unpooled.buffer(1024), 1, 2, true},
                {buf, 1, 1, IOException.class}

        });
    }

    @Test
    public void test(){

        Object result;

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
            result = e.getClass();
        }

        Assert.assertEquals(result, expected);

    }

}
