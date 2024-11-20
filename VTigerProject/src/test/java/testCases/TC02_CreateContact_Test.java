package testCases;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import POMPages.ContactPage;
import POMPages.HomePage;
import WD_JavaUtility.JavaUtility;
import baseUtility.BaseTest;
import genericUtilities.ListenerImplementation;

@Listeners(ListenerImplementation.class)
public class TC02_CreateContact_Test extends BaseTest {
	public HomePage hp=null;
	public ContactPage cp=null;
	public Select sel=null;
	public JavaUtility ju=null;
	@Test(groups = "Smoke")
	public void createContacr_Test() {
		String expectedContactNum="Tanujam";
		
		
		hp=new HomePage(driver);
		hp.getCont().click();
		hp.getAddButton().click();
		
		cp=new ContactPage(driver);
		
		sel=new Select(cp.getSalutationtype());
		sel.selectByIndex(2);
		ju=new JavaUtility();
		cp.getFname().sendKeys("Nagnath_"+ju.generateRandomNumber());
		cp.getLname().sendKeys("Sonawale");
		cp.getAccName().sendKeys("Starr");;
		cp.getGroup().click();
		sel= new Select(cp.getGroupDropdown());
		sel.selectByVisibleText("Marketing Group");
		cp.getSaveButton().click();
		
		hp.getCont().click();
		
		String val=driver.findElement(By.linkText("Nagnath")).getText();
		
		if(expectedContactNum.equals(val)) {
			ListenerImplementation.test.log(Status.PASS, "Contact created");
		}
		else {
			ListenerImplementation.test.log(Status.FAIL, "Contact not created");
		}
		
		Assert.assertEquals(val, expectedContactNum, "Contact is created");
	}
}
