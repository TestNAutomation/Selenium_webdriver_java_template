package com.tmx;

import static org.junit.Assert.*;

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

public class TestLogin {
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
	public void checkAllElementInPage() {
		assertTrue(driver.findElement(By.id("username")).isDisplayed());
		assertTrue(driver.findElement(By.id("password")).isDisplayed());
		assertTrue(driver.findElement(By.id("captcha_code")).isDisplayed());

		assertEquals(TMXConstant.TMX_HOME_URL, driver.findElement(By.linkText("หน้าแรก")).getAttribute("href"));
		assertEquals(TMXConstant.TMX_ABOUT_URL, driver.findElement(By.linkText("บริการทรูมันนี่ เอ็กซ์เพรส")).getAttribute("href"));
		assertEquals(TMXConstant.TMX_NEWS_URL, driver.findElement(By.linkText("ข่าว/กิจกรรม")).getAttribute("href"));
		assertEquals(TMXConstant.TMX_CONTACT_URL, driver.findElement(By.linkText("ติดต่อเรา")).getAttribute("href"));
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

		assertTrue(errorMessage.getText().startsWith("กรอกตัวเลขตามภาพไม่ถูกต้อง"));
		assertTrue(errorMessage.getText().contains("error : WLS10008"));
	}

	@Test
	public void loginFailureWithWrongPassword() {

	}

	@Test
	public void loginSuccess() {
		WebElement userName = driver.findElement(By.id("username"));
		WebElement password = driver.findElement(By.id("password"));
		WebElement captchaCode = driver.findElement(By.id("captcha_code"));
		WebElement submitButton = driver.findElement(By.id("wpsubmit"));

		userName.sendKeys("9000042052");
		password.sendKeys("test1234");
		captchaCode.sendKeys("test");
		submitButton.click();

		Wait<WebDriver> wait = new WebDriverWait(driver, 10);
		WebElement logoutLink = wait.until(visibilityOfElementLocated(By.linkText("ออกจากระบบ ")));

		WebElement changePasswordLink = driver.findElement(By.linkText("เปลี่ยนรหัสผ่าน"));
		WebElement channelLink = driver.findElement(By.linkText("ช่องทางการติดต่อ"));
		WebElement profileLink = driver.findElement(By.linkText("ข้อมูลส่วนตัว"));
		WebElement printerLink = driver.findElement(By.linkText("ขนาดกระดาษเครื่องพิมพ์"));

		assertTrue(driver.getPageSource().contains("9000042052"));
		assertEquals(TMXConstant.TMX_LOGOUT_URL, logoutLink.getAttribute("href"));
		assertEquals(TMXConstant.TMX_CHANGE_PASSWORD_URL, changePasswordLink.getAttribute("href"));
		assertEquals(TMXConstant.TMX_CHANNEL_URL, channelLink.getAttribute("href"));
		assertEquals(TMXConstant.TMX_PROFILE_URL, profileLink.getAttribute("href"));
		assertEquals(TMXConstant.TMX_PRINTER_URL, printerLink.getAttribute("href"));

		WebElement quickPayLink = driver.findElement(By.linkText("จ่ายบิลด้วยบาร์โค้ด"));
		WebElement topUpLink = driver.findElement(By.linkText("เติม"));
		WebElement billPayLink = driver.findElement(By.linkText("จ่าย"));
		WebElement buyLink = driver.findElement(By.linkText("ซื้อ"));
		WebElement bookingLink = driver.findElement(By.linkText("จอง-จ่าย"));
		WebElement reportDailyLink = driver.findElement(By.linkText("รายงานการทำรายการ"));
		WebElement reportCancelLink = driver.findElement(By.linkText("รายงานยกเลิกทำรายการ"));
		WebElement checkBalanceLink = driver.findElement(By.linkText("ตรวจสอบยอดเงิน"));
		WebElement FundInBankLink = driver.findElement(By.linkText("เติมเงินด้วยบัญชีธนาคาร"));

		assertEquals(TMXConstant.TMX_QUICK_PAY_URL, quickPayLink.getAttribute("href"));
		assertEquals(TMXConstant.TMX_TOPUP_URL, topUpLink.getAttribute("href"));
		assertEquals(TMXConstant.TMX_BILL_PAY_URL, billPayLink.getAttribute("href"));
		assertEquals(TMXConstant.TMX_BUY_URL, buyLink.getAttribute("href"));
		assertEquals(TMXConstant.TMX_BOOKING_URL, bookingLink.getAttribute("href"));
		assertEquals(TMXConstant.TMX_REPORT_DAILY_URL, reportDailyLink.getAttribute("href"));
		assertEquals(TMXConstant.TMX_REPORT_CANCEL_URL, reportCancelLink.getAttribute("href"));
		assertEquals(TMXConstant.TMX_CHECK_BALANCE_URL, checkBalanceLink.getAttribute("href"));
		assertEquals(TMXConstant.TMX_FUND_IN_BANK_URL, FundInBankLink.getAttribute("href"));
	}

	@After
	public void tearDown() {
		driver.close();
	}

}
