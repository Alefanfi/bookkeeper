package org.apache.bookkeeper.bookie;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.bookkeeper.bookie.entities.AddEntry;
import org.apache.bookkeeper.client.BKException;
import org.apache.bookkeeper.net.BookieSocketAddress;
import org.apache.bookkeeper.proto.BookkeeperInternalCallbacks;
import org.apache.bookkeeper.test.BookKeeperClusterTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class BookieTest extends BookKeeperClusterTestCase {

    private AddEntry addEntry;

    private static final BookkeeperInternalCallbacks.WriteCallback  writeCallback = new BookkeeperInternalCallbacks.WriteCallback() {
        @Override
        public void writeComplete(int rc, long ledgerId, long entryId, BookieSocketAddress addr, Object ctx) {

        }
    };

    public BookieTest(AddEntry entryTest) {
        super(1);
        this.addEntry = entryTest;
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        baseConf.setOpenFileLimit(1);
    }

    @Parameterized.Parameters
    public static Collection<AddEntry> getParameters(){
        //byte[] masterKey = "password".getBytes();
        ByteBuf validEntry = Unpooled.buffer();

        return Arrays.asList(
                new AddEntry(validEntry,false, writeCallback,null, null, true, true)
                //new Entry(null,true,null,null, new byte[]{}, false, false),
                //new Entry(validEntry,false, writeCallback,null,null, false, false)
        );
    }

    @Test
    public void test() throws BKException, InterruptedException {


    }
}
