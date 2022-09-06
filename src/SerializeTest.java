import io.restassured.RestAssured;
import pojoSerialize.AddPlaceBody;
import pojoSerialize.LocationJson;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

public class SerializeTest {

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
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		String response=
		given().log().all().queryParam("key", "qaclick123").body(a).
		when().post("maps/api/place/add/json").
		then().assertThat().statusCode(200).extract().response().asString();
		
		System.out.println(response);
	}

}
