package files;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class DynamicJson {

		@Test(dataProvider = "BooksData")
		public void addBook(String isbn, String aisle) {
			RestAssured.baseURI = "http://216.10.245.166";
			String response = given().header("Content-Type", "application/json").
			body(payload.Addbook(isbn, aisle)).
			when()
			.post("/Library/Addbook.php")
			.then().log().all().assertThat().statusCode(200)
			.extract().response().asString();
			JsonPath js = ReUsableMethods.rawToJson(response);
			String id = js.getString("ID");
			System.out.println(id);
			
			//Delete Book
			
		}
		
		@Test (dataProvider = "BooksData",enabled=true)
		public void deleteBook(String isbn, String aisle) {
			RestAssured.baseURI = "http://216.10.245.166";
			String res = given().header("Content-Type", "application/json").
			body(payload.Deletebook(isbn,aisle)).
			when()
			.post("/Library/DeleteBook.php")
			.then().log().all().assertThat().statusCode(200)
			.extract().response().asString();
			System.out.println("Response starts " + res + " ends");
		}
		@DataProvider(name = "BooksData")
		public Object[][] getData() {
			/*Array = collections of elements:
			 * multi-dimensional array = collection of arrays
			 * */  
			return new Object [][] {{"ohmygod","1321"}, {"Habits", "0909"},{"Yetss", "0900"}};
		}
}
