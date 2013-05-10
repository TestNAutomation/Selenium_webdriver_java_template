package com.demo;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestTopUpTrueMovePartial {

	WebDriver driver = null;

	@Before
	public void setUp() {
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.get("https://dl.dropboxusercontent.com/u/47150969/site/30.html");
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
	public void topUpFailureWithEmptyInput() {
		WebElement pinTextBox = driver.findElement(By.id("pin"));
		WebElement ref1TextBox = driver.findElement(By.id("ref1"));
		WebElement amountTextBox = driver.findElement(By.id("amount"));
		WebElement amountReceiveTextBox = driver.findElement(By.id("amount_receive"));
		WebElement submitButton = driver.findElement(By.id("btn_submit"));

		pinTextBox.sendKeys("");
		ref1TextBox.sendKeys("");
		amountTextBox.sendKeys("");
		amountReceiveTextBox.sendKeys("");
		submitButton.click();

		Wait<WebDriver> wait = new WebDriverWait(driver, 5);
		WebElement errorForPin = wait.until(visibilityOfElementLocated(By.xpath("//label[@class='error' and @for='pin']")));

		assertEquals("กรุณากรอกข้อมูล", errorForPin.getText());
		assertEquals("กรุณากรอกข้อมูล", driver.findElement(By.xpath("//label[@class='error' and @for='ref1']")).getText());
		assertEquals("กรุณากรอกข้อมูล", driver.findElement(By.xpath("//label[@class='error' and @for='amount']")).getText());
		assertEquals("กรุณากรอกข้อมูล", driver.findElement(By.xpath("//label[@class='error' and @for='amount_receive']")).getText());
	}

	@Test
	public void validateInputOnBlurAction() {
		WebElement pinTextBox = driver.findElement(By.id("pin"));
		WebElement ref1TextBox = driver.findElement(By.id("ref1"));
		WebElement amountTextBox = driver.findElement(By.id("amount"));
		WebElement amountReceiveTextBox = driver.findElement(By.id("amount_receive"));
		WebElement amountChangeTextBox = driver.findElement(By.id("amount_change"));
		WebElement submitButton = driver.findElement(By.id("btn_submit"));

		// Validate pin
		pinTextBox.sendKeys("ใส่รหัสผ่าน");
		ref1TextBox.sendKeys("");

		try {
			Wait<WebDriver> wait = new WebDriverWait(driver, 1);
			wait.until(visibilityOfElementLocated(By.xpath("//label[@class='error' and @for='pin']")));
			fail("Error message for pin was show");
		} catch (Exception e) {
		}

		// Validate ref1
		ref1TextBox.sendKeys("ตัวอักษร");
		amountTextBox.sendKeys("");
		Wait<WebDriver> wait = new WebDriverWait(driver, 1);
		WebElement errorForRef1 = wait.until(visibilityOfElementLocated(By.xpath("//label[@class='error' and @for='ref1']")));
		assertEquals("กรุณากรอกหมายเลขที่ถูกต้อง", errorForRef1.getText());

		ref1TextBox.clear();
		ref1TextBox.sendKeys("123");
		amountTextBox.sendKeys("");
		wait = new WebDriverWait(driver, 1);
		errorForRef1 = wait.until(visibilityOfElementLocated(By.xpath("//label[@class='error' and @for='ref1']")));
		assertEquals("กรุณากรอกอย่างน้อย 10 ตัวอักษร.", errorForRef1.getText());

		ref1TextBox.clear();
		ref1TextBox.sendKeys("1111111111");
		amountTextBox.sendKeys("");
		try {
			wait = new WebDriverWait(driver, 1);
			wait.until(visibilityOfElementLocated(By.xpath("//label[@class='error' and @for='ref1']")));
			fail("Error message for ref1 was showed");
		} catch (Exception e) {
		}

		// Validate amount
		amountTextBox.sendKeys("ตัวอักษร");
		amountReceiveTextBox.sendKeys("");
		wait = new WebDriverWait(driver, 1);
		WebElement errorForAmount = wait.until(visibilityOfElementLocated(By.xpath("//label[@class='error' and @for='amount']")));
		assertEquals("กรุณาป้อนตัวเลขเท่านั้น", errorForAmount.getText());

		amountTextBox.clear();
		amountTextBox.sendKeys("9");
		amountReceiveTextBox.sendKeys("");
		wait = new WebDriverWait(driver, 1);
		errorForAmount = wait.until(visibilityOfElementLocated(By.xpath("//label[@class='error' and @for='amount']")));
		assertEquals("โปรดป้อนค่าระหว่าง 10 ถึง 1000.", errorForAmount.getText());

		amountTextBox.clear();
		amountTextBox.sendKeys("1001");
		amountReceiveTextBox.sendKeys("");
		wait = new WebDriverWait(driver, 1);
		errorForAmount = wait.until(visibilityOfElementLocated(By.xpath("//label[@class='error' and @for='amount']")));
		assertEquals("โปรดป้อนค่าระหว่าง 10 ถึง 1000.", errorForAmount.getText());

		amountTextBox.clear();
		amountTextBox.sendKeys("500");
		amountReceiveTextBox.sendKeys("");
		try {
			wait = new WebDriverWait(driver, 1);
			wait.until(visibilityOfElementLocated(By.xpath("//label[@class='error' and @for='amount']")));
			fail("Error message for amount was showed");
		} catch (Exception e) {
		}

		// Validate amount receive
		Actions buider = new Actions(driver);
		buider.sendKeys("1").click().perform();
		WebElement errorForAmountReceive = driver.findElement(By.xpath("//label[@class='error' and @for='amount_receive']"));
		assertEquals("กรุณาใส่จำนวนเงินให้มากกว่าหรือเท่ากับจำนวนเงินที่ต้องชำระทั้งหมด/ใส่จำนวนเงินจริงตามที่ได้รับ", errorForAmountReceive.getText());
		assertEquals("-499", amountChangeTextBox.getAttribute("value"));

		// 550-500 = 50
		amountReceiveTextBox.clear();
		buider.sendKeys("550").click().perform();
		errorForAmountReceive = driver.findElement(By.xpath("//label[@class='error' and @for='amount_receive']"));
		assertEquals("", errorForAmountReceive.getText());
		assertEquals("50", amountChangeTextBox.getAttribute("value"));

		// 500-500=0
		amountReceiveTextBox.clear();
		buider.sendKeys("500").click().perform();
		errorForAmountReceive = driver.findElement(By.xpath("//label[@class='error' and @for='amount_receive']"));
		assertEquals("", errorForAmountReceive.getText());
		assertEquals("0", amountChangeTextBox.getAttribute("value"));

	}

	@Test
	public void previewTopUpDetailInPopUpPage() {
		WebElement pinTextBox = driver.findElement(By.id("pin"));
		WebElement ref1TextBox = driver.findElement(By.id("ref1"));
		WebElement amountTextBox = driver.findElement(By.id("amount"));
		WebElement amountReceiveTextBox = driver.findElement(By.id("amount_receive"));
		WebElement submitButton = driver.findElement(By.id("btn_submit"));

		pinTextBox.sendKeys("111");
		ref1TextBox.sendKeys("1111111111");
		amountTextBox.sendKeys("500");
		amountReceiveTextBox.sendKeys("500");
		submitButton.click();

		// Verify data in popup page
		WebElement confirmButton = driver.findElement(By.id("btn_confirm"));

		assertEquals("1111111111", driver.findElement(By.id("ref1_show")).getText());
		assertEquals("500.00", driver.findElement(By.id("amount_show")).getText());
		assertEquals("0.00", driver.findElement(By.xpath("//tr/td[@class='font_bill']")).getText());
		assertEquals("500.00", driver.findElement(By.id("total_amount_show")).getText());
		assertEquals("500.00", driver.findElement(By.id("amount_receive_show")).getText());
		assertEquals("0.00", driver.findElement(By.id("amount_change_show")).getText());

		confirmButton.click();

	}

	@After
	public void tearDown() {
		driver.close();
	}

}
