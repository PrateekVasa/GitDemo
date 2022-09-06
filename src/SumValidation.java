import org.testng.Assert;
import org.testng.annotations.Test;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class SumValidation 
{
	@Test
	public void sumOfCourses()
	{
		JsonPath js=new JsonPath(Payload.CoursePrice());
		int count = js.getInt("courses.size()");
		int sum=0;
		for(int i=0;i<count;i++)
		{
			int AllPrices=js.getInt("courses["+i+"].price");
			int copies=js.getInt("courses["+i+"].copies");
			int amount=AllPrices*copies;
			System.out.println(amount);
			sum=sum+amount;
		}
		System.out.println(sum);
		
		int PurchaseAmount = js.getInt("dashboard.purchaseAmount");
		Assert.assertEquals(sum, PurchaseAmount);
	}
}
