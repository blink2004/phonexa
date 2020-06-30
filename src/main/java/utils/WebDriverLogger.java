package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebDriverLogger implements ITestListener {

    @Override
    public void onTestStart(ITestResult iTestResult) {
        log("Method [" + iTestResult.getName() + "] was starter");
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        log("Method [" + iTestResult.getName() + "] successfully finished.");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        log("Method [" + iTestResult.getName() + "] failed");
        try {
            takeSnapShot(DriverManager.getDriver(), iTestResult.getName() + ".jpg");
        } catch (Exception e) {
            log("Can't create screenshot");
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        log("Method [" + iTestResult.getName() + "] was skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {

    }

    private void log(String msg) {
        System.out.println("[" + getCurrentTime() + "] - " + msg);
    }

    public void takeSnapShot(WebDriver webdriver, String fileWithPath) throws Exception {
        final String screenshotPath = "./screenshots/";

        File path = new File(screenshotPath);
        if ( !path.isDirectory() ) {
            log("Create directory for screenshots...");
            try {
                path.mkdir();
            } catch (Exception e) {
                log("Some problem while creating folder.");
            }
            log("Folder for screenshots created successfully.");
        }

        TakesScreenshot scrShot =((TakesScreenshot)webdriver);
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
        File DestFile = new File(screenshotPath + fileWithPath);
        FileUtils.copyFile(SrcFile, DestFile);
    }

    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }
}
