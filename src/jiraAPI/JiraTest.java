package jiraAPI;
import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class JiraTest {

	public static void main(String[] args) 
	{
		RestAssured.baseURI="http://localhost:8081";
		
		//Login
		SessionFilter session=new SessionFilter();
		given().header("Content-Type","application/json").body("{ \"username\": \"prateekvasa\", \"password\": \"Pr@teek0704\" }").log().all().
		        filter(session).
		when().post("/rest/auth/1/session").
		then().log().all().extract().response().asString();
		
		//Add comment
		String expMessage="Hi";
		String addCommentResponse=given().pathParam("id", "10001").log().all().header("Content-Type","application/json").body("{\r\n"
				+ "    \"body\": \""+expMessage+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}").filter(session).
		when().post("/rest/api/2/issue/{id}/comment").
		then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js=new JsonPath(addCommentResponse);
		String commentID=js.getString("id");
		
		//Add attachment
		given().pathParam("id", "10001").header("X-Atlassian-Token","no-check").filter(session).multiPart("file",new File("jira.txt")).
		        header("Content-Type","multipart/form-data").
		when().post("/rest/api/2/issue/{id}/attachments").
		then().log().all().assertThat().statusCode(200);
		
		//Get issue details
		String issueDetails=
		given().filter(session).pathParam("id", "10001").queryParam("fields", "comment").log().all().
		when().get("/rest/api/2/issue/{id}").
		then().log().all().extract().response().asString();
		
		System.out.println(issueDetails);
		
		JsonPath js1=new JsonPath(issueDetails);
		int commentsCount = js1.getInt("fields.comment.comments.size()");
		for(int i=0;i<commentsCount;i++)
		{
			String commentIssueID=js1.getString("fields.comment.comments["+i+"].id").toString();
			if(commentIssueID.equalsIgnoreCase(commentID))
			{
				String message=js1.getString("fields.comment.comments["+i+"].body").toString();
				System.out.println(message);
				Assert.assertEquals(message, expMessage);
			}
		}
	}

}
