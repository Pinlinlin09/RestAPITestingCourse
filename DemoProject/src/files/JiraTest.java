package files;
import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class JiraTest {

	public static void main(String[] args) {
		RestAssured.baseURI = "http://localhost:8080";
		
		//Login Scenario
		//relaxedHTTPSValidation after given for https web, it will take care of the certification
		SessionFilter session = new SessionFilter();//this variable can store the response. Use before 'when()'. can use this instead of JsonPath
		String response = given().relaxedHTTPSValidation().header("Content-Type", "application/json").
		body("{ \"username\": \"Pinlinl1\", \"password\": \"Jkeyboardasd.123\" }").
		log().all().filter(session).when().post("/rest/auth/1/session").then().log().all().extract().response().asString();
		
		//Add Comment
		String expectedMessage = "Hi there";
		String addCommentResponse = given().pathParam("id", "10003").
		log().all().header("Content-Type", "application/json").
		body("{\r\n"
				+ "    \"body\": \""+expectedMessage+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}").filter(session).when().post("/rest/api/2/issue/{id}/comment").
		then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js = new JsonPath(addCommentResponse);
		String commentId = js.getString("id");
		
		//Add Attachment; key is file(need to create File class so java knows it is a file
		given().header("X-Atlassian-Token","no-check").filter(session).pathParam("id", "10003").
		header("Content-Type","multipart/form-data").
		multiPart("file", new File("jira.txt")).
		when().post("/rest/api/2/issue/{id}/attachments").
		then().log().all().assertThat().statusCode(200);
		
		//Get Issue - interest in only reviewing "comment" field
		String issueDetails = given().filter(session).pathParam("id", "10003").
				queryParam("fields","comment").
				log().all().when().get("/rest/api/2/issue/{id}").
		then().log().all().extract().response().asString();
		System.out.println(issueDetails);
		
		JsonPath js1 = new JsonPath(issueDetails);
		int commentsCount = js1.getInt("fields.comment.comments.size()");
		for(int i = 0; i < commentsCount; i++) {
			String issueCommentId = js1.get("fields.comment.comments["+i+"].id").toString();
			if(issueCommentId.equalsIgnoreCase(commentId)) {
				String message = js1.get("fields.comment.comments["+i+"].body").toString();
				System.out.println(message);
				Assert.assertEquals(message, expectedMessage);
			}
		}
		
	}

}
