package main.java;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class AppiumServerController {

    //private Process mProcess;
    private HashMap<String, Process> processHashMap = new HashMap<>();
    private String nodePath = "node";
    private String appiumJsPath;
    private String  port;
    private String bootstrapPort;
    private String chromedriver_port;
    private String UID;

    private static AppiumServerController appiumServerController = new AppiumServerController();

    private AppiumServerController() {
    }

    public void startServer() throws Exception {
        Process process;
        BufferedReader envReader = new BufferedReader(new FileReader(new File("env.txt")));
        String envLine;
        while ((envLine = envReader.readLine()) != null) {
            if (envLine.startsWith("#")) continue;

            String[] split = envLine.split("=");
            if (split.length != 2) continue;
            switch (split[0]) {
                case "node":
                    nodePath = split[1];
                    System.out.println("New node path: " + nodePath);
                    break;
                case "appium.js":
                    appiumJsPath = split[1];
                    System.out.println("New appium.js path: " + appiumJsPath);
                    break;
                case "port":
                    port = split[1];
                    break;
                case "bootstrap_port":
                    bootstrapPort = split[1];
                    break;
                case "chromedriver_port":
                    chromedriver_port =split[1];
                    break;
                case "UID":
                    UID = split[1];
                    break;
                default:
                    break;
            }
        }
        //startServer(nodePath, appiumJsPath,port,bootstrapPort,chromedriver_port,UID);
        String cmd = nodePath + " \"" + appiumJsPath + "\" " + "--session-override " + " -p "
                + port + " -bp " + bootstrapPort + " --chromedriver-port " + chromedriver_port + " -U " + UID;
        System.out.println(cmd);
        process = Runtime.getRuntime().exec(cmd);
        processHashMap.put(String.valueOf(port), process);
        System.out.println(process);
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        process.waitFor();
        System.out.println("Stop appium server");
        inputStream.close();
        reader.close();
        process.destroy();
    }

    public void startServer(ReentrantLock lock, String nodePath, String appiumPath, String port,
                            String bootstrapPort, String chromeDriverPort, String udid) throws Exception {
        Process process;
        String cmd = nodePath + " \"" + appiumPath + "\" " + "--session-override " + " -p "
                + port + " -bp " + bootstrapPort + " --chromedriver-port " + chromeDriverPort + " -U " + udid;
        System.out.println(cmd);
        process = Runtime.getRuntime().exec(cmd);
        processHashMap.put(port, process);
        System.out.println(process);
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            if(line.startsWith("[Appium] Appium REST http interface listener started on")) {
                lock.unlock();
            }
        }
        process.waitFor();
        System.out.println("Stop appium server");
        inputStream.close();
        reader.close();
        process.destroy();
    }

    public void stopServer(Process process) {

        if (process != null) {
            System.out.println(process);
            process.destroy();
        }
    }

    public void stopServer(String port) {
        Process process = processHashMap.get(port);
        stopServer(process);
        processHashMap.remove(port);
    }

    public static AppiumServerController getInstance() {
        return appiumServerController;
    }
}
