
//import org.junit.jupiter.api.Test;
import org.testng.Assert;
import org.testng.annotations.*;
import files.payload;
import io.restassured.path.json.JsonPath;

public class SumValidation {

	@Test
	public void sumOfCourses() {
		int sum = 0;
		JsonPath js = new JsonPath (payload.coursePrice());
		int numCourses = js.getInt("courses.size()");
		//Verify if sum of all course prices matches with Purchase Amount
				for(int i = 0; i < numCourses; i++) {
					int price = js.getInt("courses["+i+"].price");
					int copies = js.get("courses["+i+"].copies");
					int amount = copies*price;
					System.out.println(amount);
					sum += amount;
				}
				System.out.println(sum);
				int purchaseAmount = js.getInt("dashboard.purchaseAmount");
				Assert.assertEquals(sum, purchaseAmount);
	}
}
