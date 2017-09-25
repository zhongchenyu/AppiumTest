package main.java;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

/**
 * Created by chenyu on 2017/9/9.
 */

public class AppiumTestCase {

    protected AppiumDriver<AndroidElement> driver;
    protected DesiredCapabilities capabilities = new DesiredCapabilities();
    //protected int port = 4723;
    protected int maxWaitTimeMS = 1000;
    protected int waitIntervalMS = 1000;

    @Parameters({"node", "appium.js", "port", "bootstrap_port", "chromedriver_port",
            "udid"})
    @BeforeSuite
    public void startServer(String nodePath, String appiumPath, String port,
                            String bootstrapPort, String chromeDriverPort, String udid) {
        boolean needStartServer = true;
        if (needStartServer) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        AppiumServerController.getInstance().startServer(nodePath, appiumPath, port, bootstrapPort, chromeDriverPort, udid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            try {
                Thread.sleep(40000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Parameters({"port", "platformName", "platformVersion", "deviceName", "appPackage", "appActivity"
            , "noReset", "app"})
    @BeforeTest
    public void setUp(String appiumPort, String platformName, String platformVersion, String deviceName, String appPackage,
                      String appActivity, String noReset, String app) {
        System.out.println("[-----------Paramaters-----------] port=" + appiumPort);
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("platformVersion", platformVersion);
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("appPackage", appPackage);
        capabilities.setCapability("appActivity", appActivity);
        capabilities.setCapability("noReset", noReset);
        capabilities.setCapability("app", app);
        capabilities.setCapability("unicodeKeyboard", true);
        capabilities.setCapability("resetKeyboard", true);

        //loadConfig();
        //loadFile();
        //loadStaticConfig();
        System.out.println(capabilities.toString());
        try {
            driver =
                    new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:"
                            + appiumPort + "/wd/hub"), capabilities);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @AfterTest
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Parameters({"suitName", "port"})
    @AfterSuite
    public void stopServer(String suitName, String port) {
        System.out.println("AfterSuite, stopServer() " + suitName);
        AppiumServerController.getInstance().stopServer(port);
    }

    private void loadConfig() {
        //File classpathRoot = new File(System.getProperty("user.dir"));
        //File configPath = new File(classpathRoot, "demotest/src/main/java/com/example/config.txt");
        File configPath = new File("config.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(configPath));
            String line;
            String[] splitLine;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) continue;
                splitLine = line.split("=");
                if (splitLine.length != 2) continue;
                switch (splitLine[0]) {
                    case "platformName":
                        capabilities.setCapability("platformName", splitLine[1]);
                        break;
                    case "noReset":
                        if (splitLine[1].equals("true")) {
                            capabilities.setCapability("noReset", true);
                        } else if (splitLine[0].equals("false")) {
                            capabilities.setCapability("noReset", false);
                        }
                        break;
                    case "app":
                        File app = new File(splitLine[1]);
                        capabilities.setCapability("app", app.getAbsolutePath());
                        break;
                    default:
                        capabilities.setCapability(splitLine[0], splitLine[1]);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    protected void clickIfExitInfo(String info) {
        try {
            findElement(info).click();
        } catch (NoSuchElementException e) {
            System.out.println("Can not find element with info " + info + " . Skip!");
        }
    }

    protected void clickIfExitInfos(String[] infos) throws Exception {
        for (String info : infos) {
            clickIfExitInfo(info);
        }
    }

    protected void sendWithInfo(SendAction action) throws Exception {
        WebElement element = findElement(action.info);
        if (action.text == null || action.text.length() == 0) {
            element.click();
        } else {
            element.sendKeys(action.text);
        }
    }

    protected void sendWithInfos(SendAction[] actions) throws Exception {
        for (SendAction action : actions) {
            sendWithInfo(action);
        }
    }

    protected void sendWithInfos(String[][] actions) throws Exception {
        for (String[] action : actions) {
            sendWithInfo(new SendAction(action[0], action[1]));
        }
    }

    protected void sendWithInfos(String[][] actions, int maxWaitTimeMS) {

        for (String[] action : actions) {
            sendWithInfo(action, maxWaitTimeMS);
        }
    }

    protected void sendWithInfo(String[] action, int maxWaitTimeMS) {
        int findTimes = maxWaitTimeMS / waitIntervalMS + 1;
        for (int i = 0; i < findTimes; i++) {
            try {
                WebElement element = findElement(action[0]);
                if (action[1] == null || action[1].length() == 0) {
                    element.click();
                } else {
                    element.sendKeys(action[1]);
                }
            } catch (NoSuchElementException e) {
                if (i == findTimes - 1) {
                    System.out.println("Can not find [" + action[0] + "] in last attempt!!!");
                    throw e;
                }
                System.out.println("Can not find [" + action[0] + "] in " + (i + 1) + "th attempt. Try again after " + waitIntervalMS + "ms.");
                sleep(waitIntervalMS);
                continue;
            }
            break;
        }
    }

    protected void assertTextWithInfos(String[][] assertions) throws Exception {
        for (String[] assertion : assertions) {
            if (assertion[1] == null || assertion[1].length() == 0) {
                Assert.assertNotNull(assertion[0], findElement(assertion[2]));
            } else {
                Assert.assertEquals(assertion[0], assertion[1],
                        findElement(assertion[2]).getText());
            }
        }
    }

    protected WebElement findElement(String info) {
        WebElement element = null;
        if (info.startsWith("//")) {
            element = driver.findElementByXPath(info);
        } else if (info.contains(":id/")) {
            element = driver.findElementById(info);
        } else {
            try {
                //element = driver.findElementByAndroidUIAutomator(("text(\"中文\")"));
                //element = driver.findElementByAccessibilityId(info);
                element = driver.findElementByXPath("//*[@text='" + info + "']");
                System.out.println("[------------Element---------- ] " + info + " " + element.getLocation() + " ");//+ element.getAttribute("class")
            } catch (NoSuchElementException e) {
                try {
                    //element = driver.findElementByXPath("//*[contains(@content-desc ,'"+info+"')]");
                    element = driver.findElementByXPath("//*[@content-desc = '" + info + "']");
                    System.out.println("[------------Element---------- ] " + info + " " + element.getLocation() + " ");//+ element.getAttribute("class")
                } catch (NoSuchElementException e2) {
                    element = driver.findElementByClassName(info);
                    System.out.println("[------------Element---------- ] " + info + " " + element.getLocation() + " ");
                    //    throw new NoSuchElementException("Can not find info = " + info);
                }


                //throw new Exception("Can not find info = " + info);
                //element = driver.findElementByClassName(info);
            }
        }
        if (null == element) {
            throw new NoSuchElementException("Can not find info = " + info);
        }
        return element;
    }

  /*
  protected void waitElement(String info)  throws Exception{
    new WebDriverWait(driver, 5).until(new Function<WebDriver, Object>() {
      @Override public WebElement apply(WebDriver webDriver) {
        return findElement(info);
      }
    });
  }

  protected void waitForElement(String info) {
    new WebDriverWait(driver, 5).until((webDriver) -> findElement(info));
  }
  */

    protected void loadStaticConfig() {
        //File classpathRoot = new File(System.getProperty("user.dir"));
        //File appDir = new File(classpathRoot, "demotest/src/main/java/apps");
        //File app = new File(appDir, "jokes.apk");
        File app = new File("chenyu.memorydemo-debug-v1.2.apk");
        capabilities.setCapability("deviceName", "emulator-5554");
        capabilities.setCapability("platformVersion", "6.0");
        capabilities.setCapability("app", app.getAbsolutePath());
        capabilities.setCapability("appPackage", "chenyu.momerydemo");
        capabilities.setCapability("appActivity", ".MainActivity");
        capabilities.setCapability("noReset", true);


    }

    private void loadFile() {
        try {
            File file = new File("test.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected boolean switchToWebView() {
        Set<String> contextNames = driver.getContextHandles();
        for (String contextName : contextNames) {

            if (contextName.contains("WEBVIEW")) {
                driver.context(contextName);
                System.out.print("Switch to WEBVIEW: ");
                System.out.println(contextName);
                return true;
            } else {
                System.out.print("not WEBVIEW: ");
                System.out.println(contextName);
            }
        }
        return false;
    }

}
