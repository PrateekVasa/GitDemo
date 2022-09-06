import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) 
	{
		JsonPath js=new JsonPath(Payload.CoursePrice());
		
		//Print number of courses returned by API
		int count = js.getInt("courses.size()");
		System.out.println(count);
		
		//Print purchase amount
		int totalamount = js.getInt("dashboard.purchaseAmount");
		System.out.println(totalamount);
		
		//Print title of the first course
		String title1 = js.getString("courses[0].title");
		System.out.println(title1);
		
		//Print all courses and their respective titles
		for(int i=0;i<count;i++)
		{
			String AllTitles=js.get("courses["+i+"].title");
			int AllPrices=js.get("courses["+i+"].price");
			System.out.println(AllTitles);
			System.out.println(AllPrices);
		}
		
		//Print number of copies sold by RPA
		System.out.print("Number of copies sold by RPA course: ");
		for(int i=0;i<count;i++)
		{
			String AllTitles=js.get("courses["+i+"].title");
			if (AllTitles.equalsIgnoreCase("RPA")) 
			{
				int copies=js.get("courses["+i+"].copies");
				System.out.println(copies);
				break;
			}
		}
	}

}
