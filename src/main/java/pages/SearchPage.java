package pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchPage extends AbstractPage {

    public WebElement getSearchField() {
        return getElement("id", "search-text");
    }

    public WebElement getRegion() {
        return getElement("id", "cityField");
    }

    public void search() {
        clickElement(getElement("id", "search-submit"));
    }

    public void applyFilters() {
        sleep(2);
        search();
    }

    public WebElement getSelectedCategory() {
        return getElement("id", "main-category-choose-label");
    }

    public List<String> getCarBrandsList() {
        List<String> carBrandsList = new ArrayList<>();
        // scrolling down that dropbox was displayed
        scrollPageY(800);
        // click dropdown for list was available
        clickElement(getElement("css", "div[id=subSelect108] + a > span"));
        waitElementIsDisplayed(getElement("css", "ul[class=\"small suggestinput bgfff lheight20 br-3 abs subcategories binded\"] > li:nth-child(2) > a"));
        List<WebElement> list = getElements("ul[class=\"small suggestinput bgfff lheight20 br-3 abs subcategories binded\"] > li > a");
        for (WebElement element : list) {
            String carBrandName = element.getText().replaceAll("\\d+", "").trim();
            if ( carBrandName.length()>0 )
                carBrandsList.add(carBrandName);
        }
        return carBrandsList;
    }

    public void setPrice(String field, String price) {
        WebElement el;
        sleep(5);
        el = (field.toLowerCase().equals("to")) ?
                getElement("css", "li[id=param_price] > div > div:nth-child(2) > a > span:nth-child(1)") :
                getElement("css", "li[id=param_price] > div > div:nth-child(1) > a > span:nth-child(1)");
        clearFieldByName(field);
        sendKeys(el, price);
    }

    public void setPriceFrom(String price){
        setPrice("from", price);
    }

    public void setPriceTo(String price){
        setPrice("to", price);
    }

    /**
     * Get price element "to" if @fieldName equals "to", "from" otherwise
     *
     * @param fieldName
     * @return price
     */
    public String getPrice(String fieldName) {
        WebElement el = ( fieldName.toLowerCase().equals("to") ) ?
                getElement("css", "li[id=param_price] > div > div:nth-child(2) > a > span:nth-child(1)") :
                getElement("css", "li[id=param_price] > div > div:nth-child(1) > a > span:nth-child(1)");

        return el.getAttribute("innerText");
    }

    public String getPriceFrom() {
        return getPrice("from");
    }

    public String getPriceTo() {
        return getPrice("to");
    }

    public void setMileage(String field, String value) {
        WebElement el = ( field.toLowerCase().equals("to") ) ?
            getElement("xpath", "//li[@id='param_motor_mileage']/div/div[2]/a/span[@class='header block']") :
            getElement("xpath", "//li[@id='param_motor_mileage']/div/div[1]/a/span[@class='header block']");
        sendKeys(el, value);
    }

    public void setMileageFrom(String value) {
        setMileage("from", value);
    }

    public void setMileageTo(String value) {
        setMileage("to", value);
    }

    /**
     * Set mileage from dropdown by index in list
     *
     * @param field can be "from" or "to"
     * @param index index of item from list. Started from 1
     */
    public void setMileageByIndex(String field, int index) {
        if ( index < 1 ) return;

        getElement("xpath", "//li[@data-key='motor_mileage']/div/div[1]/a").click();
        waitElementIsDisplayed(getElement("xpath", "//li[@id='param_motor_mileage']/div[2]/div[1]/ul"));
        WebElement item;
        try {
            item = getElement("xpath", "//li[@id='param_motor_mileage']/div/div[1]/ul/li[" + index + "]/a");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Element with index='" + index + "' not found");
        }
        item.click();
    }

    public List<WebElement> getTransmissions() {
        List<WebElement> transmissions = null;
        List<WebElement> list = getElements("#param_transmission_type ul li");
        Map<String, WebElement> t = null;
        t.put("a", null);
        return transmissions;
    }

    public WebElement getTransmissionTypeCheckbox(Transmission type) {
        WebElement dropdownTransmissionElement = getElement("css", "#param_transmission_type div a span:nth-child(1)");
        moveToElement(dropdownTransmissionElement);
        clickElement(dropdownTransmissionElement);
        sleep(5);
        waitElementIsDisplayed(getElement("xpath", "//ul[@class='small suggestinput bgfff lheight20 br-3 abs select binded']"));
        return getElement("id", Transmission.getLocatorByName(type));
    }

    public void setTransmissionType(Transmission type) {
        WebElement checkbox = getTransmissionTypeCheckbox(type);
        setCheckbox(checkbox, true);
    }

    /**
     * Set checkbox 'checked' status
     *
     * @param el     WebElement
     * @param status 'true' - checked, 'false' - unchecked
     */
    public void setCheckbox(WebElement el, boolean status) {
        if (((!el.isSelected()) && (status==true)) || ((el.isSelected()) && (status==false))) {
            executeJS("jQuery(\"#" + el.getAttribute("id") + "\").parent().get(0).click();");
        }
    }

    public boolean isChecked(WebElement el) {
        return el.isSelected();
    }

    /**
     * Closing any popup windows which can overlay document
     */
    protected void checkPopupAndCloseIt() {
        WebElement webElement = null;
        try {
            webElement = getElement("css", "div[class='gtm-survey js-gtm-survey gtm-survey--in-arrow']");
        } catch (Exception e) {

        }
        if ( webElement!=null ) {
            WebElement closeBtn = getElement("css", "*[class='gtm-survey__close js-gtm-survey-close']");
            clickElement(closeBtn);
        }

        // get Agree button
        webElement = null;
        try {
            webElement = getElement("css", "button[class='cookie-close abs cookiesBarClose']");
        } catch (Exception e) {
        }
        if ( webElement!=null ) {
            clickElement(webElement);
        }
    }
}
