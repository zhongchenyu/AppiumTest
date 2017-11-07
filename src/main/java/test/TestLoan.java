package main.java.test;

import main.java.AppiumTestCase;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class TestLoan extends AppiumTestCase {
    @Test public void test() {
        //sendWithInfos(new String[][]{{"帮助",""},{"费率",""}}, 5000);
        sendWithInfos(new String[][]{{"转到上一层级",""}}, 5000);
        WebElement element = findElement("输入手机号码");
        System.out.println("--------------------------ELEMENT---------------------------------");
        System.out.println("[-----ELEMENT-----]element " + element);
        //System.out.println("[-----ELEMENT-----]getRect" + element.getRect().toString());

        System.out.println("[-----ELEMENT-----]getLocation "+element.getLocation());
        System.out.println("[-----ELEMENT-----]getText " +element.getText());
        System.out.println("[-----ELEMENT-----]getAttribute resourceId " + element.getAttribute("resourceId"));
        System.out.println("[-----ELEMENT-----]getName " + element.getTagName());
        //System.out.println(element.getCssValue());
        System.out.println("[-----ELEMENT-----]getSize " + element.getSize());
        System.out.println("[-----ELEMENT-----]getClass " + element.getClass());

    }
}
