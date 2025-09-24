package functional;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * This is the test runner class for Cucumber.
 * It's responsible for finding and running all the .feature files
 * and linking them to the step definitions.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        // The glue property specifies the package where the step definitions are located.
        glue = {"functional"},
        // The features property points to the location of the feature files.
        features = {"src/test/resources/features"}
)
public class CucumberTestRunner {
}
