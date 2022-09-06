package jiraAPI;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class AddAndUpdateComment 
{
	@Test
	public void tc1()
	{
		RestAssured.baseURI="http://localhost:8081";
		
		//Login
		SessionFilter session=new SessionFilter();
		given().header("Content-Type","application/json").body("{ \"username\": \"prateekvasa\", \"password\": \"Pr@teek0704\" }").log().all().
		        filter(session).
		when().post("/rest/auth/1/session").
		then().log().all().extract().response().asString();
		
		//Add comment
		String Message="Hellooo";
		String addCommentResponse=
		given().pathParam("issueid", "10001").log().all().header("Content-Type","application/json").
		        body("{\r\n"
				   + "    \"body\": \""+Message+"\",\r\n"
			       + "    \"visibility\": {\r\n"
				   + "        \"type\": \"role\",\r\n"
				   + "        \"value\": \"Administrators\"\r\n"
				   + "    }\r\n"
				   + "}").filter(session).
		when().post("/rest/api/2/issue/{issueid}/comment").
		then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js=new JsonPath(addCommentResponse);
		String commentID=js.getString("id");
		
		//Update comment
		String updatedMessage="Byee";
		String updatedComment=
		given().pathParam("issueid", "10001").pathParam("cId", commentID).log().all().header("Content-Type","application/json").filter(session).
		        body("{\r\n"
		        		+ "    \"body\": \""+updatedMessage+"\",\r\n"
		        		+ "    \"visibility\": {\r\n"
		        		+ "        \"type\": \"role\",\r\n"
		        		+ "        \"value\": \"Administrators\"\r\n"
		        		+ "    }\r\n"
		        		+ "}").
		when().put("/rest/api/2/issue/{issueid}/comment/{cId}").
		then().assertThat().statusCode(200).log().all().extract().response().asString();
		
		System.out.println(updatedComment);
		
		JsonPath js1=new JsonPath(updatedComment);
		String actualUpdated = js1.getString("body");
		System.out.println(actualUpdated);
	}
}
