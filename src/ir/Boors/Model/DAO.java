package ir.Boors.Model;
import ir.Boors.ServiceHandler.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.math.*;
import java.io.*;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;
import java.util.LinkedList;
import java.sql.*;

public class DAO {
	private static final String CONN_STR = "jdbc:hsqldb:hsql://localhost/bourse";
	static {
		try {
				Class.forName("org.hsqldb.jdbc.JDBCDriver");
			} catch (ClassNotFoundException ex) {
				System.err.println("Unable to load HSQLDB JDBC driver");
		}
	}

	public static User serachUser(int id) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from user where id = " + id);
		String name,family,email;
		int balance ;
		Map<String,Integer> sharesAmount = new HashMap<String, Integer>();
		if(!rs.next()){
			return null;
		}	
		name = rs.getString("name");
		family = rs.getString("family");
		balance = rs.getInt("balance");
		email = rs.getString("email");
		rs = st.executeQuery("select * from share where user_id = " + id);
		while(rs.next()){
			sharesAmount.put(rs.getString("stock_id"),rs.getInt("quantity"));
		}
		con.close();
		return new User(id,name,family,email,balance,sharesAmount);
	}

	public static int nextId(String table) throws SQLException {
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select max(id) as max_id from " + table);
		int maxId = 0;
		if (rs.next()) {
			maxId = rs.getInt("max_id");
		}
		con.close();
		return maxId + 1;
	}

	public static void addRecord(Record record) throws SQLException{
		DateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		st.executeUpdate("insert into record values ("+nextId("record") + ",'" 
																	+ record.getStockId() + "',"
																	+ record.getSeller().getId() + ","
																	+ record.getBuyer().getId() + ","
																	+ record.getQuantity() + "," 
																	+ record.getPrice() + ","
																	+ record.getSellerBalance() + ","
																	+ record.getBuyerBalance() + ",'" 
																	+ record.getOpType() + "','"
																	+ df.format(record.getDate()) + "')");	
		con.close();
	}

	public static Record createRecord(ResultSet rs) throws SQLException{
		User seller,buyer;
		String stockId,opType;
		int id,quantity,price,sellerId,buyerId,sellerBalance,buyerBalance;
		Date regDate;
		id = rs.getInt("id");
		stockId = rs.getString("stock_id");
		seller = serachUser(rs.getInt("seller_id"));
		buyer = serachUser(rs.getInt("buyer_id"));
		sellerBalance = rs.getInt("seller_balance");
		buyerBalance = rs.getInt("buyer_balance");
		quantity = rs.getInt("quantity");
		price = rs.getInt("price");
		opType = rs.getString("op_type");
		regDate = new Date(rs.getTimestamp("reg_date").getTime());
		return new Record(id,stockId,seller,buyer,sellerBalance,buyerBalance,quantity,price,opType,regDate);		
	}

	public static List<Record> getRecords() throws SQLException{
		List<Record> records = new LinkedList<Record>();
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from record");
		while(rs.next()){
			records.add(createRecord(rs));
		}		
		con.close();
		return records;
	}	


	public static Order createOrder(ResultSet rs,User user) throws SQLException{
		String stockId,orderType,opType;
		int id,quantity,price;
		id = rs.getInt("id");
		stockId = rs.getString("stock_id");
		quantity = rs.getInt("quantity");
		price = rs.getInt("price");
		orderType = rs.getString("request_type");
		opType = rs.getString("op_type");
		return new Order(id,stockId,user,quantity,price,orderType,opType);		
	}

	public static void loadUserOrders(User user) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from request where user_id=" + user.getId());
		while(rs.next()){
			user.addOrder(createOrder(rs,user));
		}
		con.close();		
	}	

	public static void addOrder(Order order) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		st.executeUpdate("insert into request values ("+nextId("request") + ",'" 
																	+ order.getStockId() + "',"
																	+ order.getUser().getId() + ","
																	+ order.getQuantity() + "," 
																	+ order.getPrice() + ",'" 
																	+ order.getOrderType() + "','" 
																	+ order.getOpType() + "')");
		con.close();

	}

	public static void updateOrder(Order order) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();		
		st.executeUpdate("update request set quantity=" + order.getQuantity() + " where id=" + order.getId());
		con.close();
	}

	public static void deleteOrder(Order order) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		st.executeUpdate("delete from request where id=" + order.getId());	
		con.close();	
	}
	
	public static void addUser(User user) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();		
		st.executeUpdate("insert into user values(" + user.getId() + ",'" + user.getPassword() +"','" + user.getName() + "','" + user.getFamily() + "','"+user.getEmail()+"'," + user.getBalance() + ")" );
		con.close();
	}

	public static void updateUserInfo(User user) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();		
		st.executeUpdate("update user set name='" + user.getName() + "',family='" + user.getFamily() + "',balance=" + user.getBalance() + " where id=" + user.getId());
		con.close();
	}

	public static void addShareAmount(int userId,String stockId,int amount) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();		
		st.executeUpdate("insert into share values('" + stockId + "'," + userId + "," + amount + ")");		
		con.close();
	}

	public static Integer findShareAmount(int userId,String stockId) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();		
		ResultSet rs = st.executeQuery("select quantity from share where user_id=" + userId + "and stock_id='" + stockId + "'");
		if(rs.next()){
			con.close();
			return rs.getInt("quantity");
		}else{
			con.close();
			return null;
		}			
	}


	public static void updateShareAmount(int userId,String stockId,int newAmount) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();		
		st.executeUpdate("update share set quantity=" + newAmount + " where user_id=" + userId + "and stock_id='" + stockId + "'");		
		con.close();		
	}

	public static void addStock(Stock stock) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();		
		st.executeUpdate("insert into stock values('" + stock.getId() + "', "+stock.getOwnerId()+" )");
		con.close();
	}	

	public static List<Order> findStockOrders(String stockId,String orderType) throws SQLException{
		List<Order> orders = new LinkedList<Order>();
		User user;
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from request where stock_id='" +stockId + "' and request_type='" + orderType+ "'");		
		while(rs.next()){
			user = serachUser(rs.getInt("user_id"));
			orders.add(createOrder(rs,user));
		}
		con.close();
		return orders;
	}

	public static Stock serachStock(String id) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from stock where id='" +id+"'");
		if(rs.next()){			
			con.close();
			return new Stock(id,findStockOrders(id,"sell"),findStockOrders(id,"buy"));
		}else{		
			con.close();
			return null ;
		}		
	}

	public static List<Stock> getStocks() throws SQLException{
		List<Stock> stocks = new LinkedList<Stock>();
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from stock");
		String stockId;
		while(rs.next()){			
			stockId = rs.getString("id");
			stocks.add(new Stock(stockId,findStockOrders(stockId,"sell"),findStockOrders(stockId,"buy")));
		}		
		con.close();
		return stocks;
	}


	public static void addPendingStock(Order o)throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();		
		st.executeUpdate("insert into pending_stock values('"+ o.getStockId()+ "',"+o.getUser().getId()+","+o.getQuantity()+","+o.getPrice()+",'"+o.getOpType() +"')");
		con.close();
	}


	public static Stock findPendingStock(String stockName)throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();	
		ResultSet rs = st.executeQuery("select * from pending_stock where id='" + stockName + "'");
		con.close();
		if(rs.next()){
			return new Stock(rs.getString("id"),rs.getInt("owner_id"));
		}else{
			Stock s = serachStock(stockName);
			if(s == null){
				return null;
			}
			return s;
		}		
	}

	public static List<Order> getPendingStocks() throws SQLException{
		List<Order> orders = new LinkedList<Order>();
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from pending_stock");
		while(rs.next()){			
			String stockId = rs.getString("id");
			int ownerId = rs.getInt("owner_id");
			int quantity = rs.getInt("quantity");
			int price = rs.getInt("price");
			String request_type = rs.getString("request_type");
			orders.add(new Order(-1,stockId,new User(ownerId),quantity,price,"sell",request_type));
		}		
		con.close();
		return orders;
	}

	public static void confirmSymbol(Stock s)throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		st.executeUpdate("delete from pending_stock where id='" + s.getId()+"'");	
		addStock(s);
		con.close();	
	}


	public static void setLimit(int limit) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from config where name='limit'");
		if(rs.next()){
			st.executeUpdate("update config set value=" + limit + " where name='limit'");	
		}
		else{
			st.executeUpdate("insert into config values ('limit',"+limit+")");
		}
		con.close();
	}

	public static int getLimit()throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from config where name='limit'");
		if(rs.next()){
			int limit = rs.getInt("value");
			return limit;	
		}
		else{
			return -1;
		}
	}

	public static void addExpensiveOrder(Order order)throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		st.executeUpdate("insert into expensive_request values ("+nextId("expensive_request") + ",'" 
																	+ order.getStockId() + "',"
																	+ order.getUser().getId() + ","
																	+ order.getQuantity() + "," 
																	+ order.getPrice() + ",'" 
																	+ order.getOrderType() + "','" 
																	+ order.getOpType() + "')");
		con.close();
	}

	public static List<Order> getExpensiveRequests() throws SQLException{
		List<Order> orders = new LinkedList<Order>();
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from expensive_request");
		while(rs.next()){
			int id = rs.getInt("id");			
			String stockId = rs.getString("stock_id");
			int ownerId = rs.getInt("user_id");
			int quantity = rs.getInt("quantity");
			int price = rs.getInt("price");
			String request_type = rs.getString("request_type");
			String op_type = rs.getString("op_type");
			orders.add(new Order(id,stockId,new User(ownerId),quantity,price,request_type,op_type));
		}		
		con.close();
		return orders;
	}

	public static void confirmExpensive(int id)throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		st.executeUpdate("delete from expensive_request where id=" + id);
		con.close();
	}

	public static List<String> getStocksOfUser(int id)throws SQLException{
		List<String> strings = new LinkedList<String>();
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from stock where owner_id="+id);
		while(rs.next()){
			String stockId = rs.getString("id");			
			strings.add(stockId);
		}
		return strings;	
	}

	public static List<Share> getShares(String name)throws SQLException{
		List<Share> shares = new LinkedList<Share>();
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from share where stock_id='"+name+"'");
		while(rs.next()){
			int id = rs.getInt("user_id");
			int count = rs.getInt("quantity");			
			shares.add(new Share(id,count));
		}
		return shares;	
	}





	public static void addDepositRequest(DepositRequest dr) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		st.executeUpdate("insert into deposit_req values(" + nextId("deposit_req") + "," + dr.getUserId() + "," + dr.getAmount() + ")" );
		con.close();
	}

	public static void loadUserDepositRequests(User user) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from deposit_req where user_id=" + user.getId());
		user.clearDepositRequests();
		while(rs.next()){
			user.addDepositRequest(new DepositRequest(rs.getInt("id"),rs.getInt("user_id"),rs.getInt("amount")));
		}
		con.close();		
	}	

	public static void loadUserRoles(User user) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from user_role where id=" + user.getId());
		user.clearRoles();
		while(rs.next()){
			user.addRole(rs.getString("role"));
		}
		con.close();		
	}		

	public static DepositRequest findDepositRequestById(int requestId) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from deposit_req where id=" + requestId);
		if(rs.next()){
			con.close();
			return new DepositRequest(rs.getInt("id"),rs.getInt("user_id"),rs.getInt("amount"));
		}else{
			con.close();
			return null ;
		}
	}

	public static List<DepositRequest> getDepositRequests() throws SQLException{
		List<DepositRequest> requests = new LinkedList<DepositRequest>(); 
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from deposit_req");
		while(rs.next()){
			requests.add(new DepositRequest(rs.getInt("id"),rs.getInt("user_id"),rs.getInt("amount")));
		}
		con.close();
		return requests;
	}

	public static void deleteDepositRequest(int requestId) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		st.executeQuery("delete from deposit_req where id=" + requestId);
		con.close();
	}

	public static boolean hasUserRole(int userId,String role) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs =st.executeQuery("select * from user_role where id=" + userId + " and role='" + role + "'");
		con.close();
		if(rs.next())
			return true;
		else
			return false;
	}

	public static void addUserRole(int userId,String role) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		st.executeQuery("insert into user_role values(" + userId + ",'" + role + "')");
		con.close();
	}


	public static int getCSRF(String sessionId) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from config where name='"+sessionId +"'");
		if(rs.next()){
			int csrf = rs.getInt("value");	
			con.close();
			return csrf;
		}
		else{
			Random rnd = new Random();
			int csrf = 100000 + rnd.nextInt(900000);
			st.executeUpdate("insert into config values ('"+sessionId+"',"+csrf+")");
			con.close();
			return csrf;
		}
		
	}

	public static void delCSRF(String sessionId)throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		st.executeQuery("delete from config where name='" + sessionId+"'");
		con.close();
	}





}
