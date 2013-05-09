package com.tmx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.tmx.constant.TMXConstant;

public class TestRegister {

	WebDriver driver = null;

	@Before
	public void setUp() {
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.get(TMXConstant.TMX_HOME_URL);
	}

	private ExpectedCondition<WebElement> visibilityOfElementLocated(final By by) {
		return new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				WebElement element = driver.findElement(by);
				return element.isDisplayed() ? element : null;
			}
		};
	}

	@Test
	public void registerFailureWithWorngRegisterCode() {
		WebElement registerButton = driver.findElement(By.tagName("button"));
		registerButton.click();

		// Waiting for id=btn_accept
		Wait<WebDriver> wait = new WebDriverWait(driver, 10);
		WebElement acceptButton = wait.until(visibilityOfElementLocated(By.id("btn_accept")));

		// Check web element in page
		WebElement acceptCheckbox = driver.findElement(By.id("accept_status"));
		WebElement rejectButton = driver.findElement(By.id("btn_denied"));

		// Verify checkbox
		assertFalse(acceptCheckbox.isSelected());

		// Click accept policy box
		acceptCheckbox.click();
		// Verify Checkbox is Selected
		assertTrue(acceptCheckbox.isSelected());

		// Click accept button
		acceptButton.click();

		// Waiting for id=btn_accept
		wait = new WebDriverWait(driver, 10);
		WebElement element = wait.until(visibilityOfElementLocated(By.tagName("legend")));

		WebElement codeTextbox = driver.findElement(By.id("code"));
		WebElement checkButton = driver.findElement(By.id("jq_check"));
		WebElement cancelButton = driver.findElement(By.id("jq_cancel"));

		codeTextbox.sendKeys("xxxxx");
		checkButton.click();

		// Waiting for id=btn_accept
		wait = new WebDriverWait(driver, 5);
		WebElement warningMessage = wait.until(visibilityOfElementLocated(By.id("warning_lable")));

		assertEquals("ไม่พบหมายเลย Register Code กรุณาตรวจสอบใหม่อีกครั้ง", warningMessage.getText());

	}
	
	@Test
	public void registerCancelInPageFillInCode() {
		WebElement registerButton = driver.findElement(By.tagName("button"));
		registerButton.click();

		// Waiting for id=btn_accept
		Wait<WebDriver> wait = new WebDriverWait(driver, 10);
		WebElement acceptButton = wait.until(visibilityOfElementLocated(By.id("btn_accept")));

		// Check web element in page
		WebElement acceptCheckbox = driver.findElement(By.id("accept_status"));
		WebElement rejectButton = driver.findElement(By.id("btn_denied"));

		// Verify checkbox
		assertFalse(acceptCheckbox.isSelected());

		// Click accept policy box
		acceptCheckbox.click();
		// Verify Checkbox is Selected
		assertTrue(acceptCheckbox.isSelected());

		// Click accept button
		acceptButton.click();

		// Waiting for id=btn_accept
		wait = new WebDriverWait(driver, 10);
		WebElement element = wait.until(visibilityOfElementLocated(By.tagName("legend")));

		WebElement codeTextbox = driver.findElement(By.id("code"));
		WebElement checkButton = driver.findElement(By.id("jq_check"));
		WebElement cancelButton = driver.findElement(By.id("jq_cancel"));

		codeTextbox.sendKeys("ยกเลิกนะ");
		cancelButton.click();

		assertEquals(TMXConstant.TMX_INDEX_URL, driver.getCurrentUrl()) ;
	}

	@After
	public void tearDown() {
		driver.close();
	}

}
