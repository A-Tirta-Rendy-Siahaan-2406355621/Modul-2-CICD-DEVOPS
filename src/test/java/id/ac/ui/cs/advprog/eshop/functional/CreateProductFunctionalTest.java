package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createProduct_shouldAppearInProductList(ChromeDriver driver) {

        String productName = "Sampo Selenium " + System.currentTimeMillis();
        String productQty = "12";

        String[] createUrls = new String[]{
                baseUrl + "/product/create",
                baseUrl + "/create-product",
                baseUrl + "/product/add",
                baseUrl + "/create"
        };
        openFirstWorkingUrl(driver, createUrls);

        WebElement nameInput = findFirst(driver,
                By.name("productName"),
                By.id("productName"),
                By.cssSelector("input[name='productName']"),
                By.cssSelector("input[id='productName']")
        );

        WebElement qtyInput = findFirst(driver,
                By.name("productQuantity"),
                By.id("productQuantity"),
                By.cssSelector("input[name='productQuantity']"),
                By.cssSelector("input[id='productQuantity']"),
                By.cssSelector("input[type='number']")
        );

        nameInput.clear();
        nameInput.sendKeys(productName);

        qtyInput.clear();
        qtyInput.sendKeys(productQty);

        WebElement submitButton = findFirst(driver,
                By.cssSelector("button[type='submit']"),
                By.cssSelector("input[type='submit']"),
                By.xpath("//button[contains(.,'Create') or contains(.,'Add') or contains(.,'Submit')]"),
                By.xpath("//input[@type='submit']")
        );
        submitButton.click();

        boolean foundOnCurrentPage = pageContains(driver, productName);

        if (!foundOnCurrentPage) {
            String[] listUrls = new String[]{
                    baseUrl + "/product/list",
                    baseUrl + "/product",
                    baseUrl + "/products",
                    baseUrl + "/"
            };
            openFirstWorkingUrl(driver, listUrls);
        }

        assertTrue(pageContains(driver, productName),
                "New product name should appear in product list/page source. " +
                        "If this fails, adjust create/list URL or the selectors to match your HTML.");
    }

    private static void openFirstWorkingUrl(ChromeDriver driver, String[] urls) {
        RuntimeException lastError = null;
        for (String url : urls) {
            try {
                driver.get(url);

                if (!pageLooksLike404(driver)) return;
            } catch (RuntimeException e) {
                lastError = e;
            }
        }
        throw new IllegalStateException("Could not open any expected URL. Update the URL list to match your routes.", lastError);
    }

    private static boolean pageLooksLike404(ChromeDriver driver) {
        String src = driver.getPageSource();
        String title = "";
        try {
            title = driver.getTitle();
        } catch (Exception ignored) {
        }
        String hay = (title + "\n" + src).toLowerCase(Locale.ROOT);
        return hay.contains("whitelabel error page") || hay.contains("404") || hay.contains("not found");
    }

    private static boolean pageContains(ChromeDriver driver, String text) {
        return driver.getPageSource() != null && driver.getPageSource().contains(text);
    }

    private static WebElement findFirst(ChromeDriver driver, By... locators) {
        for (By by : locators) {
            try {
                WebElement el = driver.findElement(by);
                if (el != null) return el;
            } catch (NoSuchElementException ignored) {
            }
        }
        throw new NoSuchElementException("Could not find element with provided locators. Adjust selectors to match your HTML.");
    }
}
