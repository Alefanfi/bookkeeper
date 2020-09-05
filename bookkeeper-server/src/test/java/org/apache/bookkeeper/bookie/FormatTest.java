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
    public static Collection getParameters() throws IOException {

        ServerConfiguration server = new ServerConfiguration();
        String[] journalDir = {"dir", "dir2"};
        server.setJournalDirsName(journalDir);
/*
        ServerConfiguration server2 = new ServerConfiguration();
        Scanner sc = new Scanner(System.in);
        String path = sc.next();
        path = path+sc.next();
        File dir = new File(path);
        dir.mkdir();
        String fileName="generate required fileName";
        File tagFile=new File(dir,fileName+".txt");
        if(!tagFile.exists()){
            tagFile.createNewFile();
        }

        String[] dirName = {path};
        server2.setJournalDirsName(dirName);*/


        return Arrays.asList(new Object[][]{

                {null, true, false, NullPointerException.class},
                {new ServerConfiguration(), false, true, true},
                {new ServerConfiguration(), false, false, true},
                {server, false, true, true},
                {server, false, false, true},
                {server, true, false, true},
                {server, false, false, true},

                //{server2, false, false, true},

                //{new ServerConfiguration(), true, false, true},
                //{new ServerConfiguration(), true, true, true}

        });
    }

    @Test
    public void test(){

        Object result;

        if(conf != null){

             conf.setLedgerDirNames(new String[]{"temp", "tester"});
             File myFile =  new File("/temp/bk-txn/test.txt");

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