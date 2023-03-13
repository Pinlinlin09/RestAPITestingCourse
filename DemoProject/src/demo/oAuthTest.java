package demo;
import static io.restassured.RestAssured.*;

import java.util.*;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourse;
import pojo.WebAutomation;

public class oAuthTest {

	public static void main(String[] args) throws InterruptedException {
		/**Using Selenium..to get the url and then to get the code using split method in java
		//google will stop people from login using automation bot
//		System.setProperty("webdriver.chrome.driver", "C://chromedriver.exe");
//		WebDriver driver = new ChromeDriver();
//		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
//		driver.findElement(By.cssSelector("input[type='email']")).sendKeys("srinath19830");
//		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
//		Thread.sleep(3000);
//		driver.findElement(By.cssSelector("input[type='password']")).sendKeys("password");
//		driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.ENTER);
//		Thread.sleep(4000);
		String url = driver.getCurrentUrl();*/
		String[] courseTitles = {"Selenium Webdriver java", "Cypress", "Protractor"};
		
		String url ="https://rahulshettyacademy.com/getCourse.php?state=verifyfjdss&code=4%2FvAHBQUZU6o4WJ719NrGBzSELBFVBI9XbxvOtYpmYpeV47bFVExkaxWaF_XR14PHtTZf7ILSEeamywJKwo_BYs9M&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&session_state=0c32992f0d47e93d273922018ade42d1072b9d1f..a35c&prompt=none#";
		String partialCode = url.split("code=")[1];//****
		String code = partialCode.split("&scope")[0];
		System.out.println(code);
		
		//Getting access token
		//Explicit tell restAssured encode anything with special character
		String accessTokenRes = given().urlEncodingEnabled(false)
		.queryParams("code", code)
		.queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		.queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
		.queryParams("grant_type","authorization_code")
		.when().log().all()
		.post("https://www.googleapis.com/oauth2/v4/token").asString();
		JsonPath js = new JsonPath(accessTokenRes);
		String accessToken = js.getString("access_token");
		
		GetCourse gc = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON) //this will tell rest assured reads as default json
		.when()//.log().all() will be removed because of the defaultParser
		.get("https://rahulshettyacademy.com/getCourse.php")
		.as(GetCourse.class); //short-cut to grab the string for raw response: asString(); And now are we using GetCourse deserialization for it
		//if you want to get LinkedIn
		System.out.println(gc.getLinkedIn());
		System.out.println(gc.getInstructor());
		//get(1) is bc it is list and you want to get the 1 index
		System.out.println(gc.getCourses().getApi().get(1).getCourseTitle()); 
		
		List<Api> apiCourses = gc.getCourses().getApi();
		for(int i = 0; i<apiCourses.size();i++) {
			if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
				System.out.println(apiCourses.get(i).getPrice());
				//gc.getCourses().getMobile().get(1).getCourseTitle();
			}
		}
		
		//print all course titles in webautomation
		List<WebAutomation> webAutomations = gc.getCourses().getWebAutomation();
		
		ArrayList<String> a = new ArrayList<String>();
		for(int i = 0; i<webAutomations.size();i++) {
			a.add(webAutomations.get(i).getCourseTitle());
		}
		//convert Array to List
		List<String> expectedList = Arrays.asList(courseTitles);
		Assert.assertTrue(a.equals(expectedList)); 
		//System.out.println(response);
	}
}
