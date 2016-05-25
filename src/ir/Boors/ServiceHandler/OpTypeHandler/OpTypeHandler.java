package ir.Boors.ServiceHandler.OpTypeHandler;
import ir.Boors.Model.*;

import java.util.List;

public interface OpTypeHandler {
	public abstract List<String> handle(Order order,List<Order> sellOrders,List<Order> buyOrders);
}
