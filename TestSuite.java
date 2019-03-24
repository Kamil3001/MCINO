package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        FileMetricsTest.class,
        MethodMetricsTests.class,
        SetupTest.class })
		
public class TestSuite {}