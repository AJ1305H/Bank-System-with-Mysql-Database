package bank_system_v2;

import java.sql.*;

public class mysqlconnection {

	public Connection mysqlconn(String url,String user,String password) {
		
		try {
			//Class.forName("com.mysql.jdbc.Driver");// Load and Register Driver. Now not required 
			Connection conn = DriverManager.getConnection(url,user,password);// Step 3: create's Connection 
			return conn;
		} 
		catch (Exception e) {
			
			System.out.println(e);
			
			return null;
		}
		
	}
}