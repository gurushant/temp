import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Linkedin {

	static WebDriver driver=new FirefoxDriver();
	public static final String MESSAGE="Hello {name},\n"+
			"Hope you are doing great.This is Gurushant from India having 10+ years of experience in designing and developing scalable software systems.I have started working as a full time freelancer.I have a team of 4 folks having expertise in UI/UX ,Mobile and Web app development.\n"
			+"Thanks!!";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.gecko.driver", "geckodriver");

		driver.manage().timeouts().implicitlyWait(15000, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		WebDriverWait wait=new WebDriverWait(driver, 20);

		driver.get("https://www.linkedin.com");
		driver.findElement(By.id("login-email")).sendKeys("XXXXXX");
		driver.findElement(By.id("login-password")).sendKeys("XXXXX");
		driver.findElement(By.id("login-submit")).click();
		driver.findElement(By.id("a11y-ember1299")).sendKeys("co founder");
		driver.findElement(By.xpath("//button[@class='nav-search-button']")).click();
		int pageCount=10;
		int connectedCount=0;
		for(int p=0;p<pageCount;p++)
		{
			int recCount=driver.findElements(By.xpath("//ul[@class='results-list']/li")).size();
			for(int i=1;i<=recCount;i++)
			{
				String text=driver.findElement(By.xpath("//ul[@class='results-list']/li["+i+"]")).getText();
				String arr[]=text.split("\n");
				String txt=arr[arr.length-1];
				String name=text.split("\n")[0].split(" ")[0];
				if(txt.equals("Message"))
				{
					System.out.println("Already connected to : "+name);
				}
				else
					if(txt.equals("Following"))
					{
						System.out.println("Needs to follow. : "+name);
					}
					else
						if(txt.equals("Invite Sent"))
						{
							System.out.println("Invite Sent. : "+name);
						}
						else
							if(txt.equals("Connect"))
							{
								driver.findElement(By.xpath("//ul[@class='results-list']/li["+i+"]/div/div[3]/div/button")).click();
								driver.findElement(By.xpath("//button[@class='button-secondary-large']")).click();
								String m=MESSAGE.replace("{name}", name);
								driver.findElement(By.id("custom-message")).sendKeys(m);
								driver.findElement(By.xpath("//button[@class='button-primary-large ml3']")).click();
								System.out.println("Connected to : "+name);
								connectedCount++;
								sleep(3);
							}

							else
								System.out.println(txt+" : "+name);
			}
			System.out.println("Connected people count :"+connectedCount);
			//			connectedCount=0;
			driver.findElement(By.xpath("//button[@class='next']")).click();
			sleep(5);
		}
		//			driver.findElement(By.xpath("//ul[@class='results-list']/li["+i+"]/div/div/div[2]/a")).click();;
		//			WebElement element=wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//button[@class='connect primary top-card-action ember-view']"))));
		//			if(element!=null)
		//			{
		//				String name=driver.findElement(By.xpath("//h1[@class='pv-top-card-section__name Sans-26px-black-85% mb1']")).getText().split(" ")[0];
		//				driver.findElement(By.xpath("//button[@class='connect primary top-card-action ember-view']")).click();
		//				driver.findElement(By.xpath("//button[@class='button-secondary-large']")).click();;
		//				String m=MESSAGE.replace("{name}", name);
		//				driver.findElement(By.id("custom-message")).sendKeys(m);
		//				driver.findElement(By.xpath("//button[@class='button-primary-large ml3']")).click();
		//				driver.navigate().back();
		//			}
		//			driver.navigate().back();
		//		}
	}

	private static void sleep(int sec)
	{

		try {
			Thread.sleep(sec*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	static WebElement findElement(By by) {
		int attempts = 10;
		while (attempts < 10)
		{
			try {
				return driver.findElement(by);
			} catch (Exception e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				attempts++;
			}
		}
		return null;
	}

}
