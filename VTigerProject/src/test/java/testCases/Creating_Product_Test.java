package testCases;

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

public class Creating_Product_Test {
	WebDriver driver=null;
	
	@DataProvider
	public Object[][]  dataFromCreatingProduct() throws Exception {
		FileInputStream fis=new FileInputStream("./testData/TestData.csv");
		Workbook wb=WorkbookFactory.create(fis);
		int row=wb.getSheet("Sheet1").getPhysicalNumberOfRows()-1;
		int col=wb.getSheet("Sheet1").getRow(0).getPhysicalNumberOfCells()-1;
		System.out.println(row+" "+col);
		Object obj[][]=new Object[row][col];
		
		for(int i=1;i<=row;i++) {
			for(int j=0;j<col;j++) {
				obj[(i-1)][j]=wb.getSheet("Sheet1").getRow(i).getCell(j).toString();
			}
		}
		return obj;
	}
	
//	@Test http://localhost:8888/index.php
//	public void loginToApplication_Test() throws Exception {
//		FileInputStream fis=new FileInputStream("./testData/ConfigurationFile.properties");
//		Properties prop=new Properties();
//		prop.load(fis);
//		
//		if(prop.getProperty("browser").equalsIgnoreCase("chrome"))
//			driver=new ChromeDriver();
//		else if(prop.getProperty("browser").equalsIgnoreCase("firefox"))
//			driver=new FirefoxDriver();
//		else if(prop.getProperty("browser").equalsIgnoreCase("edge"))
//			driver=new EdgeDriver();
//		else
//			driver=new ChromeDriver();
//		
//		driver.manage().window().maximize();
//		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//		
//		driver.get(prop.getProperty("url"));
//		driver.findElement(By.name("user_name")).sendKeys(prop.getProperty("user1"));
//		driver.findElement(By.name("user_password")).sendKeys(prop.getProperty("pwd1"));
//		driver.findElement(By.id("submitButton")).click();
//	, dependsOnMethods = "loginToApplication_Test"}
	
	@Test(dataProvider = "dataFromCreatingProduct")
	public void createProduct_Test(String p_Name,String s_Date,String part_No,String p_Cat,String title,String assigned_To,String Cont_Org,String conOrg_Name, String u_g, String status,String priority,String severity) throws Exception {
		FileInputStream fis=new FileInputStream("./testData/ConfigurationFile.properties");
		Properties prop=new Properties();
		prop.load(fis);
		
		if(prop.getProperty("browser").equalsIgnoreCase("chrome"))
			driver=new ChromeDriver();
		else if(prop.getProperty("browser").equalsIgnoreCase("firefox"))
			driver=new FirefoxDriver();
		else if(prop.getProperty("browser").equalsIgnoreCase("edge"))
			driver=new EdgeDriver();
		else
			driver=new ChromeDriver();
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		driver.get(prop.getProperty("url"));
		driver.findElement(By.name("user_name")).sendKeys(prop.getProperty("user1"));
		driver.findElement(By.name("user_password")).sendKeys(prop.getProperty("pwd1"));
		driver.findElement(By.id("submitButton")).click();
		//Creating product
		JavaUtility ju=new JavaUtility();
		driver.findElement(By.linkText("Products")).click();
		driver.findElement(By.xpath("//img[@src='themes/softed/images/btnL3Add.gif']")).click();
		driver.findElement(By.name("productname")).sendKeys(p_Name+ju.generateRandomNumber());
		driver.findElement(By.id("jscal_field_sales_start_date")).sendKeys(s_Date);
		driver.findElement(By.id("productcode")).sendKeys(part_No);
		Select sel=new Select(driver.findElement(By.name("productcategory")));
		sel.selectByVisibleText(p_Cat);
		driver.findElement(By.xpath("//input[contains(@title,'Save')]")).click();
		
		String parent=driver.getWindowHandle();
		
		//Creating Trouble Tickets
		driver.findElement(By.linkText("Trouble Tickets")).click();
		driver.findElement(By.xpath("//img[contains(@title,'Create Ticket')]")).click();
		driver.findElement(By.name("ticket_title")).sendKeys(title+ju.generateRandomNumber());
		sel=new Select(driver.findElement(By.name("parent_type")));
		sel.selectByVisibleText(Cont_Org);
		driver.findElement(By.xpath("//input[@id='parentid']/following-sibling::img[@alt='Select']")).click();
		Set<String> handles=driver.getWindowHandles();
		for(String handle:handles) {
			if(!handle.equals(parent))
				driver.switchTo().window(handle);
		}
		driver.findElement(By.linkText(conOrg_Name)).click();
		driver.switchTo().window(parent);
		driver.findElement(By.xpath("//input[@name='product_name']/following-sibling::img[@alt='Select']")).click();
		
		handles=driver.getWindowHandles();
		for(String handle:handles) {
			if(!handle.equals(parent))
				driver.switchTo().window(handle);
		}
		driver.findElement(By.linkText(p_Name)).click();
		driver.switchTo().window(parent);
		if(u_g.equals("User")) {
			driver.findElement(By.xpath("//input[@value='U']")).click();
			sel=new Select(driver.findElement(By.xpath("//span[@id='assign_user']//select[@name='assigned_user_id']")));
			sel.selectByVisibleText(assigned_To);
		}
		else if(u_g.equals("Group")) {
			driver.findElement(By.xpath("//input[@value='T']")).click();
			sel=new Select(driver.findElement(By.xpath("//span[@id='assign_team']//select[@name='assigned_group_id']")));
			sel.selectByVisibleText(assigned_To);
		}
		
		sel=new Select(driver.findElement(By.name("ticketpriorities")));
		sel.selectByVisibleText(priority);
		
		sel=new Select(driver.findElement(By.name("ticketseverities")));
		sel.selectByVisibleText(severity);
		
		sel=new Select(driver.findElement(By.name("ticketstatus")));
		sel.selectByVisibleText(status);
		
		driver.findElement(By.xpath("//input[contains(@title,'Save')]")).click();
		
		Thread.sleep(5000);
		driver.quit();
	}
}
