package testCases;

import org.testng.annotations.Test;

public class Parameters {
	@Test(groups = "SS")
	public void do_Prarameterization() {
		String URL=System.getProperty("url");
		String Pwd=System.getProperty("pwd");
		
		System.out.println("URL:"+URL+" Password:"+Pwd);
	}
}
