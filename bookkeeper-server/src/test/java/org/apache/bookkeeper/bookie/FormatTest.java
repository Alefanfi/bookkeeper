package org.apache.bookkeeper.bookie;

import org.apache.bookkeeper.conf.ServerConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class FormatTest {

    private final ServerConfiguration conf;
    private final Boolean isInteractive;
    private final Boolean force;
    private final Object expected;
    private final Boolean writeFile;

    public FormatTest(ServerConfiguration conf, Boolean isInteractive, Boolean force, Object expected, Boolean writeFile){

        this.conf = conf;
        this.isInteractive = isInteractive;
        this.force = force;
        this.expected = expected;
        this.writeFile = writeFile;
    }

    @Parameterized.Parameters
    public static Collection getParameters() throws IOException {

        ServerConfiguration server = new ServerConfiguration();
        String[] journalDir = {"dir", "dir2"};
        server.setJournalDirsName(journalDir);

        return Arrays.asList(new Object[][]{

                {null, true, false, NullPointerException.class, false},
                {new ServerConfiguration(), false, true, true, true},
                {new ServerConfiguration(), false, false, false, true},
                {server, false, true, true, true},
                {server, false, false, true, true},
                {server, true, false, true, true},
                {server, false, false, true, false},

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

                 if(writeFile) {

                     BufferedWriter writer = new BufferedWriter(new FileWriter(myFile));
                     writer.write("Hello world!");

                 }

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