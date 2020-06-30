package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DriverManager;

import java.util.List;

public class AbstractPage {
    private WebDriverWait wait;

    public WebElement getElement(String by, String locator) {
        WebElement element = null;
        try {
            switch (by.toLowerCase()) {
                case "id":
                    element = DriverManager.getDriver().findElement(By.id(locator));
                    break;
                case "class":
                    element = DriverManager.getDriver().findElement(By.className(locator));
                    break;
                case "css":
                    element = DriverManager.getDriver().findElement(By.cssSelector(locator));
                    break;
                case "xpath":
                    element = DriverManager.getDriver().findElement(By.xpath(locator));
            }
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Element with '" + by + "'='" + locator + "' is not found");
        } catch (StaleElementReferenceException e) {
            throw new StaleElementReferenceException("StaleElementReferenceException for element '" + by + "='" + locator + "'");
        }
        return element;
    }

    public List<WebElement> getElements(String locator){
        return DriverManager.getDriver().findElements(By.cssSelector(locator));
    }

    public void setValueToField(WebElement element, String value) {
        element.clear();
        element.sendKeys(value);
    }

    public void clickElement(WebElement el) {
        Actions actions = new Actions(DriverManager.getDriver());
        scrollPage(el.getLocation());
        actions.moveToElement(el).click(el).build().perform();
    }

    public void moveToElement(WebElement el) {
        Actions actions = new Actions(DriverManager.getDriver());
        scrollPage(el.getLocation());
        actions.moveToElement(el).build().perform();
    }

    public void setCheckbox(WebElement el, boolean status) {
        if (( (!el.getAttribute("checked").equals("checked")) && (status==true)) ||
                ( (el.getAttribute("checked").equals("checked")) && (status==false)))
            el.click();
    }

    public void waitElementIsDisplayed(WebElement element) {
        waitElementIsDisplayed(element, DriverManager.getExplicitWait());
    }

    public void waitElementIsDisplayed(WebElement element, int timeInSeconds) {
        wait = new WebDriverWait(DriverManager.getDriver(), timeInSeconds);
        Exception exception = null;
        do {
            try {
                wait.until(ExpectedConditions.visibilityOf(element));
                break;
            } catch (StaleElementReferenceException e) {
                exception = new StaleElementReferenceException(e.getMessage());
            }
        } while ((exception!=null) && (exception.equals(StaleElementReferenceException.class)));
    }

    public void scrollPageY(int offsetY) {
        JavascriptExecutor jse = (JavascriptExecutor) DriverManager.getDriver();
        jse.executeScript("window.scrollBy(0, " + offsetY + ")");
    }

    public void scrollPage(Point p) {
        sleep(2);
        JavascriptExecutor jse = (JavascriptExecutor) DriverManager.getDriver();
        jse.executeScript("window.scroll(" + (p.x - 50) + ", " + (p.y - 50) + ")");

    }

    public void executeJS(String jsCode) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript(jsCode);
    }

    public void sendKeys(WebElement element, String keys) {
        Actions actions = new Actions(DriverManager.getDriver());
        scrollPage(element.getLocation());
        actions.sendKeys(element, keys).build().perform();
    }

    public void clearFieldByName(String field) {
        executeJS("jQuery(\"span[data-default-label='" + field + "']\").text = '';");
    }

    protected void sleep(int sec) {
        try {
            Thread.sleep(sec*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


