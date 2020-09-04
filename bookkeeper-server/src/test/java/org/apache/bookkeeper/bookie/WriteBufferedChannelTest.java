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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class WriteBufferedChannelTest {

    private final ByteBuf sourceBufferedChannel;

    private BufferedChannel destinationBufferedChannel;

    private static final Long valueToWrite = 15L;

    private final Object expected;

    public WriteBufferedChannelTest(ByteBuf sourceBufferedChannel, Object expected){

        this.sourceBufferedChannel = sourceBufferedChannel;
        this.expected = expected;
    }

    @Before
    public void setUp() throws IOException {

        if(sourceBufferedChannel != null){

            ByteBufAllocator allocator = new ByteBufAllocatorBuilderImpl().build();

            RandomAccessFile file = new RandomAccessFile("file.test", "rw");
            FileChannel channel = file.getChannel();

            destinationBufferedChannel = new BufferedChannel(allocator, channel, 32);

        }
    }

    @Parameterized.Parameters
    public static Collection getParameters() {

        return Arrays.asList(new Object[][]{

                {Unpooled.buffer(), true},
                {null, NullPointerException.class},
                {Unpooled.buffer(0), true},
                {Unpooled.buffer().capacity(1024), true},
                {Unpooled.buffer().writeLong(valueToWrite.byteValue()), true}

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

        Object result;

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

            result = e.getClass();;
        }

        Assert.assertEquals(result, expected);
    }

    private Boolean checkWrite(byte[] check) {

        assert(check.length == 8);

        ByteBuffer buff = ByteBuffer.wrap(check);

        return buff.getLong() == valueToWrite;
    }

}
