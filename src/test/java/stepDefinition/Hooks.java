package stepDefinition;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import testUtilities.DriverFactory;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import testUtilities.ConfigReader;

public class Hooks {
    private static final Logger log = LogManager.getLogger(Hooks.class);
    private ConfigReader creader;
    private Scenario scenario;

    @Before
    public void setUp(Scenario scenario) {
        this.scenario = scenario;
        log.info("====================================================================");
        log.info("Initializing Cucumber scenario: " + scenario.getName());
        log.info("====================================================================");
        
        creader = new ConfigReader("src/test/resources/config.properties");
        String browser = creader.getString("browser");
        String url = creader.getString("url");
        String mode = creader.getString("execution.mode");
        boolean headlessFlag = Boolean.parseBoolean(creader.getString("browser.headless"));
        
        log.info("Configurations -> Browser: " + browser + ", Mode: " + mode + ", Headless: " + headlessFlag);
        
        DriverFactory.initDriver(browser, mode, headlessFlag);
        
        log.info("Navigating to URL: " + url);
        DriverFactory.getDriver().get(url);
        
        // Mutes the long, harmless Selenium CDP warnings from the console log output
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(java.util.logging.Level.SEVERE);
    }

    @After
    public void tearDown() {
        if (scenario.isFailed()) {
            log.error("Scenario FAILED: " + scenario.getName());
            
            // Capture screenshot and attach as Base64
            WebDriver driver = DriverFactory.getDriver();
            if (driver != null) {
                try {
                    TakesScreenshot screenshot = (TakesScreenshot) driver;
                    String base64Screenshot = screenshot.getScreenshotAs(OutputType.BASE64);
                    
                    // Attach screenshot to Cucumber report
                    scenario.attach(base64Screenshot, "image/png", "Failure Screenshot - " + scenario.getName());
                    log.info("Base64 screenshot successfully attached to Cucumber report.");
                } catch (Exception e) {
                    log.error("Failed to capture screenshot: ", e);
                }
            }
        } else {
            log.info("Scenario PASSED: " + scenario.getName());
        }
        
        // Close browser and cleanup
        if (DriverFactory.getDriver() != null) {
            log.info("Closing browser driver instance.");
            DriverFactory.getDriver().quit();
        }
        
        DriverFactory.unloadDriver();
        
        log.info("====================================================================");
        log.info("Scenario execution completed: " + scenario.getName());
        log.info("====================================================================");
    }
}
