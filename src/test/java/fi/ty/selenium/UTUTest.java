package fi.ty.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

/**
 * Unit test for simple App.
 */
public class UTUTest {

    private WebDriver driver;
    private static final String baseUrl = "http://www.utu.fi/en/";

    @Before
    public void setUp() {
        System.out.println("creating driver...");
        driver = new FirefoxDriver();
        driver.manage().window().setSize(new Dimension(1024, 768));
        System.out.println("opening Turku Univesities page...");
        driver.get(baseUrl);
    }

    @Test
    public void testStudyingTab() {
    	//get all menu items with class
        List<WebElement> menuItems = driver.findElements(By
                .cssSelector(".menu-item-text"));
        
        WebElement studyingMenuItem = null;
        for (WebElement menuItem : menuItems) {
            //find the element with text "Studying"
        	if (menuItem.getText().equals("Studying")) {
                studyingMenuItem = menuItem;
                break;
            }
        }
        
        //assert element is not null and click it
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
        WebElement searchField = driver.findElement(By.id("ctl00_PlaceHolderSearchArea_mossSearch_S2768ABB1_InputKeywords"));
        
        searchField.click();
		searchField.sendKeys(searchWord);
		
		//submit and wait
        driver.findElement(By.id("ctl00_PlaceHolderSearchArea_mossSearch_S2768ABB1_go")).click();
        waitForPage();
        
        try {
        	//assert that url is the english URL
			assertEquals(baseUrl + "search/Pages/results.aspx?k=" + URLEncoder.encode(searchWord, "UTF-8") , driver.getCurrentUrl());
			//ignore the encoding exception
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

    }
    
    @Test
    public void testUTUShopLink() {
    	final String utuShopURL = "https://utushop.utu.fi/";
		WebElement utuShopLink = driver.findElement(By.cssSelector(String.format("a[href *= '%s']", utuShopURL)));
		
    	utuShopLink.click();
    	
    	waitForPage();
    	
    	//correct page is opened
    	assertEquals(utuShopURL, driver.getCurrentUrl());
    		
    	WebElement productsElement = driver.findElement(By.className("section-header"));
    	//Page is in english
    	assertEquals("PRODUCTS", productsElement.getText());
    }

	@After
    public void tearDown() {
        System.out.println("Closing driver...");
        driver.quit();
    }
}
