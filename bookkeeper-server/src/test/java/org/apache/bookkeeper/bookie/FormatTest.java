package org.apache.bookkeeper.bookie;

import org.apache.bookkeeper.conf.ServerConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class FormatTest {

    private final ServerConfiguration conf;
    private final Boolean isInteractive;
    private final Boolean force;
    private final Object expected;

    public FormatTest(ServerConfiguration conf, Boolean isInteractive, Boolean force, Object expected){

        this.conf = conf;
        this.isInteractive = isInteractive;
        this.force = force;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection getParameters(){

        ServerConfiguration server = new ServerConfiguration();
        String[] journalDir = {"dir", "dir2"};
        server.setJournalDirsName(journalDir);

        return Arrays.asList(new Object[][]{

                {null, true, false, NullPointerException.class},
                {new ServerConfiguration(), false, true, true},
                {new ServerConfiguration(), false, false, false},
                {server, false, true, true},
                {server, false, false, true},
                {server, true, false, true},
                {server, false, false, true},

                //{new ServerConfiguration(), true, false, true},
                //{new ServerConfiguration(), true, true, true}

        });
    }

    @Test
    public void test(){

        Object result;

        if(conf != null){

             conf.setLedgerDirNames(new String[]{"temp", "tester"});
             File myFile =  new File("/tmp/bk-txn/test.txt");

             try{

                 myFile.createNewFile();

             } catch (IOException e) {

                 e.printStackTrace();
             }

        }

        try {
            result = Bookie.format(conf, isInteractive, force);

        } catch (Exception e) {

            result = e.getClass();

        }

        Assert.assertEquals(expected, result);

    }

}