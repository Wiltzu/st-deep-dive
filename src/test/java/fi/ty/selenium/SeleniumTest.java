package fi.ty.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.client.utils.URIBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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

        waitForPage();


        assertEquals(baseUrl + "studying/Pages/home.aspx",
                driver.getCurrentUrl());
    }

	private void waitForPage() {
		WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(new Predicate<WebDriver>() {
            public boolean apply(WebDriver arg0) {
                return ((JavascriptExecutor) driver).executeScript(
                        "return document.readyState").equals("complete");
            }
        });
	}

    @Test
    public void testSearch() {
    	final String searchWord = "Studying";
        WebElement textField = driver.findElement(By.id("ctl00_PlaceHolderSearchArea_mossSearch_S2768ABB1_InputKeywords"));
        textField.click();
		textField.sendKeys(searchWord);
        driver.findElement(By.id("ctl00_PlaceHolderSearchArea_mossSearch_S2768ABB1_go")).click();
        sleep();
        waitForPage();
        try {
			assertEquals(baseUrl + "search/Pages/results.aspx?k=" + URLEncoder.encode(searchWord, "UTF-8") , driver.getCurrentUrl());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    private void sleep() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@After
    public void tearDown() {
        System.out.println("Closing driver...");
        driver.quit();
    }
}
