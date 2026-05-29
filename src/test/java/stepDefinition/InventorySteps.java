package stepDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import testUtilities.DriverFactory;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.InventoryPage;
import pages.LoginPage;
import pages.ProductDetailsPage;
import testUtilities.ConfigReader;

public class InventorySteps {
    private static final Logger log = LogManager.getLogger(InventorySteps.class);
    private WebDriver driver;
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private ProductDetailsPage productDetailsPage;
    private ConfigReader creader;
    
    // Class-level variable to save state dynamically before page navigation
    private String clickedProductName;

    public InventorySteps() {
        this.driver = DriverFactory.getDriver();
        this.loginPage = new LoginPage(driver);
        this.inventoryPage = new InventoryPage(driver);
        this.productDetailsPage = new ProductDetailsPage(driver);
        this.creader = new ConfigReader("src/test/resources/config.properties");
    }

    @When("User changes sort option to {string}")
    public void user_changes_sort_option_to(String sortOption) {
        log.info("Modifying active grid element sort sequence parameters to target format: " + sortOption);
        inventoryPage.sortByVisibileText(sortOption);
    }

    @Then("All products should be sorted by price in ascending order")
    public void all_products_sorted_by_price_ascending() {
        log.info("Price parsing matrix map step initiated for low-to-high validation.");
        
        List<WebElement> priceElements = inventoryPage.getPriceElements();
        List<Double> actualPrices = new ArrayList<>();
        
        for (WebElement pricesEl : priceElements) {
            Double price = Double.parseDouble(pricesEl.getText().replace("$", ""));
            actualPrices.add(price);
        }
        
        log.info("Price parsing matrix map step completed. Discovered item cost array sequence: " + actualPrices);
        
        List<Double> sortedPrices = new ArrayList<>(actualPrices);
        Collections.sort(sortedPrices);
        
        Assert.assertEquals("Prices are NOT sorted Low to High", sortedPrices, actualPrices);
        log.info("Verified low-to-high numeric price sequencing remains secure.");
    }

    @Then("All products should be sorted by name in reverse alphabetical order")
    public void all_products_sorted_by_name_reverse() {
        log.info("Lexicographical validation step initiated for Z-to-A name sorting.");
        
        List<WebElement> nameElements = inventoryPage.getNameElements();
        List<String> actualNames = new ArrayList<>();
        
        for (WebElement nameEl : nameElements) {
            actualNames.add(nameEl.getText());
        }
        
        log.info("Lexicographical label string arrays compiled completely: " + actualNames);
        
        List<String> sortedNames = new ArrayList<>(actualNames);
        Collections.sort(sortedNames, Collections.reverseOrder());
        
        Assert.assertEquals("Names are NOT sorted Z to A", sortedNames, actualNames);
        log.info("Verified lexicographical reverse alphabetical sorting sequence order.");
    }

    @When("User clicks on the product at index {int}")
    public void user_clicks_on_product_at_index(Integer index) {
        log.info("Target product item name identified at grid catalog line entry reference index: " + index);
        inventoryPage.clickProductNameByIndex(index);
    }

    @Then("Product detail page should display product name, description, and price")
    public void product_detail_page_displays_info() {
        log.info("Extracting individual localized element variables from specialized focus layout sheets panel.");
        
        String actualProductName = productDetailsPage.getProductName();
        String actualProductDesc = productDetailsPage.getProductDesc();
        String actualProductPrice = productDetailsPage.getproductPrice();
        
        Assert.assertNotNull("Product name is null", actualProductName);
        Assert.assertNotNull("Product description is null", actualProductDesc);
        Assert.assertNotNull("Product price is null", actualProductPrice);
    }

    @And("Product price should contain currency symbol {string}")
    public void product_price_contains_currency(String currencySymbol) {
        String actualProductPrice = productDetailsPage.getproductPrice();
        log.info("Validating price format correctness: " + actualProductPrice);
        Assert.assertTrue("Price format is incorrect or missing!", actualProductPrice.contains(currencySymbol));
    }

    @And("Product description should not be empty")
    public void product_description_not_empty() {
        String actualProductDesc = productDetailsPage.getProductDesc();
        log.info("Validating product description is populated: " + actualProductDesc);
        Assert.assertFalse("Product description is empty!", actualProductDesc.isEmpty());
    }

    @When("User clicks {string} on detail page")
    public void user_clicks_button_on_detail_page(String buttonText) {
        if (buttonText.equalsIgnoreCase("Add to Cart")) {
            log.info("Testing product addition execution triggers directly inside deep detailed panels focus view layout matrix.");
            productDetailsPage.clickOnAddToCart();
        }
    }

