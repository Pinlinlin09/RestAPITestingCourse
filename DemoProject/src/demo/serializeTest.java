package demo;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import java.util.*;

import io.restassured.response.Response;
import pojo.AddPlace;
import pojo.Location;

public class serializeTest {

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
		
		Response res = given().log().all().queryParam("key", "qaclick123")
				.body(p)
				.when().post("/maps/api/place/add/json")
				.then().assertThat().statusCode(200).extract().response();
		String responseString = res.asString();
		System.out.println(responseString);
	}

}
