package fi.ty.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

/**
 * Unit test for simple App.
 */
public class SeleniumTest {

    private WebDriver driver;
    private static final String baseUrl = "http://www.utu.fi/en/";

    @Before
    public void setUp() {
        System.out.println("creating driver...");
        driver = new FirefoxDriver();
        System.out.println("opening Turku Univesities page...");
        driver.get(baseUrl);
    }

    @Test
    public void testApp() {
        List<WebElement> menuItems = driver.findElements(By
                .cssSelector(".menu-item-text"));
        WebElement studyingMenuItem = null;
        for (WebElement menuItem : menuItems) {
            if (menuItem.getText().equals("Studying")) {
                studyingMenuItem = menuItem;
                break;
            }
        }
        assertNotNull(studyingMenuItem);

        studyingMenuItem.click();

        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(new Predicate<WebDriver>() {
            public boolean apply(WebDriver arg0) {
                return ((JavascriptExecutor) driver).executeScript(
                        "return document.readyState").equals("complete");
            }
        });

        assertEquals(baseUrl + "studying/Pages/home.aspx",
                driver.getCurrentUrl());
    }

    @After
    public void tearDown() {
        System.out.println("Closing driver...");
        driver.quit();
    }
}
