import org.apache.commons.lang3.Range;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import pages.ResultPage;
import pages.SearchPage;
import pages.Transmission;
import utils.DriverManager;
import utils.WebDriverLogger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

@Listeners({WebDriverLogger.class})
public class TestSearchPage extends SearchPage {
    private DriverManager browser = new DriverManager();

//    private String url = "https://www.olx.ua/transport/legkovye-avtomobili/dnepr/q-%D0%BB%D0%B5%D0%B3%D0%BA%D0%BE%D0%B2%D1%8B%D0%B5-%D0%B0%D0%B2%D1%82%D0%BE%D0%BC%D0%BE%D0%B1%D0%B8%D0%BB%D0%B8/";
    private String url = "https://www.olx.ua/transport/legkovye-avtomobili/q-%D0%BB%D0%B5%D0%B3%D0%BA%D0%BE%D0%B2%D1%8B%D0%B5-%D0%B0%D0%B2%D1%82%D0%BE%D0%BC%D0%BE%D0%B1%D0%B8%D0%BB%D0%B8/";

    @BeforeClass
    public void beforeClass() throws IOException {
        browser.startBrowser();
        browser.goToUrl(url);
        checkPopupAndCloseIt();
    }

    @BeforeMethod
    public void beforeMethod() {
        browser.goToUrl(url);
    }

    @AfterClass
    public void afterClass(){
        browser.close();
    }

    @Test
    public void verifyDefaultFieldsTest(){
        WebElement selectedCategory = getSelectedCategory();
        verifyDefaultFieldsAssert(selectedCategory.getText());
    }

    @Test
    public void verifyCarsInListTest() {
        List<String> expectedCarBrandsExist = Arrays.asList("Acura", "BMW");
        List<String> expectedCarBrandsInvented = Arrays.asList("Invented", "brands");
        List<String> actualCarsBrands = getCarBrandsList();
        assertTrue(actualCarsBrands.containsAll(expectedCarBrandsExist), "Can't find one of car brands in the list");
        assertFalse(actualCarsBrands.containsAll(expectedCarBrandsInvented), "Find invented car brands in the list");
    }

    @Test
    public void verifyPriceHasOnlyDigitalTest(){
        final List<String> errorPriceFrom = Arrays.asList("Aa-55./_+", "55");
        final List<String> errorPriceTo = Arrays.asList("zD+78.", "78");
        final String curSuffix = " грн.";

        setPriceFrom(errorPriceFrom.get(0));
        setPriceTo(errorPriceTo.get(0));
        assertEquals(getPriceFrom().trim(), errorPriceFrom.get(1).concat(curSuffix) );
        assertEquals(getPriceTo().trim(), errorPriceTo.get(1).concat(curSuffix));
    }

    @Test
    public void verifyMileageFilterTest(){
        setMileageByIndex("from", 3);
        setMileageTo("300 000 км");
        applyFilters();

        // на странице нет возможности проверить результат. Как вариант, обходить и открывать все найденные объявления
    }

    @Test
    public void verifyPriceFilterTest() {
        final String priceFrom = "10000";
        final String priceTo = "200000";
        ResultPage page = new ResultPage();

        setPriceFrom(priceFrom);
        setPriceTo(priceTo);
        applyFilters();
        verifyPriceRange(page.getPrices(), Integer.valueOf(priceFrom), Integer.valueOf(priceTo));
    }

    @Test
    public void verifyCheckboxesStatusTest(){
        verifyTransmissionTypeCheckbox(true, "Checkbox 'All' is not checked");

        setTransmissionType(Transmission.AUTOMATIC);
        verifyTransmissionTypeCheckbox(false, "Checkbox 'All' is checked");
    }

    private void verifyTransmissionTypeCheckbox(boolean expected, String message) {
        WebElement checkbox = getTransmissionTypeCheckbox(Transmission.ALL);
        assertEquals(checkbox.isSelected(), expected, message);
    }

    private void verifyDefaultFieldsAssert(String selectedCategory) {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(selectedCategory, "Легковые автомобили", "Category is different");
        softAssert.assertEquals(getPrice("from"), "от (грн.)", "Price \"from\" is different");
        softAssert.assertEquals(getPrice("to"), "до (грн.)", "Price \"to\" is different");
        softAssert.assertEquals(isChecked(getElement("id", "photo-only")), false, "With photo checked");
        softAssert.assertAll();
    }

    private void verifyPriceRange(List<String> carsElements, Integer priceFrom, Integer priceTo) {
        if ( carsElements==null ) {
            fail("No elements to compare found");
        }
        SoftAssert softAssert = new SoftAssert();
        carsElements.forEach((x->{
            Range<Integer> myRange = Range.between(priceFrom, priceTo);
            if ( !x.equals("") )
                softAssert.assertTrue(myRange.contains(Integer.valueOf(x)));
        }));
        softAssert.assertAll();
    }
}
