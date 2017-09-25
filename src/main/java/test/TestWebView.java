package main.java.test;

import main.java.AppiumTestCase;
import main.java.SendAction;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class TestWebView extends AppiumTestCase {
    @Test
    public void testWebView() throws Exception{

        sendWithInfo(new String[]{"Web view", ""},5000);

        sendWithInfos(new String[][]{
                {"android.widget.EditText","appium"}
                , {"百度一下",""}
        },5000);
        sleep(10000);
    }


}
