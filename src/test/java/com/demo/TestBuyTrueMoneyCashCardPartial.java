package com.demo;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestBuyTrueMoneyCashCardPartial {

	WebDriver driver = null;

	@Before
	public void setUp() {
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.get("https://dl.dropboxusercontent.com/u/47150969/site/23.html");
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
	public void buyFailureWithEmptyInput() {
		WebElement pinTextBox = driver.findElement(By.id("pin"));
		WebElement amountReceiveTextBox = driver.findElement(By.id("amount_receive"));
		WebElement submitButton = driver.findElement(By.id("btn_submit"));

		pinTextBox.sendKeys("");
		amountReceiveTextBox.sendKeys("");
		submitButton.click();

		Wait<WebDriver> wait = new WebDriverWait(driver, 5);
		WebElement errorForPin = wait.until(visibilityOfElementLocated(By.xpath("//label[@class='error' and @for='pin']")));

		assertEquals("กรุณากรอกข้อมูล", errorForPin.getText());
		assertEquals("กรุณาเลือกจำนวนเงิน", driver.findElement(By.xpath("//label[@class='error' and @for='amount']")).getText());
		assertEquals("กรุณากรอกข้อมูล", driver.findElement(By.xpath("//label[@class='error' and @for='amount_receive']")).getText());
	}

	@Test
	public void buyFailureWithPinAndAmount() {
		WebElement pinTextBox = driver.findElement(By.id("pin"));
		WebElement amountReceiveTextBox = driver.findElement(By.id("amount_receive"));
		WebElement submitButton = driver.findElement(By.id("btn_submit"));

		List<WebElement> amountReceiveRadioList = driver.findElements(By.id("amount"));
		for (WebElement webElement : amountReceiveRadioList) {
			if (webElement.getAttribute("value").equals("150")) {
				if (!webElement.isSelected()) {
					webElement.click();
				}
				assertTrue(webElement.isSelected());
				break;
			}
		}

		pinTextBox.sendKeys("123");
		amountReceiveTextBox.sendKeys("");
		submitButton.click();

		Wait<WebDriver> wait = new WebDriverWait(driver, 5);
		WebElement errorForAmountReceive = wait.until(visibilityOfElementLocated(By.xpath("//label[@class='error' and @for='amount_receive']")));

		try {
			driver.findElement(By.xpath("//label[@class='error' and @for='pin']"));
			fail("Error message for pin was show");
		} catch (Exception e) {
		}
		WebElement amountErrorElement = driver.findElement(By.xpath("//label[@class='error' and @for='amount']"));
		assertEquals("display: none;", amountErrorElement.getAttribute("style"));
		assertEquals("กรุณากรอกข้อมูล", errorForAmountReceive.getText());
	}
	
	@Test
	public void buyFailureWithAmountReceiveLessThanAmount() {
		WebElement pinTextBox = driver.findElement(By.id("pin"));
		WebElement amountReceiveTextBox = driver.findElement(By.id("amount_receive"));
		WebElement amountChangeTextBox = driver.findElement(By.id("amount_change"));

		pinTextBox.sendKeys("123");
		List<WebElement> amountReceiveRadioList = driver.findElements(By.id("amount"));
		for (WebElement webElement : amountReceiveRadioList) {
			if (webElement.getAttribute("value").equals("150")) {
				if (!webElement.isSelected()) {
					webElement.click();
				}
				assertTrue(webElement.isSelected());
				break;
			}
		}		
		amountReceiveTextBox.sendKeys("");
		Actions buider = new Actions(driver);
		buider.sendKeys("50").click().perform();
		WebElement errorForAmountReceive = driver.findElement(By.xpath("//label[@class='error' and @for='amount_receive']"));
		assertTrue(errorForAmountReceive.getText().contains("กรุณาใส่จำนวนเงินให้มากกว่าหรือเท่ากับจำนวนเงินที่ต้องชำระทั้งหมด/ใส่จำนวนเงินจริงตามที่ได้รับ"));
		assertEquals("-100", amountChangeTextBox.getAttribute("value"));
	}
	
	@Test
	public void buySuccessWithAmountReceiveMoreThanAmount() {
		WebElement pinTextBox = driver.findElement(By.id("pin"));
		WebElement amountReceiveTextBox = driver.findElement(By.id("amount_receive"));
		WebElement amountChangeTextBox = driver.findElement(By.id("amount_change"));
		WebElement submitButton = driver.findElement(By.id("btn_submit"));

		pinTextBox.sendKeys("123");
		List<WebElement> amountReceiveRadioList = driver.findElements(By.id("amount"));
		for (WebElement webElement : amountReceiveRadioList) {
			if (webElement.getAttribute("value").equals("150")) {
				if (!webElement.isSelected()) {
					webElement.click();
				}
				assertTrue(webElement.isSelected());
				break;
			}
		}		
		amountReceiveTextBox.sendKeys("");
		Actions buider = new Actions(driver);
		buider.sendKeys("200").click().perform();
		
		try {
			driver.findElement(By.xpath("//label[@class='error' and @for='amount_receive']"));
			fail("Error message for amount_receive was show");
		} catch (Exception e) {
		}
		assertEquals("50", amountChangeTextBox.getAttribute("value"));
		
		//Submit form
		submitButton.click();
		// Verify data in popup page
		WebElement confirmButton = driver.findElement(By.id("btn_confirm"));

		assertTrue(driver.findElement(By.tagName("legend")).getText().startsWith("ซื้อรหัสบัตรเงินสด :"));
		assertEquals("150.00", driver.findElement(By.id("amount_show")).getText());
		assertEquals("0.00", driver.findElement(By.xpath("//tr/td[@class='font_bill']")).getText());
		assertEquals("150.00", driver.findElement(By.id("total_amount_show")).getText());
		assertEquals("200.00", driver.findElement(By.id("amount_receive_show")).getText());
		assertEquals("50.00", driver.findElement(By.id("amount_change_show")).getText());
		
		confirmButton.click();
	}

	@After
	public void tearDown() {
		 driver.close();
	}

}
