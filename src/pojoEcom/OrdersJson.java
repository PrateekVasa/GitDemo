package pojoEcom;

import java.util.List;

public class OrdersJson 
{
	private List<OrdersDetails> orders;

	public List<OrdersDetails> getOrders() {
		return orders;
	}

	public void setOrders(List<OrdersDetails> orders) {
		this.orders = orders;
	}
}
