package tpass;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.Set;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import WD_JavaUtility.JavaUtility;
import genericUtilities.ReadingDataFromPropertiesFile;

public class Creating_Contact_With_Organization_Test {
	public WebDriver driver=null;
	@DataProvider
	public Object[][] dataToCreateContact() throws Exception {
		FileInputStream fis=new FileInputStream("./testData/TestData1.xlsx");
		Workbook wb=WorkbookFactory.create(fis);
		
		int row=wb.getSheet("Contact").getPhysicalNumberOfRows();
		int col=wb.getSheet("Contact").getRow(0).getPhysicalNumberOfCells()-1;
		Object[][] obj=new Object[(row-1)][col];
		System.out.println(row+" "+col);
		for(int i=1;i<row;i++) {
			for(int j=0;j<col;j++) {
				obj[(i-1)][j]=wb.getSheet("Contact").getRow(i).getCell(j).toString();
			}
		}
		return obj;
	}
	
	@Test(dataProvider = "dataToCreateContact")
	public void createContactWithOrg_Test(String org_name,String website,String phno,String emps,String email,String industry,String rating,String type,String ass_To,String assigne,String a_p,String f_Name,String l_Name) throws Exception {
		ReadingDataFromPropertiesFile prop=new ReadingDataFromPropertiesFile();
		String browser=prop.readDataFromFile("browser");
		//launching browser
		if(browser.equalsIgnoreCase("Chrome"))
			driver=new ChromeDriver();
		else if(browser.equalsIgnoreCase("Firefox"))
			driver=new FirefoxDriver();
		else if(browser.equalsIgnoreCase("edge"))
			driver=new EdgeDriver();
		else
			driver=new ChromeDriver();
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		
		//navigating to application & login
		driver.get(prop.readDataFromFile("url"));
		driver.findElement(By.name("user_name")).sendKeys(prop.readDataFromFile("user1"));
		driver.findElement(By.name("user_password")).sendKeys(prop.readDataFromFile("pwd1"));
		driver.findElement(By.id("submitButton")).click();
		
		//Creating organization
		driver.findElement(By.linkText("Organizations")).click();
		driver.findElement(By.xpath("//img[contains(@title,'Create Organization')]")).click();
		
		JavaUtility ju=new JavaUtility();
		String org=org_name+ju.generateRandomNumber();
		
		driver.findElement(By.name("accountname")).sendKeys(org);
		driver.findElement(By.name("website")).sendKeys(website);
		driver.findElement(By.id("phone")).sendKeys(phno);
		driver.findElement(By.id("employees")).sendKeys(emps);
		driver.findElement(By.id("email1")).sendKeys(email);
		
		Select sel=new Select(driver.findElement(By.name("industry")));
		sel.selectByVisibleText(industry);
		
		sel=new Select(driver.findElement(By.name("rating")));
		sel.selectByVisibleText(rating);
		
		sel=new Select(driver.findElement(By.name("accounttype")));
		sel.selectByVisibleText(type);
		
		if(ass_To.equals("User")) {
			driver.findElement(By.xpath("//input[@value='U']")).click();
			sel=new Select(driver.findElement(By.name("assigned_user_id")));
			sel.selectByVisibleText(assigne);
		}
		else {
			driver.findElement(By.xpath("//input[@value='T']")).click();
			sel=new Select(driver.findElement(By.name("assigned_group_id")));
			sel.selectByVisibleText(assigne);
		}
		driver.findElement(By.xpath("//input[contains(@title,'Save')]")).click();
		
		Thread.sleep(3000);
		//creating contact
		String parent=driver.getWindowHandle();
		driver.findElement(By.linkText("Contacts")).click();
		driver.findElement(By.xpath("//img[@src='themes/softed/images/btnL3Add.gif']")).click();
		sel=new Select(driver.findElement(By.name("salutationtype")));
		sel.selectByVisibleText(a_p);
		driver.findElement(By.name("firstname")).sendKeys(f_Name);
		driver.findElement(By.name("lastname")).sendKeys(l_Name);
		driver.findElement(By.id("mobile")).sendKeys(phno);
		driver.findElement(By.xpath("//input[@name='account_name']/following-sibling::img[@src='themes/softed/images/select.gif']")).click();
		Set<String> windows=driver.getWindowHandles();
		for(String win:windows) {
			if(!win.equals(parent)) 
				driver.switchTo().window(win);
		}
		driver.findElement(By.id("search_txt")).sendKeys(org);
		driver.findElement(By.id("search_txt")).sendKeys(Keys.ENTER);
		driver.findElement(By.linkText(org)).click();
		driver.switchTo().window(parent);
		
		if(ass_To.equals("User")) {
			driver.findElement(By.xpath("//input[@value='U']")).click();
			sel=new Select(driver.findElement(By.name("assigned_user_id")));
			sel.selectByVisibleText(assigne);
		}
		else {
			driver.findElement(By.xpath("//input[@value='T']")).click();
			sel=new Select(driver.findElement(By.name("assigned_group_id")));
			sel.selectByVisibleText(assigne);
		}
		driver.findElement(By.xpath("//input[contains(@title,'Save')]")).click();
		
		Actions action=new Actions(driver);
		action.moveToElement(driver.findElement(By.xpath("//img[@src='themes/softed/images/user.PNG']"))).perform();
		driver.findElement(By.linkText("Sign Out")).click();
		
		//driver.quit();
	}
	
	@Test
	public void simple_Test() {
		System.out.println("Hiii from simple test");
	}
}
