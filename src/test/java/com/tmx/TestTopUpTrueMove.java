package com.tmx;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.tmx.constant.TMXConstant;

public class TestTopUpTrueMove {
	
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
	
	private void login() {
		WebElement userName = driver.findElement(By.id("username"));
		WebElement password = driver.findElement(By.id("password"));
		WebElement captchaCode = driver.findElement(By.id("captcha_code"));
		WebElement submitButton = driver.findElement(By.id("wpsubmit"));

		userName.sendKeys("9000042052");
		password.sendKeys("test1234");
		captchaCode.sendKeys("test");
		submitButton.click();
	}
	
	@Test
	public void topUpFailureWithEmptyInput() {
		login();
		
		Wait<WebDriver> wait = new WebDriverWait(driver, 10);
		WebElement topUpLink = wait.until(visibilityOfElementLocated(By.linkText("เติม")));		
		topUpLink.click();
		
		wait = new WebDriverWait(driver, 5);
		WebElement trueMoveLink = wait.until(visibilityOfElementLocated(By.xpath("//tr/td/a[contains(@href, 'products/form/30')]")));
		trueMoveLink.click();		
		assertTrue(driver.getPageSource().contains("ทรูมูฟระบบเติมเงิน"));
		
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
		
		wait = new WebDriverWait(driver, 5);
		WebElement errorForPin = wait.until(visibilityOfElementLocated(By.xpath("//label[@class='error' and @for='pin']")));
		
		assertEquals("กรุณากรอกข้อมูล", errorForPin.getText());
		assertEquals("กรุณากรอกข้อมูล", driver.findElement(By.xpath("//label[@class='error' and @for='ref1']")));
		assertEquals("กรุณากรอกข้อมูล", driver.findElement(By.xpath("//label[@class='error' and @for='amount']")));
		assertEquals("กรุณากรอกข้อมูล", driver.findElement(By.xpath("//label[@class='error' and @for='amount_receive']")));		
	}
	
	@Test
	public void validateInputOnBlurAction() {
		//TODO
	}
	
	@After
	public void tearDown() {
		driver.close();
	}

}
