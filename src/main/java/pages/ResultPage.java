package pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class ResultPage extends AbstractPage {

    public List<String> getPrices() {
        List<WebElement> list;
        List<String> resultList = new ArrayList<>();
        try {
            sleep(5);
            waitElementIsDisplayed(getElement("id", "offers_table"));
        } catch (NoSuchElementException e) {
//            throw new NoSuchElementException("No cars matches criteria found");
        }
        list = getElements("table[id='offers_table'] p > strong");
        for (WebElement x : list) {
            resultList.add(x.getAttribute("innerText").replaceAll("\\D+", ""));
        }
        return resultList;
    }

}
