package utils;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class DriverManager {
    static private String baseUrl;

    public static String getBrowser() {
        return browser;
    }

    static protected String browser;
    static private WebDriver driver = null;
    private DesiredCapabilities capabilities;
    private static int explicitWait;
    private int implicitWait;
    private int browserWidth;
    private static String browserName;

    static public WebDriver getDriver() {
        return driver;
    }

    public static void goToUrl(String url) {
        driver.get(url);
    }

    public static String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public void setImplicitWait(int timeInSeconds) {
        this.implicitWait = timeInSeconds;
    }

    public int getImplicitWait() {
        return implicitWait;
    }

    public static int getExplicitWait() {
        return explicitWait;
    }

    public void setExplicitWait(int timeInSeconds) {
        this.explicitWait = timeInSeconds;
    }

    public int getBrowserWidth() {
        return browserWidth;
    }

    private void readPropertyFile(String fileName) throws IOException {
        List<String> browsersPreSetup = Arrays.asList(new String[]{"ch", "ff", "op"});
        Properties prop = readPropertiesFile(fileName);
        implicitWait = getDefVal(Integer.valueOf(prop.getProperty("implicit")), 0);
        explicitWait = getDefVal(Integer.valueOf(prop.getProperty("explicit")), 0);
        browserWidth = getDefVal(Integer.valueOf(prop.getProperty("browserWidth")), 500);
        browserName = prop.getProperty("browser");
        if (!browsersPreSetup.contains(browserName.toLowerCase()))
            browserName = "ch";
    }

    private static Properties readPropertiesFile(String fileName) throws IOException {
        FileInputStream file = null;
        Properties prop = null;
        try {
            file = new FileInputStream(fileName);
            prop = new Properties();
            prop.load(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.close();
        }
        return prop;
    }

    /**
     * Get default value. Return value param if value is exist, defaultValue otherwise.
     *
     * @param value
     * @param defaultValue
     * @return default value
     */
    private String getDefVal(String value, String defaultValue) {
        return (value != null) ? value : defaultValue;
    }

    private int getDefVal(Integer value, int defaultValue) {
        return (value != null) ? value : defaultValue;
    }

    /**
     * create listener instance for logging events
     *
     * @param wDriver
     */
    public WebDriver createEventListenerDriver(WebDriver wDriver) {
        EventFiringWebDriver driver = new EventFiringWebDriver(wDriver);
        driver.manage().window().setSize(new Dimension(browserWidth, 700));
        driver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
        return driver;
    }

    public void startBrowser() throws IOException {
        readPropertyFile("src\\main\\resources\\page.properties");
        switch (getBrowserName().toLowerCase()) {
            case "ch":
                setChromeCapabilities();
                driver = createEventListenerDriver(new ChromeDriver());
                break;
            case "ff":
                setFirefoxCapabilities();
                driver = createEventListenerDriver(new FirefoxDriver(capabilities));
                break;
            case "op":
                setOperaCapabilities();
                driver = createEventListenerDriver(new OperaDriver(capabilities));
                break;
        }
    }

    public void setChromeCapabilities() {
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
    }

    public void setFirefoxCapabilities() {
        System.setProperty("webdriver.gecko.driver", "src\\main\\resources\\geckodriver.exe");
        capabilities = new DesiredCapabilities();
        capabilities.setCapability("security.enable_java", false);
        capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
    }

    public void setOperaCapabilities() {
        System.setProperty("webdriver.opera.driver", "src\\main\\resources\\operadriver.exe");
        OperaOptions options = new OperaOptions();
        options.setBinary("D:\\Program Files\\Opera\\69.0.3686.36\\opera.exe");
        capabilities = new DesiredCapabilities();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
    }

    public void close() {
        if (driver != null)
            driver.quit();
    }

}
