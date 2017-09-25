package main.java;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;

import java.util.ArrayList;
import java.util.List;

public class RunSuit {
    public static void main(String[] args) {
        TestListenerAdapter tla = new TestListenerAdapter();
        TestNG testng = new TestNG();

        List<String> testFieldList = new ArrayList<>();
        testFieldList.add("testng_main.xml");
        //testFieldList.add("testng1.xml");
        //testFieldList.add("testng2.xml");
        testng.setTestSuites(testFieldList);

        testng.addListener(tla);
        testng.setSuiteThreadPoolSize(2);

        testng.run();
        System.out.println("ConfigurationFailures: "+tla.getConfigurationFailures());
        System.out.println("getFailedTests()" + tla.getFailedTests());
    }
}
