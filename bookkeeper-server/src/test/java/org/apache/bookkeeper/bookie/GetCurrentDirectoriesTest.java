package org.apache.bookkeeper.bookie;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(value = Parameterized.class)
public class GetCurrentDirectoriesTest {

    private final File[] listFile;
    private final Object expected;
    private List<File> elenco;

    public GetCurrentDirectoriesTest(Object expected, File[] listFile){
        this.expected =  expected;
        this.listFile = listFile;
    }

    @Parameterized.Parameters
    public static Collection getParameters(){

        return Arrays.asList(new Object[][]{

                {true, new File[]{}},
                {NullPointerException.class, null},
                {true, new File[]{new File("file1"), new File("file2")}}

        });
    }

    @Before
    public void setUp(){
        if(listFile != null){
            elenco = new ArrayList<>();
            for(File f : listFile){
                File f2  = new File(f.getPath() + "/current");
                elenco.add(f2);
            }
        }
    }

    @Test
    public void test(){

        Object result;
        try{

            File[] listFileTest = Bookie.getCurrentDirectories(listFile);

            if(listFile == null){

                result = true;

            }else{

                if(listFileTest != null){

                    result = elenco.containsAll(Arrays.asList(listFileTest));

                }else{

                    result = false;
                }
            }

        } catch (Exception e){

            result = e.getClass();
        }

        Assert.assertEquals(expected, result);

    }



}
