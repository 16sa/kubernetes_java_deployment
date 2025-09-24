package functional;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import uk.co.danielbryant.djshopping.stockmanager.model.Stock;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;

/**
 * Step definitions for the REST API functional tests.
 * This class is a glue for Cucumber, and it is configured to use the Spring
 * Boot test context. The actual test execution is handled by the
 * CucumberTestRunner class.
 */
public class RestStepDefs {

    @Autowired
    private TestRestTemplate restTemplate;

    private List<Stock> stocks;

    @Given("^the application has been initialised with test data$")
    public void init() {
        // The default profile loads synthetic stocks, so no further action is needed here.
    }

    @When("^the client gets all stocks$")
    public void getAllStocks() {
        // Use TestRestTemplate to make a GET request to the /stocks endpoint
        Stock[] stockArray = restTemplate.getForObject("/stocks", Stock[].class);
        stocks = Arrays.asList(stockArray);
    }

    @Then("^a list of (.*) stocks will be returned$")
    public void assertListOfStocksLength(int length) {
        // Assert that the returned list of stocks has the expected size
        assertThat(stocks, hasSize(length));
    }

    @Then("^the stock at index (.*) will have the sku (.*)$")
    public void assertStockHasSku(int stockIndex, String sku) {
        // Assert that the stock at a specific index has the expected SKU
        assertThat(stocks.get(stockIndex).getSku(), is(sku));
    }
}