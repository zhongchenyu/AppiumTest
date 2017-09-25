package main.java.test;

import main.java.AppiumTestCase;
import org.testng.annotations.Test;

public class TestAnimation extends AppiumTestCase {
    @Test
    public void testAnimation() {
        sleep(2000);
        sendWithInfo(new String[]{"Animation", ""}, 5000);
        sleep(5000);
    }
}
