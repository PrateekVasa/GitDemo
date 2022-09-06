package files;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class DynamicJson 
{
	@Test(dataProvider = "BooksData")
	public void addBook(String isbn,String aisle)
	{
		RestAssured.baseURI="http://216.10.245.166";
		String response=
		given().header("Content-Type", "application/json").body(Payload.AddBook(isbn,aisle)).
		when().post("Library/Addbook.php").
		then().assertThat().statusCode(200).extract().asString();
		
		JsonPath js = ReusableMethods.rawToJson(response);
		String id=js.get("ID");
		System.out.println(id);
	
		String response2=
		given().header("Content-Type", "application/json").body(Payload.DeleteBook(id)).
		when().post("Library/DeleteBook.php").
		then().assertThat().statusCode(200).extract().asString();
		
		JsonPath js1 = ReusableMethods.rawToJson(response2);
		String mess=js1.get("msg");
		System.out.println(mess);
	}
	
	@DataProvider(name = "BooksData")
	public Object[][] getData()
	{
		return new Object[][] {{"bajkfv","63213"},{"khfjhh","90589"},{"kaksdf","6792"}};
	}
}
