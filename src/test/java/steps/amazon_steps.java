package steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class amazon_steps {

    public WebDriver driver ;
    String bookName;

    @Given("^I navigate to \"([^\"]*)\"\\.$")
    public void i_navigate_to(String website) throws Throwable {
        System.setProperty("webdriver.chrome.driver", "/Users/laerciosouza/Drivers/chromedriver");
        driver = new ChromeDriver();
        driver.get("https://" + website);
    }

    @When("^I select the option \"([^\"]*)\" in the dropdown next to the search text input criteria\\.$")
    public void i_select_the_option_in_the_dropdown_next_to_the_search_text_input_criteria(String category) throws Throwable {
        WebElement dropdown = driver.findElement(By.id("searchDropdownBox"));
        new Select(dropdown).selectByVisibleText(category);
    }

    @Then("^I search for \"([^\"]*)\"\\.$")
    public void i_search_for(String searchText) throws Throwable {
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys(searchText);
        driver.findElement(By.className("nav-input")).click();
    }

    @Then("^I select the cheapest book of the page without using any sorting method available\\.$")
    public void i_select_the_cheapest_book_of_the_page_without_using_any_sorting_method_available() throws Throwable {
        Double minorPrice = 9999999.9;
        WebElement bookLink = null;
        List<WebElement> books = driver.findElements(By.className("s-include-content-margin"));
        for(WebElement element:books){
            if(element.findElements(By.className("a-price")).size() != 0){
                String wholePrice = element.findElement(By.cssSelector(".a-price .a-price-whole")).getText();
                String fractionPrice = element.findElement(By.cssSelector(".a-spacing-top-small .a-price-fraction")).getText();
                Double completePrice = Double.parseDouble(wholePrice + "." + fractionPrice);
                if(minorPrice > completePrice){
                    minorPrice = completePrice;
                    bookLink = element.findElement(By.cssSelector("h2 span"));
                    bookName = bookLink.getText();
                    System.out.println();
                }
            }else {
                System.out.println("Book " + element.findElement(By.cssSelector("h2 span")).getText() + " with price out of pattern");
            }

        }
        bookLink.click();
    }

    @When("^I reach the detailed book page, I check if the name in the header is the same name of the book that I select previously\\.$")
    public void i_reach_the_detailed_book_page_I_check_if_the_name_in_the_header_is_the_same_name_of_the_book_that_I_select_previously() throws Throwable {
        Assert.assertEquals(bookName, driver.findElement(By.id("booksTitle")).findElement(By.className("a-size-extra-large")).getText());
        driver.close();
    }

}
