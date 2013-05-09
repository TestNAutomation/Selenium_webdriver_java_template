package com.tmx;

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

public class TestLogin {
	WebDriver driver = null;
	
	@Before
	public void setUp() {
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.get("https://secure.truemoney-dev.com/tmxonline11-devpoc/home");
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
	public void checkAllElementInPage() {
		assertTrue(driver.findElement(By.id("username")).isDisplayed());
		assertTrue(driver.findElement(By.id("password")).isDisplayed());
		assertTrue(driver.findElement(By.id("captcha_code")).isDisplayed());
		
		assertTrue(driver.findElement(By.linkText("หน้าแรก")).isDisplayed());
		assertTrue(driver.findElement(By.linkText("บริการทรูมันนี่ เอ็กซ์เพรส")).isDisplayed());
		assertTrue(driver.findElement(By.linkText("ข่าว/กิจกรรม")).isDisplayed());
		assertTrue(driver.findElement(By.linkText("ติดต่อเรา")).isDisplayed());
	}
	
	@Test
	public void loginFailureWithInvalidUsernameAndPasswordAndCaptcha() {
		WebElement userName = driver.findElement(By.id("username"));
		WebElement password = driver.findElement(By.id("password"));
		WebElement captchaCode = driver.findElement(By.id("captcha_code"));
		WebElement submitButton = driver.findElement(By.id("wpsubmit"));
		
		userName.sendKeys("test");
		password.sendKeys("test");
		captchaCode.sendKeys("test");
		submitButton.click();
		
		Wait<WebDriver> wait = new WebDriverWait(driver, 10);
		WebElement errorMessage = wait.until(visibilityOfElementLocated(By.xpath("//div[contains(@style,'background-color')]")));
		
		assertTrue(errorMessage.getText().startsWith("กรอกตัวเลขตามภาพไม่ถูกต้อง"))  ;
		assertTrue(errorMessage.getText().contains("error : WLS10008"))  ;
	}
	
	@After
	public void tearDown() {
		driver.close();
	}

}
