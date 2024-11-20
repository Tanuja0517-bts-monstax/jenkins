package tpass;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Creating_TroubleTicket_On_Vtiger {
	public Workbook wb=null;
	FileInputStream fis=null;
	WebDriver driver=null;
	Sheet sh=null;
	
	public static int generatRandomNumber() {
		Random random=new Random();
		return random.nextInt(500);
	}
	@DataProvider
	public Object[][] gettingDataToCreateTroubleTicket() throws EncryptedDocumentException, IOException {
		fis=new FileInputStream("./testData/TestData.csv");
		wb=WorkbookFactory.create(fis);
		sh=wb.getSheet("Trouble_Ticket");
		
		int rowCount=sh.getPhysicalNumberOfRows()-1;
		int colCount=sh.getRow(0).getPhysicalNumberOfCells();
		
		Object obj[][]=new Object[rowCount][colCount-1];
		for(int i=1;i<rowCount;i++) {
			for(int j=0;j<colCount-1;j++) {
				obj[i-1][j]=sh.getRow(i).getCell(j).toString();
			}
		}
		
		return obj;
	}
	
	@Test(dataProvider = "gettingDataToCreateTroubleTicket")
	public void addingTroubleTicket(String pName,String sDate,String pNumber,String pCategory,String title,String assignedTo,String contact_org,String u_g,String status,String priority,String severity) throws Exception {
		Select sel=null;
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3000));
		
		Properties prop=new Properties();
		prop.load(new FileInputStream("./testData/ConfigurationFile.properties"));
		driver.get(prop.getProperty("url"));
		
		driver.findElement(By.name("user_name")).sendKeys(prop.getProperty("user1"));
		driver.findElement(By.name("user_password")).sendKeys(prop.getProperty("pwd1"));
		driver.findElement(By.id("submitButton")).click();
		
		driver.findElement(By.linkText("Products")).click();
		driver.findElement(By.xpath("//img[contains(@title,'Create')]")).click();
		
		driver.findElement(By.name("productname")).sendKeys(pName);
		driver.findElement(By.id("productcode")).sendKeys(pNumber);
		driver.findElement(By.id("jscal_field_sales_start_date")).sendKeys(sDate);
		WebElement categoryDD=driver.findElement(By.name("productcategory"));
		
		sel=new Select(categoryDD);
		sel.selectByVisibleText(pCategory);
		driver.findElement(By.xpath("(//input[contains(@value,'Save')])[2]")).click();
		
		System.out.println("Product added❤️");
		
		driver.findElement(By.linkText("Trouble Tickets")).click();
		driver.findElement(By.xpath("//img[contains(@title,'Create')]")).click();
		driver.findElement(By.name("ticket_title")).sendKeys(title+generatRandomNumber()); 
		
		String parentWindow=driver.getWindowHandle();
		System.out.println("PW:"+parentWindow+" ug:"+u_g);
		if(u_g.equals("User")) {
			System.out.println("iff");
			driver.findElement(By.xpath("//input[@value='U']")).click();
			System.out.println("clicked");
			sel=new Select(driver.findElement(By.xpath("//select[@name='assigned_user_id']")));
			sel.selectByVisibleText(assignedTo);
			System.out.println("second click");
		}
		else {
			driver.findElement(By.xpath("//input[@value='T']")).click();
			sel=new Select(driver.findElement(By.xpath("//select[@name='assigned_group_id'")));
			sel.selectByVisibleText(assignedTo);
		}
		driver.findElement(By.xpath("//input[@name='product_name']/..//img[@title='Select']")).click();
		Set<String> windowss=driver.getWindowHandles();
		for(String win:windowss) {
			if(!win.equals(parentWindow))
				driver.switchTo().window(win);
		}
		
		driver.findElement(By.linkText(pName)).click();

		sel=new Select(driver.findElement(By.xpath("//select[@name='ticketstatus']")));
		sel.selectByVisibleText(status);
		
		sel=new Select(driver.findElement(By.xpath("//select[@name='ticketstatus']")));
		sel.selectByVisibleText(priority);
		
		sel=new Select(driver.findElement(By.name("ticketseverities")));
		sel.selectByVisibleText(severity);
		
		driver.findElement(By.xpath("(//input[contains(@value,'Save')])[2]")).click();
		System.out.println("Trouble Ticket created😁❤️");
		
//		FileOutputStream fos=new FileOutputStream("./testData/TestData.csv");
//		Cell cel=sh.getRow(0).createCell(11);
//		cel.setCellValue("TroubleTicket Added");
//		
//		wb.write(fos);
//		fos.close();
//		wb.close();
		driver.quit();
	}
}
