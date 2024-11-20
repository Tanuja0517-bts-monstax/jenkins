package tpass;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.Properties;
import java.util.Set;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import WD_JavaUtility.JavaUtility;

public class Creating_Documents_Test {
	public WebDriver driver=null;
	@DataProvider
	public Object[][] getDocData() throws Exception {
		FileInputStream fis=new FileInputStream("./testData/TestData.csv");
		Workbook wb=WorkbookFactory.create(fis);
		int row=wb.getSheet("Doc").getPhysicalNumberOfRows()-1;
		int col=wb.getSheet("Doc").getRow(0).getPhysicalNumberOfCells()-1;
		
		Object obj[][]=new Object[row][col];
		for(int i=1;i<=row;i++) {
			for(int j=0;j<col;j++) {
				obj[(i-1)][j]=wb.getSheet("Doc").getRow(i).getCell(j).toString();
			}
		}
		return obj;
	}
	
	@Test(dataProvider = "getDocData")
	public void creatingDocument_Test(String title,String fld_Name,String Ass_To,String Ass_Team,String dwl_Type,String version) throws Exception {
		FileInputStream fis=new FileInputStream("./testData/ConfigurationFile.properties");
		Properties prop=new Properties();
		prop.load(fis);
		
		JavaUtility ju=new JavaUtility();
		
		String browser=prop.getProperty("browser");
		if(browser.equalsIgnoreCase("chrome"))
			driver=new ChromeDriver();
		else if(browser.equalsIgnoreCase("Firefox"))
			driver=new FirefoxDriver();
		else if(browser.equalsIgnoreCase("edge"))
			driver=new EdgeDriver();
		else
			driver=new ChromeDriver();
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		
		driver.get(prop.getProperty("url"));
		driver.findElement(By.name("user_name")).sendKeys(prop.getProperty("user1"));
		driver.findElement(By.name("user_password")).sendKeys(prop.getProperty("pwd1"));
		driver.findElement(By.id("submitButton")).click();
		
		driver.findElement(By.linkText("Documents")).click();
		driver.findElement(By.xpath("//img[contains(@title,'Create Document')]")).click();
		
		driver.findElement(By.name("notes_title")).sendKeys(title+ju.generateRandomNumber());
		Select sel=new Select(driver.findElement(By.name("folderid")));
		sel.selectByVisibleText("Default");
		
		if(Ass_To.equals("User")) {
			driver.findElement(By.xpath("//input[@value='U']")).click();
			sel=new Select(driver.findElement(By.xpath("//select[@name='assigned_user_id']")));
			sel.selectByVisibleText(" "+Ass_Team);
		}
		else {
			driver.findElement(By.xpath("//input[@value='T']")).click();
			sel=new Select(driver.findElement(By.xpath("//select[@name='assigned_group_id']")));
			sel.selectByVisibleText(Ass_Team);
		}
		
		sel=new Select(driver.findElement(By.xpath("//select[@name='filelocationtype']")));
		sel.selectByVisibleText(dwl_Type);
		
		driver.findElement(By.id("fileversion")).sendKeys(version);
		driver.findElement(By.xpath("//input[contains(@title,'Save')]")).click();
		
		Thread.sleep(3000);
		driver.quit();
	}
}
