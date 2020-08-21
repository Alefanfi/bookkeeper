package org.apache.bookkeeper.bookie;

import io.netty.buffer.ByteBuf;
import org.apache.bookkeeper.bookie.entities.AddEntry;
import org.apache.bookkeeper.client.BKException;
import org.apache.bookkeeper.client.BookKeeper;
import org.apache.bookkeeper.client.LedgerHandle;
import org.apache.bookkeeper.proto.BookkeeperInternalCallbacks;
import org.apache.bookkeeper.test.BookKeeperClusterTestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class AddEntryTest extends BookKeeperClusterTestCase {

    private AddEntry entry;

    /**private static final BookkeeperInternalCallbacks.WriteCallback  writeCallback = new BookkeeperInternalCallbacks.WriteCallback() {
    @Override
    public void writeComplete(int rc, long ledgerId, long entryId, BookieSocketAddress addr, Object ctx) {

    }
    };*/

    private static final BookkeeperInternalCallbacks.WriteCallback writeCallback = (rc, ledgerId, entryId, addr, ctx) -> { };

    public AddEntryTest(AddEntry entryTest) {
        super(1);
        this.entry = entryTest;
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        baseConf.setOpenFileLimit(1);
    }

    @Parameterized.Parameters
    public static Collection<AddEntry> getParameters(){
        byte[] masterKey = "password".getBytes();

        return Arrays.asList(
                new AddEntry(null,false, writeCallback,null, masterKey, false)
                //new Entry(null,true,null,null, new byte[]{}, false),
                //new Entry(Unpooled.buffer(), false, writeCallback,null,null, false)
        );
    }

    @Test
    public void test() throws BKException, InterruptedException {

        Boolean result;

        Boolean expected = entry.getExpected();

        LedgerHandle ledger = bkc.createLedger(1, 1, BookKeeper.DigestType.CRC32, entry.getKey());
        Long ledgerID = ledger.getId();

        entry.setLedgerId(ledgerID);

        Bookie bookie = bs.get(0).getBookie();

        try {
            bookie.addEntry(entry.getEntry(), false, writeCallback, null, entry.getKey());
        } catch (IOException | BookieException | InterruptedException | NullPointerException e) {
            e.printStackTrace();
            result = false;
        }

        try {

            ByteBuf entryRead = bookie.readEntry(entry.getLedgerId(), entry.getEntryId());

            byte[] destination = new byte[entryRead.readableBytes()];
            entryRead.getBytes(0, destination);

            String check = "tests";
            String content = new String(destination);
            content = content.substring(content.length() - check.length());

            Assert.assertEquals(check, content);
            result = true;

        } catch (IOException e) {

            e.printStackTrace();

            result = false;
        }

        Assert.assertEquals(expected,result);
    }
}
