package com.demo;

import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestAlert {

	private static WebDriver driver = null;

	@BeforeClass
	public static void setUp() {
		driver = new FirefoxDriver();
		driver.get("http://dl.dropbox.com/u/55228056/Alerts.html");
	}

	@Test
	public void testSimpleAlert() {
		WebElement button = driver.findElement(By.id("simple"));
		button.click();
		try {
			// Manage Alert box
			Alert alert = driver.switchTo().alert();
			String textOnAlert = alert.getText();
			alert.accept();

			// Verify Alert displayed correct message to user
			assertEquals("Hello! I am an alert box!", textOnAlert);
		} catch (NoAlertPresentException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDown() {
		driver.close();
		driver.quit();
	}

}
