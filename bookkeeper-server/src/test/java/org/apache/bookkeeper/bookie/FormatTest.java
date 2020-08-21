package org.apache.bookkeeper.bookie;

import org.apache.bookkeeper.bookie.entities.Format;
import org.apache.bookkeeper.conf.ServerConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class FormatTest {

    private Format formatTesting;

    public FormatTest(Format formatTesting){
        this.formatTesting = formatTesting;
    }

    @Parameterized.Parameters
    public static Collection<Format> getParameters(){

        return Arrays.asList(

                //new Format(new ServerConfiguration(), true, true, true),
                new Format(null, true, false, false),
                new Format(new ServerConfiguration(), false, true, true),
                //new Format(new ServerConfiguration(), true, false, true) //Funziona
                new Format(null, false, false, false)
        );
    }

    @Test
    public void formatTest(){

        Boolean success;

        Boolean expected = formatTesting.getExpected();
        ServerConfiguration conf = formatTesting.getConf();

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

        Boolean isInteractive = formatTesting.getInteractive();
        Boolean force = formatTesting.getForce();

        try {
            success = Bookie.format(conf, isInteractive, force);

        } catch (Exception e) {

            success = false;

        }
        Assert.assertEquals(expected, success);

        //myFile.delete();
    }


}