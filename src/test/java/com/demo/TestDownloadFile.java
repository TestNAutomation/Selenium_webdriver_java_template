package com.demo;

import static org.junit.Assert.*;
import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestDownloadFile {
	
	WebDriver driver = null;
	
	@Before
	public void setup() {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference( "browser.helperApps.neverAsk.saveToDisk", "text/plain,application/pdf,application/zip,text/csv" );
		profile.setPreference( "browser.download.dir",  "C:\\download\\");
		profile.setPreference( "browser.download.lastDir" , "C:\\download\\");
		profile.setPreference( "browser.download.folderList", 2);
		
		driver = new FirefoxDriver(profile);
		driver.get("http://maven.apache.org/");
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
	public void testDownload() {
		WebElement downloadLink = driver.findElement(By.linkText("Download"));
		downloadLink.click();
		
		Wait<WebDriver> wait = new WebDriverWait(driver, 5);
		WebElement downloadMavenLink = wait.until(visibilityOfElementLocated(By.linkText("apache-maven-3.0.5-bin.zip")));

		downloadMavenLink.click();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Waiting to download file");
		File file = new File("C:\\download\\apache-maven-3.0.5-bin.zip.part");
		while( file.exists() ) {			
		}
		
		assertFalse(file.exists());		
	}
	
	@After
	public void tearDown() {
		driver.quit();
	}

}
