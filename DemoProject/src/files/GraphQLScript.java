package files;

import static io.restassured.RestAssured.*;

import org.testng.Assert;

import io.restassured.path.json.JsonPath;

public class GraphQLScript {

	public static void main(String[] args) {
		//Query
		//int characterId = 1442; //use this if want to variable outside of the query
		String response = given().log().all().header("Content-Type", "application/json")
		.body("{\"query\":\"query ($characterId: Int!,$episodeId: Int!, $locationId: Int!) {\\n  character(characterId:$characterId) {\\n    name\\n    gender\\n    status\\n    id\\n  }\\n  location(locationId:$locationId){\\n    name\\n    dimension\\n  }\\n  episode (episodeId:$episodeId){\\n    name\\n    air_date\\n    episode\\n  }\\n  characters(filters:{name:\\\"Rahul\\\"}){\\n    info{\\n      count\\n    }\\n    result{\\n      name\\n      type\\n    }\\n  }\\n  \\n  episodes(filters:{episode: \\\"hulu\\\"}){\\n    result{\\n      id\\n      name\\n      air_date\\n      episode\\n    }\\n  }\\n  \\n}\\n\",\"variables\":{\"characterId\":1442,\"episodeId\":1073,\"locationId\":1904}}")
		.when().post("https://rahulshettyacademy.com/gq/graphql").then().extract().asString();
		System.out.println(response);
		JsonPath js = new JsonPath(response);
		String characterName = js.getString("data.character.name");
		Assert.assertEquals(characterName, "Robin Hood");
		
		//Mutation
		String newCharacter = "Robin Hood";
		String mutationResponse = given().log().all().header("Content-Type", "application/json")
				.body("{\"query\":\"mutation ($locationName: String!, $characterName:String!, $episodeName:String! ) {\\n  createLocation (location: {name: $locationName, type: \\\"North America\\\", dimension: \\\"123\\\"}){\\n    id\\n  }\\n  \\n  createCharacter(character:{name:$characterName, type:\\\"Horseman\\\", status:\\\"halfDead\\\",species:\\\"Horseman\\\",gender:\\\"undefined\\\",image:\\\"png\\\", originId:1894,locationId:1893}){\\n    id\\n  }\\n  \\n  createEpisode (episode:{name:$episodeName,air_date: \\\"never\\\", episode:\\\"Netflix\\\"}){\\n   id \\n  }\\n  \\n  deleteLocations(locationIds: [1899, 1898]){\\n    locationsDeleted\\n  }\\n  \\n}\",\"variables\":{\"locationName\":\"USA\",\"characterName\":\""+newCharacter+"\",\"episodeName\":\"Manifest\"}}")
				.when().post("https://rahulshettyacademy.com/gq/graphql").then().extract().asString();
				System.out.println(mutationResponse);
				JsonPath js1 = new JsonPath(mutationResponse);
	}

}
