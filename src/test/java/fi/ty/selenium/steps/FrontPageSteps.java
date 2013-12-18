package fi.ty.selenium.steps;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.text.DateFormatter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class FrontPageSteps {
	WebDriver driver;
	String mainWindow;
	

	@Before
	public void beforeScenario() {
		driver = new FirefoxDriver();
		mainWindow = driver.getWindowHandle();
	}

	@After
	public void afterScenario() {
		driver.quit();
	}

	@Given("^the front page is loaded$")
	public void the_front_page_is_loaded() throws Throwable {
		driver.get("http://www.matkahuolto.fi");
	}

	@Given("^the front page has the schedule search$")
	public void the_front_page_has_the_schedule_search() throws Throwable {
		checkNotNull(getScheduleSearchElement(), "Timetable form was not visible in the front page");
	}

	@Given("^the front page has news feed$")
	public void the_front_page_has_news_feed() throws Throwable {
		checkNotNull(getNewsElement(), "The Front page didn't have the news feed as expected.");
	}

	private WebElement getNewsElement() {
		return driver.findElement(By.id("news"));
	}

	@Then("^logo is on top left of the page$")
	public void logo_is_on_top_left_of_the_page() throws Throwable {
		WebElement logoElement = driver.findElement(By.id("logo"));
		assertNotNull("Logo was not found in the page", logoElement);
		
		assertEquals("The y coordinate was", 11, logoElement.getLocation().y);
		assertEquals("The x coordinate was", 126, logoElement.getLocation().x);
	}

	@Then("^(\\d+) most recent news should be on the news feed$")
	public void most_recent_news_should_be_on_the_news_feed(int numberOfNews)
			throws Throwable {
		WebElement listOfNewsElement = getNewsElement().findElement(By.tagName("ul"));
		List<WebElement> newsElementsList = listOfNewsElement.findElements(By.tagName("li"));
		
		assertEquals(6, newsElementsList.size());
		
		DateTimeFormatter formatter = DateTimeFormat.forPattern("d.M.yyyy");
		DateTime lastNewsDate = null;
		for(WebElement newsElement : newsElementsList) {
			DateTime currentNewsDate = formatter.parseDateTime(newsElement.findElement(By.tagName("a")).getText());
			if(lastNewsDate != null) {
				assertTrue("News weren't in inverted chronological order from latest to oldest", currentNewsDate.isBefore(lastNewsDate) || currentNewsDate.isEqual(lastNewsDate));
			}
			lastNewsDate = currentNewsDate;
		}
	}

	@Then("^the contact information for ticket purchase and opening hours should be visible$")
	public void the_contact_information_for_ticket_purchase_and_opening_hours_should_be_visible()
			throws Throwable {
		List<WebElement> ticketInfoBoxs = getinfoBoxElementsWithText("Aikataulut, hinnat, lippu- ja paikkavaraukset");
		assertEquals(
				String.format("The page should have contained one contact information for ticket purchase."),
				ticketInfoBoxs.size(), 1);

		WebElement ticketInfo = ticketInfoBoxs.get(0);

		// get info elements text and assert that those contain info
		String infoText = ticketInfo.findElement(By.tagName("p")).getText();
		assertThat("Expected to contain opening hours", infoText,
				containsString("Ma-pe klo 8-17"));
		assertThat("Expected to contain phone number", infoText,
				containsString("0200 4000"));
	}

	@Then("^the contact information for parcel services and opening hours should be visible$")
	public void the_contact_information_for_parcel_services_and_opening_hours_should_be_visible()
			throws Throwable {
		List<WebElement> parcelInfoBoxs = getinfoBoxElementsWithText("Pakettipalvelujen valtakunnallinen palvelunumero");
		assertEquals(
				String.format("The page should have contained one contact information for parcel services."),
				parcelInfoBoxs.size(), 1);
		
		WebElement ticketInfo = parcelInfoBoxs.get(0);

		// get info elements text and assert that those contain info
		String infoText = ticketInfo.findElement(By.tagName("p")).getText();
		assertThat("Expected to contain opening hours", infoText,
				containsString("Ma-pe klo 8-18"));
		assertThat("Expected to contain phone number", infoText,
				containsString("0800 132 582"));
	}

	@When("^the schedule search is done with a trip from '(\\w+)' to '(\\w+)' and departure date is current date$")
	public void the_schedule_search_is_done_with_a_trip_from_Turku_to_Helsinki_and_departure_date_is_current_date(String from, String to) throws Throwable {
		WebElement scheduleSearch = getScheduleSearchElement();
		
		//set from and to
		scheduleSearch.findElement(By.cssSelector("input[name = 'departureStopAreaName']")).sendKeys(from);
		scheduleSearch.findElement(By.cssSelector("input[name = 'arrivalStopAreaName']")).sendKeys(to);
		
		//set date as a current
		Calendar cal = Calendar.getInstance();
		WebElement departureDay = scheduleSearch.findElement(By.id("departureDay"));
		WebElement departureMonth = scheduleSearch.findElement(By.id("departureMonth"));
		WebElement departureYear = scheduleSearch.findElement(By.id("departureYear"));
		
		departureDay.clear();
		departureDay.sendKeys(cal.get(Calendar.DAY_OF_MONTH) + "");
		
		departureMonth.clear();
		departureMonth.sendKeys(cal.get(Calendar.MONTH) + 1 + "");
		
		departureYear.clear();
		departureYear.sendKeys(cal.get(Calendar.YEAR) + "");
		
		//trigger search and wait the window to be opened a
		scheduleSearch.findElement(By.cssSelector("input[name = 'search']")).click();
		Thread.sleep(2000);
		driver.switchTo().window(getNewWindow());
		waitForElement(By.className("tblContentTable"), 10);
	}

	@Then("^the timetable results should be visible$")
	public void the_timetable_should_be_visible() throws Throwable {
		assertNotNull(driver.findElement(By.className("tblList")));
	}

	private String getNewWindow() {
		for(String windowHandle : driver.getWindowHandles()) {
			if(!windowHandle.equals(mainWindow)) {
				return windowHandle;
			}
		}
		throw new IllegalStateException("No other window that main window was found");
	}

	private WebElement getScheduleSearchElement() {
		return driver.findElement(By.id("scheduleSearch"));
	}

	private List<WebElement> getinfoBoxElementsWithText(String text) {
		List<WebElement> infoBoxs = driver.findElements(By
				.className("infoBoxContent"));
		checkState(!infoBoxs.isEmpty(),
				"There were no contact information boxes in the site");

		List<WebElement> matchedInfoBoxes = Lists.newArrayList();
		for (WebElement infoBox : infoBoxs) {
			if (infoBox.getText().contains(text)) {
				matchedInfoBoxes.add(infoBox);
			}
		}

		return matchedInfoBoxes;
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
	
	private void waitForElement(final By by, int timeoutInSecs) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSecs);
		wait.ignoring(NoSuchElementException.class);
        wait.until(new Predicate<WebDriver>() {
            public boolean apply(WebDriver arg0) {
            	WebElement element = driver.findElement(by);
            	if(element != null) {
            		return driver.findElement(by).isDisplayed();            		
            	}
            	return false;
            }
        });
	}

}
