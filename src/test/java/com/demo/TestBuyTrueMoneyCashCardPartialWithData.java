package com.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

@RunWith(value = Parameterized.class)
public class TestBuyTrueMoneyCashCardPartialWithData {

	WebDriver driver = null;

	private String pin;
	private String amountReceiveSelected;
	private String amountReceive;
	private String fee;
	private String totalAmount;
	private String amountChange;

	public TestBuyTrueMoneyCashCardPartialWithData(String pin, String amountReceiveSelected, String amountReceive, String fee, String totalAmount, String amountChange) {
		this.pin = pin;
		this.amountReceiveSelected = amountReceiveSelected;
		this.amountReceive = amountReceive;
		this.fee = fee;
		this.totalAmount = totalAmount;
		this.amountChange = amountChange;
	}

	@Parameters
	public static Collection testData() throws Exception {
//		return getData();
		 return
		 getDataFromCSV("D:\\Somkiat\\training\\selinium\\workspace\\Selenium_webdriver_java_template\\data_test\\data_test.csv");
		// return
	}

	private static Collection getData() {
		return Arrays.asList(new Object[][] { { "123", "150", "200", "0", "150", "50" }, { "123", "50", "500", "0", "50", "450" }, { "123", "90", "90", "0", "90", "0" },
		// { "123", "100", "100", "0", "100", "0" },
				});
	}

	private static Collection<String[]> getDataFromCSV(String fileName) throws IOException {
		List<String[]> inputList = new ArrayList<String[]>();
		String row;

		BufferedReader file = new BufferedReader(new FileReader(fileName));
		while ((row = file.readLine()) != null) {
			String fields[] = row.split(",");
			inputList.add(fields);
		}
		file.close();
		return inputList;
	}

	@Before
	public void setUp() {
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.get("https://dl.dropboxusercontent.com/u/47150969/site/23.html");
	}

	@Test
	public void buySuccessWithAmountReceiveEqualOrMoreThanAmount() {
		WebElement pinTextBox = driver.findElement(By.id("pin"));
		WebElement amountReceiveTextBox = driver.findElement(By.id("amount_receive"));
		WebElement amountChangeTextBox = driver.findElement(By.id("amount_change"));
		WebElement submitButton = driver.findElement(By.id("btn_submit"));

		pinTextBox.sendKeys(this.amountReceive);
		List<WebElement> amountReceiveRadioList = driver.findElements(By.id("amount"));
		boolean selectedFlag = false;
		for (WebElement webElement : amountReceiveRadioList) {
			if (webElement.getAttribute("value").equals(this.amountReceiveSelected)) {
				if (!webElement.isSelected()) {
					webElement.click();
				}
				assertTrue(webElement.isSelected());
				selectedFlag = true;
				break;
			}
		}
		if (!selectedFlag) {
			fail("Error :: can not select amount receive with = " + this.amountReceiveSelected);
		}
		amountReceiveTextBox.sendKeys("");
		Actions buider = new Actions(driver);
		buider.sendKeys(this.amountReceive).click().perform();

		try {
			driver.findElement(By.xpath("//label[@class='error' and @for='amount_receive']"));
			fail("Error message for amount_receive was show");
		} catch (Exception e) {
		}
		assertEquals(this.amountChange, amountChangeTextBox.getAttribute("value"));

		// Submit form
		submitButton.click();
		// Verify data in popup page
		WebElement confirmButton = driver.findElement(By.id("btn_confirm"));

		assertTrue(driver.findElement(By.tagName("legend")).getText().startsWith("ซื้อรหัสบัตรเงินสด :"));
		assertEquals(formatNumber(this.amountReceiveSelected), driver.findElement(By.id("amount_show")).getText());
		assertEquals(formatNumber(this.fee), driver.findElement(By.xpath("//tr/td[@class='font_bill']")).getText());
		assertEquals(formatNumber(this.totalAmount), driver.findElement(By.id("total_amount_show")).getText());
		assertEquals(formatNumber(this.amountReceive), driver.findElement(By.id("amount_receive_show")).getText());
		assertEquals(formatNumber(this.amountChange), driver.findElement(By.id("amount_change_show")).getText());

		confirmButton.click();
	}

	private static String formatNumber(String input) {
		DecimalFormat format = new DecimalFormat("###.##");
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(2);
		return format.format(Double.parseDouble(input));
	}

	@After
	public void tearDown() {
		driver.close();
	}

}
