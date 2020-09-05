package org.apache.bookkeeper.bookie;

import org.apache.bookkeeper.conf.ServerConfiguration;
import org.apache.commons.io.FileUtils;
import org.junit.After;
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

    private final Boolean setDir;

    private File myFile;

    private static String[] journalDir;

    public FormatTest(ServerConfiguration conf, Boolean isInteractive, Boolean force, Object expected, Boolean setDir){

        this.conf = conf;
        this.isInteractive = isInteractive;
        this.force = force;
        this.expected = expected;
        this.setDir = setDir;
    }

    @Parameterized.Parameters
    public static Collection getParameters(){

        ServerConfiguration server = new ServerConfiguration();
        journalDir = new String[]{"dir", "dir2"};
        server.setJournalDirsName(journalDir);
        File dir = new File("dir");
        dir.mkdir();
        new File(dir, "test.txt");
        new File(dir, "tested.txt");
       /* File dir2 = new File("dir2");
        dir2.mkdir();
        new File(dir2, "test2.txt");**/

        return Arrays.asList(new Object[][]{

                {null, true, false, NullPointerException.class, true},
                {new ServerConfiguration(), false, true, true, true},
                {new ServerConfiguration(), false, false, false, true},
                {server, false, true, true, true},
                {server, false, false, true, true},
                {server, true, false, true, true},
                {server, true, false, true, false},
                {server, true, true, true, true},
                {server, false, false, true, false},
                {new ServerConfiguration(), false, false, false, false},
                {new ServerConfiguration(), false, true, true, false},
                {new ServerConfiguration(), false, false, true, false},
                //{new ServerConfiguration(), true, true, true, false}, //Are you sure to format?

        });
    }

    @After
    public void afterFunction() throws IOException {

        //delete folder
        if(new File("dir").exists()) {
            FileUtils.deleteDirectory(new File("dir"));
        }else if(new File("dir2").exists()) {
            FileUtils.deleteDirectory(new File("dir2"));
        }

    }

    @Test
    public void test(){

        Object result;

        if(conf != null && setDir){

             conf.setLedgerDirNames(new String[]{"temp", "tester"});
             myFile =  new File("/tmp/bk-txn/test.txt");

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