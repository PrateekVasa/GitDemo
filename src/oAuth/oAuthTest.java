package oAuth;
import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojoDeSerialize.ApiJson;
import pojoDeSerialize.GetCourseJson;
import pojoDeSerialize.WebAutomationJson;

public class oAuthTest 
{
	public static void main(String[] args) 
	{
		String url="https://rahulshettyacademy.com/getCourse.php?code=4%2F0AdQt8qg8pjuFOaH5birk_j9YPTzbIlBGLed1Np9mei_yXWXgZrZqaRGClc3WWIept6Lwcg&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=0&prompt=none";
		String partial = url.split("code=")[1];
		String code = partial.split("&scope")[0];
		
		String accessTokenResponse=
		given().urlEncodingEnabled(false)
		       .queryParams("code",code).queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		       .queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W").queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
		       .queryParams("grant_type","authorization_code").
		when().log().all().post("https://www.googleapis.com/oauth2/v4/token").asString();
		
		JsonPath js=new JsonPath(accessTokenResponse);
		String accessToken = js.getString("access_token");
		
		GetCourseJson response=
		given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON).
		when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourseJson.class);
		
		//System.out.println(response);	  //If its in a String
		
		System.out.println(response.getLinkedIn());
		System.out.println(response.getInstructor());
		
		System.out.println(response.getCourses().getApi().get(1).getCourseTitle());
		
		List<ApiJson> apiCourses = response.getCourses().getApi();
		for(int i=0;i<apiCourses.size();i++)
		{
			if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
			{
				System.out.println(apiCourses.get(i).getPrice());
			}
		}
		String[] courseTitles= {"Selenium Webdriver Java","Cypress","Protractor"};
		ArrayList<String> a=new ArrayList<String>();
		List<WebAutomationJson> webAutomationCourses = response.getCourses().getWebAutomation();
		for(int i=0;i<webAutomationCourses.size();i++)
		{
			a.add(webAutomationCourses.get(i).getCourseTitle());
		}
		List<String> expectedTitles = Arrays.asList(courseTitles);
		
		Assert.assertTrue(a.equals(expectedTitles));
	}
}
