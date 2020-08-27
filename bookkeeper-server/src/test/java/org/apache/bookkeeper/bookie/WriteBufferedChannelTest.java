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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class WriteBufferedChannelTest {

    private ByteBuf sourceBufferedChannel;

    private BufferedChannel destinationBufferedChannel;

    private static Long valueToWrite = 15L;

    private Boolean result;
    private Boolean expected;

    public WriteBufferedChannelTest(ByteBuf sourceBufferedChannel, Boolean expected){

        this.sourceBufferedChannel = sourceBufferedChannel;
        this.expected = expected;
    }

    @Before
    public void setUp(){

        if(sourceBufferedChannel != null){

            ByteBufAllocator allocator = new ByteBufAllocatorBuilderImpl().build();

            try{

                RandomAccessFile file = new RandomAccessFile("file.test", "rw");
                FileChannel channel = file.getChannel();

                if(sourceBufferedChannel.capacity() == 33) {

                    destinationBufferedChannel = new BufferedChannel(allocator, channel, 8, 8, 1L);

                }else{

                    destinationBufferedChannel = new BufferedChannel(allocator, channel, 32);

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Parameterized.Parameters
    public static Collection getParameters() {

        return Arrays.asList(new Object[][]{

                {Unpooled.buffer(), true},
                {null, false},
                {Unpooled.buffer(33), true},
                {Unpooled.buffer(0), true}

        });
    }

    @After
    public void close() throws IOException {
        if(destinationBufferedChannel != null) {
            destinationBufferedChannel.close();
        }
    }

    @Test
    public void test(){

        try{
            destinationBufferedChannel.write(sourceBufferedChannel);
            destinationBufferedChannel.flush();

            if (sourceBufferedChannel.writerIndex()>0){

                //Check se la scrittura è stata effettuata

                RandomAccessFile checkFile = new RandomAccessFile("file.test", "r");

                checkFile.seek(0);

                byte[] check = new byte [8];

                checkFile.read(check);

                checkFile.close();

                result = checkWrite(check);

            }else{

                result = true;
            }

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            result = false;;
        }

        Assert.assertEquals(result, expected);
    }

    private Boolean checkWrite(byte[] check) {

        assert(check.length == 8);

        ByteBuffer buff = ByteBuffer.wrap(check);

        if(buff.getLong() == valueToWrite){
            return true;
        }else{
            return false;
        }

    }

}
