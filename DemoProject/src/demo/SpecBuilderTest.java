package demo;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;

import java.util.*;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;

public class SpecBuilderTest {

	public static void main(String[] args) {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		AddPlace p = new AddPlace();
		p.setAccuracy(50);
		p.setAddress("915 E High S");
		p.setLanguage("English");
		p.setPhone_number("(+1) 434-977-0437");
		p.setWebsite("https://www.cfainstitute.org/");
		p.setName("CFA Institute");
		List <String> myList = new ArrayList<String>();
		myList.add("CFA Insitute");
		myList.add("company");
		p.setTypes(myList);
		//you need to set Location object here, refer to AddPlace.java
		Location l = new Location();
		l.setLat(-38.383494);
		l.setLng(33.427362);
		p.setLocation(l);
		
		/*build object of RequestSpecification
		 * set URL
		 * add Query Param
		 * set Content Type is optional for this test. this service is option here
		 */
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123")
		.setContentType(ContentType.JSON).build();
		
		/* ResponseSpecBuilder
		 * response specification we start with expect, unlike request start with set
		 */
		ResponseSpecification resSpec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		RequestSpecification res = given().spec(req)
				.body(p);
		//use RequestSpecification object here to hit the API and then get the response
				Response response = res.when().post("/maps/api/place/add/json")
				.then().spec(resSpec).extract().response();
		String responseString = response.asString();
		System.out.println(responseString);
	}
}
