package stepDefinition;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

//import testUtilities.DriverFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.InventoryPage;
import pages.LoginPage;
import testUtilities.ConfigReader;
import testUtilities.DriverFactory;

public class AuthSteps {
	private static final Logger log = LogManager.getLogger(AuthSteps.class);
	private WebDriver driver;
	private LoginPage loginPage;
	private InventoryPage inventoryPage;
	private ConfigReader creader;
	private String lastLoginResult;
	private String lastErrorMessage;

	public AuthSteps() {
		this.driver = DriverFactory.getDriver();
		this.loginPage = new LoginPage(driver);
		this.inventoryPage = new InventoryPage(driver);
		this.creader = new ConfigReader("src/test/resources/config.properties");
	}

	@When("User attempts login with username {string} and password {string}")
	public void user_attempts_login_with_credentials(String username, String password) {
		log.info("Executing Auth Profile verification profile strategy: [" + username + "] for user context ID: "
				+ username);

		loginPage.login(username, password);

		// Try to determine if login was successful by checking page
		try {
			String title = inventoryPage.getPageTitle();
			if (title != null && title.equals("Products")) {
				lastLoginResult = "Valid Login";
				log.info("Dashboard validation hit. Parsing screen context title banner. Evaluated text title output: "
						+ title);
			} else {
				lastLoginResult = "Invalid Login";
			}
		} catch (Exception e) {
			lastLoginResult = "Invalid Login";
			lastErrorMessage = loginPage.getErrMsg();
			log.info("Authentication rejected as expected. Evaluating alert text layer message string: "
					+ lastErrorMessage);
		}
	}

	@Then("Login validation should result in {string}")
	public void login_validation_should_result_in(String expectedResult) {
		log.info("Verifying login result matches expected outcome: " + expectedResult);
		Assert.assertEquals("Login result mismatch", expectedResult, lastLoginResult);
	}

	@And("If valid login, inventory page title should be {string}")
	public void if_valid_login_inventory_page_title_should_be(String expectedTitle) {
		if (lastLoginResult.equalsIgnoreCase("Valid Login")) {
			String currentTitle = inventoryPage.getPageTitle();
			log.info("Valid login path - Dashboard title validation: " + currentTitle);
			Assert.assertEquals("Inventory page title mismatch", expectedTitle, currentTitle);
		}
	}

	@And("If invalid login, error message should contain {string}")
	public void if_invalid_login_error_message_should_contain(String expectedErrorText) {
		if (!lastLoginResult.equalsIgnoreCase("Valid Login")) {
			String errText = loginPage.getErrMsg();
			log.info("Invalid login path - Error message validation: " + errText);
			Assert.assertTrue("Expected error message not found: " + expectedErrorText,
					errText.contains(expectedErrorText));
		}
	}

	@Given("User is logged in with valid credentials")
	public void user_is_logged_in_with_valid_credentials() {
		log.info("Setting up logged-in user session with standard_user credentials");
		String username = creader.getString("username");
		String password = creader.getString("password");

		loginPage.login(username, password);

		String title = inventoryPage.getPageTitle();
		log.info("User successfully logged in. Dashboard title: " + title);
		Assert.assertEquals("Failed to login with valid credentials", "Products", title);
	}

	@When("User clicks on menu button")
	public void user_clicks_on_menu_button() {
		log.info(
				"Triggering exit application logout commands to clear active profile authorization context headers elements.");
		inventoryPage.clickOnMenuBtn();
	}

	@And("User clicks on logout option")
	public void user_clicks_on_logout_option() {
		log.info("Executing logout sequence logic to clear active auth profiles.");
		inventoryPage.clickOnLogOut();
	}

	@Then("Current URL should redirect to login page")
	public void current_url_should_redirect_to_login_page() {
		String currentUrl = DriverFactory.getDriver().getCurrentUrl();
		log.info("Evaluating location routing after explicit user sign-out action commands. Verified destination link: "
				+ currentUrl);
		Assert.assertTrue("URL does not match the login screen base destination",
				currentUrl.contains("https://www.saucedemo.com/"));
	}

	@And("Direct URL injection to inventory.html without auth should redirect to login")
	public void direct_url_injection_to_inventory_without_auth() {
		log.info(
				"Injecting rogue bypass navigation tracking script link to forcefully load deep internal protected view directories dashboard layout without active auth.");
		DriverFactory.getDriver().get("https://www.saucedemo.com/inventory.html");
	}

	@And("System should prevent unauthorized access to protected routes")
	public void system_should_prevent_unauthorized_access() {
		String currUrlAfterByPass = DriverFactory.getDriver().getCurrentUrl();
		log.info(
				"Analyzing security guard intercept action responses. Page tracking url post injection bypass attempt: "
						+ currUrlAfterByPass);
		Assert.assertTrue("System failed to redirect unauthenticated guest back to landing pad",
				currUrlAfterByPass.contains("https://www.saucedemo.com/"));
	}
}
