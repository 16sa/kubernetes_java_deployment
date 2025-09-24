package functional;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import uk.co.danielbryant.djshopping.stockmanager.StockManagerApplication;


@CucumberContextConfiguration
@SpringBootTest(classes = StockManagerApplication.class)
public class CucumberSpringConfiguration {
}
