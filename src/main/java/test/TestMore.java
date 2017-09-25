package main.java.test;

import main.java.AppiumTestCase;
import org.junit.Test;

public class TestMore  extends AppiumTestCase{
    @Test
    public void login() throws Exception{

        clickIfExitInfos(new String[] {"chenyu.jokes:id/tabMore", "chenyu.jokes:id/logout"});

        sendWithInfos(new String[][] {
                {"chenyu.jokes:id/login", ""},
                {"chenyu.jokes:id/email", "zhongchenyu_89@163.com"},
                {"chenyu.jokes:id/password", "654321"},
                {"android:id/button1", ""}
        });

        assertTextWithInfos(new String[][] {
                {"Get name successfully", "钟晨宇", "chenyu.jokes:id/name"},
                {"Get e-mail successfully", "zhongchenyu_89@163.com", "chenyu.jokes:id/email"},
                {"Find quit button", "", "chenyu.jokes:id/logout"}
        });

    }
}
