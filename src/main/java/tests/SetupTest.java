package tests;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import utils.Setup;

import java.io.File;

public class SetupTest {

    static Setup setup;

    @BeforeClass
    public static void setup(){
        setup = new Setup(System.getProperty("user.dir"));
    }


    @Test
    public void fileTest() {
        assertNotNull(setup.fileAccessor());
    }

    @Test
    public void hashMapTest() {
        assertNotNull(setup.mapAccessor());
    }

    @AfterClass
    public static void end()
    {
        setup = null;
    }
}
