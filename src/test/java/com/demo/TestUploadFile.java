package com.demo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestUploadFile {
	WebDriver driver = null;

	@Before
	public void setup() {
		driver = new FirefoxDriver();
		driver.get("https://dl.dropboxusercontent.com/u/47150969/site/test_upload.html");
	}

	@Test
	public void testDownload() {
		WebElement fileUpload = driver.findElement(By.id("file"));
		fileUpload.sendKeys("C:\\download\\data.txt");
		driver.findElement(By.name("submit")).click();
	}

	@After
	public void tearDown() {
		driver.quit();
	}
}
