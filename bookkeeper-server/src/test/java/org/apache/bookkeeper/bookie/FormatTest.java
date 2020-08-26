package org.apache.bookkeeper.bookie;

import org.apache.bookkeeper.conf.ServerConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class FormatTest {

    private ServerConfiguration conf;
    private Boolean isInteractive;
    private Boolean force;
    private Boolean expected;

    private Boolean result;

    public FormatTest(ServerConfiguration conf, Boolean isInteractive, Boolean force, Boolean expected){

        this.conf = conf;
        this.isInteractive = isInteractive;
        this.force = force;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection getParameters(){

        return Arrays.asList(new Object[][]{

                {null, true, false, false},
                {new ServerConfiguration(), false, true, true},
                {null, false, false, false}/*
                {new ServerConfiguration(), true, false, true},
                {new ServerConfiguration(), true, true, true}*/

        });
    }

    @Test
    public void test(){

        /**
         if(conf!=null){

         conf.setLedgerDirNames(new String[]{"temp", "tester"});
         File myFile =  new File("/tmp/bk-txn/test.txt");
         try{
         myFile.createNewFile();

         } catch (IOException e) {
         e.printStackTrace();
         }
         }*/

        try {
            result = Bookie.format(conf, isInteractive, force);

        } catch (Exception e) {

            result = false;

        }

        Assert.assertEquals(expected, result);

        //myFile.delete();
    }

}