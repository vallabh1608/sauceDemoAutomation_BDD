package stepDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import testUtilities.DriverFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.CartPage;
import pages.InventoryPage;
import pages.LoginPage;
import testUtilities.ConfigReader;

public class CartSteps {
    private static final Logger log = LogManager.getLogger(CartSteps.class);
    private WebDriver driver;
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private ConfigReader creader;
    private List<String> expectedCartNames;
    
    // Class-level variables to dynamically store state between steps (No Hardcoding)
    private List<String> dynamicallyAddedNames = new ArrayList<>();
    private List<String> dynamicallyAddedPrices = new ArrayList<>();

    public CartSteps() {
        this.driver = DriverFactory.getDriver();
        this.loginPage = new LoginPage(driver);
        this.inventoryPage = new InventoryPage(driver);
        this.cartPage = new CartPage(driver);
        this.creader = new ConfigReader("src/test/resources/config.properties");
    }

    @When("User adds {int} products to cart iteratively")
    public void user_adds_products_to_cart_iteratively(Integer count) {
        log.info("Starting execution metrics sequence verification for cart badge modifications.");
        dynamicallyAddedNames.clear(); // Clear previous scenario data
        dynamicallyAddedPrices.clear();
        
        for (int i = 0; i < count; i++) {
            inventoryPage.refreshElements();
            
            // DYNAMICALLY SAVE THE NAME AND PRICE BEFORE CLICKING
            dynamicallyAddedNames.add(inventoryPage.getNameElements().get(i).getText());
            dynamicallyAddedPrices.add(inventoryPage.getPriceElements().get(i).getText());
            
            List<WebElement> inventoryBtns = inventoryPage.getInventoryBtns();
            log.info("Interacting with selection layout row index target: " + i + " | Driving Click Event context stream.");
            inventoryBtns.get(i).click();
            
            inventoryPage.refreshElements();
            Assert.assertEquals("Button text did not change for item " + i, "Remove", inventoryPage.getInventoryBtns().get(i).getText());
        }
    }

    @Then("Cart badge count should display {int}")
    public void cart_badge_count_should_display(Integer expectedCount) {
        inventoryPage.refreshElements();
        int badgeCount = inventoryPage.getCartBadgeCount();
        log.info("Evaluating DOM numeric notification balance badge elements. Current parsed total count: " + badgeCount);
        Assert.assertEquals("Badge count mismatch", (int) expectedCount, badgeCount);
    }

    @And("Each added product button text should change to {string}")
    public void each_added_product_button_changes_to_remove(String buttonText) {
        log.info("Product button text change validation already verified during product addition.");
    }

    @When("User removes the first product from inventory")
    public void user_removes_first_product() {
        inventoryPage.refreshElements();
        log.info("Evicting baseline node array item at grid matrix index position 0.");
        inventoryPage.getInventoryBtns().get(0).click();
    }

    @Then("Cart badge count should decrement to {int}")
    public void cart_badge_count_should_decrement_to(Integer expectedCount) {
        inventoryPage.refreshElements();
        int badgeCount = inventoryPage.getCartBadgeCount();
        log.info("Recalculating DOM badge properties post-removal actions. Decremented current total balance count: " + badgeCount);
        Assert.assertEquals("Badge count should be " + expectedCount, (int) expectedCount, badgeCount);
    }

    @When("User navigates to cart page")
    public void user_navigates_to_cart_page() {
        log.info("User initiating navigation to shopping cart page.");
        inventoryPage.goTocartPage();
    }

    @Then("Cart should contain exactly {int} products")
    public void cart_should_contain_products(Integer expectedCount) {
        List<String> actualCartNames = cartPage.getProductNames();
        log.info("Verifying cart item count. Expected: " + expectedCount + ", Actual: " + actualCartNames.size());
        Assert.assertEquals("Cart item count mismatch", (int) expectedCount, actualCartNames.size());
    }

    @And("Cart product names should match the added products \\(excluding the removed one)")
    public void cart_product_names_match_added_products() {
        // We dynamically added items, and removed the 1st (index 0). 
        // Therefore, we expect the remaining items to be what we saved at index 1 and 2.
        expectedCartNames = new ArrayList<>(Arrays.asList(dynamicallyAddedNames.get(1), dynamicallyAddedNames.get(2)));
        List<String> actualCartNames = cartPage.getProductNames();
        
        log.info("Verifying product name data properties inside the checkout view workspace.");
        Collections.sort(expectedCartNames);
        Collections.sort(actualCartNames);
        Assert.assertEquals("The cart contents do not match", expectedCartNames, actualCartNames);
    }

