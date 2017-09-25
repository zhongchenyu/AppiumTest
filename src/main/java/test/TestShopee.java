package main.java.test;

import main.java.AppiumTestCase;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import java.util.Set;

public class TestShopee extends AppiumTestCase {
    @Test
    public void testShopee() {
        System.out.println("Begin to test shopee: ");
        sleep(10000);


        clickIfExitInfo("查看全部");
        sleep(30000);

        Set<String> contextNames = driver.getContextHandles();
        for (String contextName : contextNames) {

            if (contextName.contains("WEBVIEW")) {
                System.out.print("WEBVIEW: ");
                System.out.println(contextName);
                driver.context(contextName);
            } else {
                System.out.print("not WEBVIEW: ");
                System.out.println(contextName);
            }
        }
        WebElement element = driver.findElementByName("百搭中筒襪 (9色11雙組)");
        //百搭中筒襪 (9色11雙組)
        System.out.println("webElement: " + element);
        element.click();
        sleep(5000);
    }
}
