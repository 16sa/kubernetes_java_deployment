package functional;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    monochrome = true,
    features = "classpath:features/",
    plugin = {
        "html:build/reports/cucumber/report.html",
        "json:build/reports/cucumber/report.json"
    },
    glue = "functional"
)
public class FunctionalTests {

}

