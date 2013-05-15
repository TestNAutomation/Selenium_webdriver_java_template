package com.demo;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class TestSeleniumGrid {

	private WebDriver driver = null;

	@Before
	public void setUp() throws MalformedURLException {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps = DesiredCapabilities.firefox();
		driver = new RemoteWebDriver(new URL("http://192.168.1.44:4444/wd/hub"), caps);
		driver.get("http://www.google.com");
	}
	
	@Test
	public void testGoogle() {
		
	}

	@After
	public void tearDown() {
		driver.quit();
	}

}
