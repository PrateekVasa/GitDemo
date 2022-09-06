package eCom;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import pojoEcom.LoginRequest;
import pojoEcom.LoginResponse;
import pojoEcom.OrdersDetails;
import pojoEcom.OrdersJson;

public class ECommerceAPITest 
{
	public static void main(String[] args) 
	{
		RequestSpecification LoginBasereq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").
									        setContentType(ContentType.JSON).build();
		
		//Login and get token
		LoginRequest l=new LoginRequest();
		l.setUserEmail("postman69@gmail.com");
		l.setUserPassword("Password123");
		
		RequestSpecification reqLogIn = 
				given().log().all().spec(LoginBasereq).body(l);
		LoginResponse loginRes = reqLogIn.
				when().post("api/ecom/auth/login").
				then().log().all().extract().response().as(LoginResponse.class);
		
		String token = loginRes.getToken();
		String userID = loginRes.getUserId();
		System.out.println(token);
		System.out.println(userID);
		
		//Create product
		RequestSpecification createProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").
				                                    addHeader("Authorization", token).build();
		RequestSpecification reqCreateProduct = 
				given().log().all().spec(createProductBaseReq).param("productName", "Laptop").param("productAddedBy", userID)
					   .param("productCategory", "fashion").param("productSubCategory", "shirts").param("productPrice", "89500")
			           .param("productDescription", "Asus ROG").param("productFor", "men")
			           .multiPart("productImage", new File("D:\\NOTES\\API Testing\\81rOuSWZtvL._SL1500_.jpg"));
		String CreateProductRes = reqCreateProduct.
				when().post("api/ecom/product/add-product").
				then().log().all().extract().response().asString();
		
		JsonPath js=new JsonPath(CreateProductRes);
		String productid = js.getString("productId");
		
		//Create order
		RequestSpecification createOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").
                                                  setContentType(ContentType.JSON).addHeader("Authorization", token).build();
		OrdersDetails od=new OrdersDetails();
		od.setCountry("India");
		od.setProductOrderedId(productid);
		List<OrdersDetails> odlist=new ArrayList<OrdersDetails>();
		odlist.add(od);
		
		OrdersJson o=new OrdersJson();
		o.setOrders(odlist);
		
		RequestSpecification reqCreateOrder = 
				given().log().all().spec(createOrderBaseReq).body(o);
		String createOrderRes = reqCreateOrder.
				when().post("api/ecom/order/create-order").
				then().log().all().extract().response().asString();
		
		JsonPath js1=new JsonPath(createOrderRes);
		String orderid = js1.getString("orders[0]");
		
		//Delete product
		RequestSpecification deleteProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").
                                                    setContentType(ContentType.JSON).addHeader("Authorization", token).build();
		RequestSpecification reqDeleteProduct =    
				given().log().all().spec(deleteProductBaseReq).pathParam("productID", productid);
		String deleteProductRes = reqDeleteProduct.
				when().delete("api/ecom/product/delete-product/{productID}").
				then().log().all().extract().response().asString();
		
		JsonPath js2=new JsonPath(deleteProductRes);
		Assert.assertEquals(js2.getString("message"), "Product Deleted Successfully");
		
		//Delete order
		RequestSpecification deleteOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").
                                                  setContentType(ContentType.JSON).addHeader("Authorization", token).build();
		RequestSpecification reqDeleteOrder = 
				given().log().all().spec(deleteOrderBaseReq).pathParam("orderID", orderid);
		String deleteOrderRes = reqDeleteOrder.
				when().delete("https://rahulshettyacademy.com/api/ecom/order/delete-order/{orderID}").
				then().log().all().extract().response().asString();
		
		JsonPath js3=new JsonPath(deleteOrderRes);
		Assert.assertEquals(js3.getString("message"), "Orders Deleted Successfully");
	}
}