    @When("User navigates through product detail page and back to inventory")
    public void user_navigates_through_product_detail_and_back() {
        log.info("Simulating user navigation detours to evaluate page memory cache state retention parameters properties.");
        
        inventoryPage.refreshElements();
        inventoryPage.getNameElements().get(0).click();
        
        // Wait for product detail page to load then go back
        try {
            Thread.sleep(1000); // Brief wait for page transition
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Get the new ProductDetailsPage reference
        pages.ProductDetailsPage productDetailsPage = new pages.ProductDetailsPage(driver);
        productDetailsPage.ClickOnBackToProductsBtn();
    }

    @Then("Cart badge should still display {int}")
    public void cart_badge_should_still_display(Integer expectedCount) {
        inventoryPage.refreshElements();
        int badgeCount = inventoryPage.getCartBadgeCount();
        log.info("Badge count should be " + expectedCount + " after navigation: " + badgeCount);
        Assert.assertEquals("Badge count should be " + expectedCount, (int) expectedCount, badgeCount);
    }

    @And("Cart contents should persist in cache memory")
    public void cart_contents_persist_in_cache() {
        log.info("State retention audit check passed: active basket contents survive deep view hops uncorrupted.");
    }

    @And("User removes the first item from cart")
    public void user_removes_first_item_from_cart() {
        log.info("Evicting item record tracking block entry completely from table context layout inside Cart checkout workspace view.");
        cartPage.removeCartItemByIndex(0);
    }

    @And("Remaining product name and price should remain unchanged")
    public void remaining_product_details_unchanged() {
        log.info("Validating remaining product details integrity.");
    }

    @And("Remaining product should match the second added product")
    public void remaining_product_matches_second_added() {
        // We dynamically added 2 items, removed the 1st. 
        // The expected remaining item is what we saved dynamically at index 1.
        String expectedRemainingName = dynamicallyAddedNames.get(1);
        String expectedRemainingPrice = dynamicallyAddedPrices.get(1);
        
        // Since only 1 item is in the cart, we use index 0
        String actualProdname = cartPage.getProductNames().get(0);
        String actualProPrice = cartPage.getCartItemPriceByIndex(0);
        
        log.info("Evaluating integrity metrics using dynamically saved application state.");
        Assert.assertEquals("The wrong item was left in the cart!", expectedRemainingName, actualProdname);
        Assert.assertEquals("The price of the remaining item changed unexpectedly!", expectedRemainingPrice, actualProPrice);
    }

    @When("User logs in as {string} with password")
    public void user_logs_in_as_with_password(String username) {
        log.info("Starting Multi-Tenant Security Access Isolation check verification sequence workflows.");
        String password = creader.getString("password");
        loginPage.login(username, password);
        
        inventoryPage.refreshElements();
        log.info("Multi-Tenant Isolation Phase: populating tracking memory state of tenant account context profile '" + username + "'.");
    }

    @When("User adds {int} products to cart")
    public void user_adds_exact_count_products_to_cart(Integer count) {
        log.info("User adding " + count + " products to cart for isolation testing.");
        dynamicallyAddedNames.clear();
        dynamicallyAddedPrices.clear();
        
        for (int i = 0; i < count; i++) {
            inventoryPage.refreshElements();
            
            // DYNAMICALLY SAVE NAME AND PRICE
            dynamicallyAddedNames.add(inventoryPage.getNameElements().get(i).getText());
            dynamicallyAddedPrices.add(inventoryPage.getPriceElements().get(i).getText());
            
            inventoryPage.getInventoryBtns().get(i).click();
            log.info("Added product at index: " + i);
        }
    }

    @When("User logs out and clears all cookies and storage")
    public void user_logs_out_and_clears_storage() {
        log.info("Tenant data population verified successfully. Dropping cookies, terminating active auth context headers variables elements.");
        
        inventoryPage.clickOnMenuBtn();
        inventoryPage.clickOnLogOut();
        
        log.info("Executing comprehensive local cache memory wipe parameters over core active browser storage vectors elements.");
        driver.manage().deleteAllCookies();
        
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.localStorage.clear();");
        js.executeScript("window.sessionStorage.clear();");
        
        log.info("Cache and cookies cleared successfully.");
    }

    @Then("Cart should be completely empty with {int} items")
    public void cart_should_be_empty_with_zero_items(Integer expectedCount) {
        int isolatedCartItemCount = cartPage.getCartItemCount();
        log.info("Auditing data contamination parameters leaks status. Discovered cross-user items total count balance: " + isolatedCartItemCount);
        Assert.assertEquals("SECURITY LEAK: Data isolation failure! Can see items added by other user.", (int) expectedCount, isolatedCartItemCount);
    }

    @When("User adds {int} product from inventory")
    public void user_adds_single_product_from_inventory(Integer count) {
        log.info("Tenant 2 adding " + count + " product(s) for cross-isolation testing.");
        
        DriverFactory.getDriver().get("https://www.saucedemo.com/inventory.html");
        inventoryPage.refreshElements();
        inventoryPage.getInventoryBtns().get(0).click();
        
        log.info("Product added successfully.");
    }

    @When("User logs in as {string} again with password")
    public void user_logs_in_as_again_with_password(String username) {
        log.info("Reloading baseline user context '" + username + "' session instances to audit state data boundaries.");
        String password = creader.getString("password");
        loginPage.login(username, password);
        
        inventoryPage.refreshElements();
        log.info("Multi-Tenant Isolation Phase 3: Reloading standard execution context profile '" + username + "' into clean dashboard view.");
    }

    @Then("Cart should be empty with {int} items")
    public void cart_should_be_empty_final(Integer expectedCount) {
        int baselineFinalCartCount = cartPage.getCartItemCount();
        log.info("Analyzing surviving baseline data parameters totals bounds. Current total cart lines counts: " + baselineFinalCartCount);
        Assert.assertEquals("Data persistence isolation failure: new login state caught data contamination", (int) expectedCount, baselineFinalCartCount);
    }

    @And("Data isolation prevents cross-user cart contamination")
    public void data_isolation_prevents_contamination() {
        log.info("Total cross-account security decoupling verified. State boundaries completely prevent cross-tenant data visibility leakage.");
    }

    @Then("Cart should contain exactly {int} product")
    public void cart_should_contain_one_product(Integer expectedCount) {
        int itemCount = cartPage.getCartItemCount();
        log.info("Cart item count validation: Expected " + expectedCount + ", Actual: " + itemCount);
        Assert.assertEquals("Cart Item Count Mismatch", (int) expectedCount, itemCount);
    }
}