import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojoSerialize.AddPlaceBody;
import pojoSerialize.LocationJson;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

public class SpecBuilderTest {

	public static void main(String[] args) 
	{
		AddPlaceBody a=new AddPlaceBody();
		
		LocationJson l=new LocationJson();
		l.setLat(-38.383494);
		l.setLng(33.427362);
		a.setLocation(l);
		
		a.setAccuracy(50);
		a.setName("Frontline house");
		a.setPhone_number("(+91) 983 893 3937");
		a.setAddress("29, side layout, cohen 09");
		
		List<String> list=new ArrayList<String>();
		list.add("shoe park");
		list.add("shop");
		a.setTypes(list);
		
		a.setWebsite("http://google.com");
		a.setLanguage("French-IN");
		
		RequestSpecification reqspec = new RequestSpecBuilder().
		setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123").setContentType(ContentType.JSON).build();
		
		ResponseSpecification resspec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		RequestSpecification request = given().spec(reqspec).body(a);
		String response = request.
		when().post("maps/api/place/add/json").
		then().spec(resspec).extract().response().asString();
		
		System.out.println(response);
	}

}
