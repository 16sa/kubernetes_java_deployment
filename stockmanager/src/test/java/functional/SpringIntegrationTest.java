package functional;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import uk.co.danielbryant.djshopping.stockmanager.StockManagerApplication;

/**
 * This class is a dedicated configuration for the Spring test context
 * used by Cucumber. By separating this from the step definitions,
 * we ensure that the Spring context is correctly loaded before any
 * steps are executed.
 */
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = StockManagerApplication.class)
public class SpringIntegrationTest {
}
