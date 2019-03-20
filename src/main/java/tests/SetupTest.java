package tests;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.Setup;

import static org.junit.Assert.assertNotNull;

public class SetupTest {

    static Setup setup;
//todo
    @BeforeClass
    public static void setup(){
        setup = new Setup(System.getProperty("user.dir"));
    }

/*
    @Test
    public void fileTest() {
        assertNotNull(setup.filesAccessor());
    }

    @Test
    public void hashMapTest() {
        assertNotNull(setup.mapAccessor());
    }*/

    @AfterClass
    public static void end()
    {
        setup = null;
    }
}
