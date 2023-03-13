package cucumberOptions;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/java/features", //if want to execute all files under features then only provide file lvl. Vise versa.
		glue = "stepDefinition") //glue is noting but a parameter to define stepDefinition file
public class TestRunner {

}
