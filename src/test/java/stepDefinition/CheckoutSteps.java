package stepDefinition;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import testUtilities.DriverFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.CartPage;
import pages.CheckOutPage;
import pages.InventoryPage;
import pages.LoginPage;
import pages.SummaryPage;
import testUtilities.ConfigReader;

public class CheckoutSteps {
    private static final Logger log = LogManager.getLogger(CheckoutSteps.class);
    private WebDriver driver;
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckOutPage checkOutPage;
    private SummaryPage summaryPage;
    private ConfigReader creader;

    public CheckoutSteps() {
        this.driver = DriverFactory.getDriver();
        this.loginPage = new LoginPage(driver);
        this.inventoryPage = new InventoryPage(driver);
        this.cartPage = new CartPage(driver);
        this.checkOutPage = new CheckOutPage(driver);
        this.summaryPage = new SummaryPage(driver);
        this.creader = new ConfigReader("src/test/resources/config.properties");
    }

    @When("User adds all {int} products to cart")
    public void user_adds_all_products_to_cart(Integer productCount) {
        log.info("Starting complete end-to-end checkout purchase execution pipeline workflow.");
        
        java.util.List<WebElement> productNames = inventoryPage.getNameElements();
        java.util.List<WebElement> inventoryBtns = inventoryPage.getInventoryBtns();
        
        log.info("Scanning inventory landing catalog table for all products. Total products count: " + productCount);
        
        for (int i = 0; i < productCount; i++) {
            inventoryBtns.get(i).click();
            log.info("Added product at index: " + i);
        }
    }

    @Then("Cart badge count should equal total product count")
    public void cart_badge_count_equals_total_products() {
        java.util.List<WebElement> productNames = inventoryPage.getNameElements();
        int expectedBadgeCount = productNames.size();
        int actualBadgeCount = inventoryPage.getCartBadgeCount();
        
        log.info("Total products in inventory: " + expectedBadgeCount + " | Cart badge count: " + actualBadgeCount);
        Assert.assertEquals("Badge count does not match product count", expectedBadgeCount, actualBadgeCount);
    }

    @When("User initiates checkout process")
    public void user_initiates_checkout_process() {
        log.info("User navigating to cart page from inventory.");
        cartPage.clickCheckOut();
    }

    @And("User enters checkout details with firstname, lastname, and zipcode")
    public void user_enters_checkout_details() {
        log.info("Populating form execution variables with properties context values.");
        
        String firstname = creader.getString("firstname");
        String lastname = creader.getString("lastname");
        String zipcode = creader.getString("zipCode");
        
        checkOutPage.enterDetails(firstname, lastname, zipcode);
        log.info("Checkout details entered: " + firstname + " " + lastname + " " + zipcode);
    }

    @And("User clicks continue button")
    public void user_clicks_continue_button() {
        log.info("User clicking continue button on checkout form.");
        checkOutPage.clickContinue();
    }

    @Then("Order summary should display subtotal, tax, and total amounts")
    public void order_summary_displays_amounts() {
        log.info("Order summary page loaded. Executing mathematical balance verification audits.");
        
        double itemPrice = summaryPage.getSubTotalAmt();
        double taxAmt = summaryPage.getTaxAmt();
        double expectedTotalAmt = summaryPage.getFinalTotalAmt();
        
        log.info("Order Summary Amounts - Subtotal: $" + itemPrice + " | Tax: $" + taxAmt + " | Total: $" + expectedTotalAmt);
        
        Assert.assertTrue("Subtotal amount is not positive", itemPrice > 0);
        Assert.assertTrue("Tax amount is not positive", taxAmt > 0);
        Assert.assertTrue("Total amount is not positive", expectedTotalAmt > 0);
    }

    @And("Total amount should equal subtotal plus tax")
    public void total_equals_subtotal_plus_tax() {
        log.info("Executing mathematical balance verification audits across transaction summary price strings.");
        
        double itemPrice = summaryPage.getSubTotalAmt();
        double taxAmt = summaryPage.getTaxAmt();
        double actualTotalAmt = itemPrice + taxAmt;
        double expectedTotalAmt = summaryPage.getFinalTotalAmt();
        
        log.info("Mathematical matrix evaluation -> Base Cost Subtotal: " + itemPrice + " | Added Calculated Sales Tax: " + taxAmt + " | Expected Final Grand Total: " + expectedTotalAmt);
        
        Assert.assertEquals("Math mismatch in order summary!", expectedTotalAmt, actualTotalAmt, 0.01);
    }

    @When("User clicks finish button")
    public void user_clicks_finish_button() {
        log.info("User clicking finish button to complete order.");
        summaryPage.clickFinish();
    }

    @Then("Confirmation message should display {string}")
    public void confirmation_message_displays(String expectedMessage) {
        String confirmationMsg = summaryPage.getConfirmationMsg();
        log.info("Order submission finalized completely. Validating checkout confirmation screen response string message: " + confirmationMsg);
        Assert.assertEquals("Final order confirmation failed", expectedMessage, confirmationMsg);
    }

    @And("Order completion workflow should succeed")
    public void order_completion_workflow_succeeds() {
        log.info("E2E purchase process sequence achieved completely. Order confirmation greeting screen logged.");
    }
}
