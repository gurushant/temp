import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * 
 */

/**
 * @author gurushant
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WebDriver driver=new HtmlUnitDriver();
		driver.get("http://www.google.com");
		WebElement element = driver.findElement(By.name("q"));	
		element.sendKeys("Baan Tao Pune,Adjacent to Aga Khan Palace,, 88, Pune Nagar Road, Kalyani Nagar, Pune, Maharashtra 411006, India");	
		element.submit();
		String source=driver.getPageSource();
		WebElement resultElement=driver.findElement(By.xpath("//div[contains(text(),'Baan Tao')]/.."));
		String text=resultElement.getText();
	
		System.out.println(text.substring(text.indexOf("(")+1,text.indexOf(")")));
//		if(source.contains("76"))
//		{
//			System.out.println(resultElement.getText());
//		}
//		else
//		{
//			System.out.println("Page title is: " + driver.getTitle());		
//		}
		driver.quit();
	}

}