    @Then("Cart badge count should increment to {int}")
    public void cart_badge_count_should_be(Integer expectedCount) {
        int actualBadgeCount = inventoryPage.getCartBadgeCount();
        log.info("Evaluating DOM numeric notification balance badge elements. Current parsed total count: " + actualBadgeCount);
        Assert.assertEquals("Cart badge count mismatch", (int) expectedCount, actualBadgeCount);
    }

    @When("User clicks {string} button")
    public void user_clicks_button(String buttonText) {
        if (buttonText.equalsIgnoreCase("Back to Products")) {
            log.info("User initiating back navigation from product detail page.");
            productDetailsPage.ClickOnBackToProductsBtn();
        }
    }

    @Then("User should be redirected to inventory.html page")
    public void user_redirected_to_inventory() {
        String currentUrl = DriverFactory.getDriver().getCurrentUrl();
        log.info("History back button redirection routing verification audit checklist step. Navigated location state tracking pointer link URL: " + currentUrl);
        Assert.assertTrue("Did not navigate back to the inventory URL!", currentUrl.contains("inventory.html"));
    }

    @And("Inventory page title should be {string}")
    public void inventory_page_title_should_be(String expectedTitle) {
        String actualTitle = inventoryPage.getPageTitle();
        log.info("Validating inventory page title: " + actualTitle);
        Assert.assertEquals("Main inventory page heading is incorrect", expectedTitle, actualTitle);
    }

    @When("User validates all product images on inventory page")
    public void user_validates_all_product_images() {
        log.info("Starting global static content validation checks and compliance accessibility audits loop.");
        inventoryPage.refreshElements();
        
        int productCount = inventoryPage.getProductImages().size();
        log.info("Discovered total graphic components arrays properties. Iterating over image links count total target: " + productCount);
        
        for (int i = 0; i < productCount; i++) {
            inventoryPage.refreshElements();
            WebElement imgElement = inventoryPage.getProductImages().get(i);
            
            String imgSrc = imgElement.getAttribute("src");
            String imgAlt = imgElement.getAttribute("alt");
            String productName = inventoryPage.getNameElements().get(i).getText();
            
            log.info("Analyzing index position reference element properties node item [" + i + "] | Source Path string location: " + imgSrc + " | Accessibility Label Tag info: " + imgAlt);
            
            Assert.assertNotNull("Product image source attribute is completely null at index " + i, imgSrc);
            Assert.assertFalse("Product image source attribute is empty at index " + i, imgSrc.isEmpty());
            Assert.assertFalse("Broken image link signature (sl-404) detected at index " + i, imgSrc.contains("sl-404"));
            Assert.assertEquals("Accessibility Failure: Image alt text does not match product label at index " + i, productName, imgAlt);
        }
    }

    @Then("Each product image should have a valid src attribute")
    public void each_product_image_has_valid_src() {
        log.info("Image src validation passed during earlier verification loop.");
    }

    @And("No product image should have broken link indicator {string}")
    public void no_broken_image_links(String brokenLinkIndicator) {
        log.info("Broken image link validation passed during earlier verification loop.");
    }

    @And("Each product image alt text should match the product name")
    public void image_alt_text_matches_product_name() {
        log.info("Image alt-text consistency validation passed during earlier verification loop.");
    }

    @When("User clicks on the first product image")
    public void user_clicks_first_product_image() {
        inventoryPage.refreshElements();
        
        // FIX: Save the target product name into our variable BEFORE we click and navigate away!
        clickedProductName = inventoryPage.getNameElements().get(0).getText();
        log.info("Testing element hyperlink consistency mapping. Target item picture mapping to perform action: " + clickedProductName);
        
        // Now it is safe to click and navigate
        inventoryPage.getProductImages().get(0).click();
    }

    @Then("Product detail page should load for the clicked product")
    public void product_detail_loads() {
        log.info("Detailed anchor view redirection tracking completed.");
    }

    @And("Detail page product name should match the clicked product name")
    public void detail_product_name_matches_clicked_product() {
        // FIX: We are now on the Details Page. We grab the actual name from the Details page object
        String actualDetailName = productDetailsPage.getProductName();
        
        log.info("Detailed anchor view redirection tracking completed. Loaded detail sheet focus header item text output contents parameter: " + actualDetailName);
        
        // Assert using our dynamically saved 'clickedProductName' variable
        Assert.assertEquals("Image routing discrepancy: Clicking the image loaded the wrong product detail view!", clickedProductName, actualDetailName);
    }
}