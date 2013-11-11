package fi.ty.selenium;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Unit test for simple App.
 */
public class SeleniumTest {

	private WebDriver driver;
	
	@Before
	public void setUp() {
		System.out.println("creating driver...");
		driver = new FirefoxDriver();
	}

    @Test
    public void testApp() {
    	System.out.println("opening google...");
    	driver.get("www.google.fi");
        assertTrue( true );
    }
    
     @After
     public void tearDown() {
    	 System.out.println("Closing driver...");
    	 driver.quit();
     }
}
