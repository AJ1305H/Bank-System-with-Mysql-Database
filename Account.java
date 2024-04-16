package bank_system_v2;

import java.sql.*;
import java.util.Scanner;

import bank_system_v2.Main.account_type;

public class Account {
	
	int lastcustomerId;
	String url = "jdbc:mysql://127.0.0.1:3306/bank_system_v2";
	String user = "devuser";
	String pass = "Theta@123";
	ResultSet rs;
	Connection con;
	Statement st;
	Scanner sc = new Scanner(System.in);
	
	Account() {
		mysqlconnection mysqlcon = new mysqlconnection();
		con = mysqlcon.mysqlconn(url,user,pass);
		if(con == null)
			System.out.println("Connection Error! Can't Connect to Database.");
		try {
			st = con.createStatement();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	 
	
	
	int insertIntoCustomers(String firstName, String lastName, String mobileNumber, String emailId) throws SQLException {
		
		String insertCustomer = "insert into customers (first_name,last_name,mobile_no,email) values "
				+ "('"+firstName+"','"+lastName+"',"+mobileNumber+",'"+emailId+"')";
		try {
			int rowsAffected = st.executeUpdate(insertCustomer);
			return rowsAffected;
		} 
		catch (SQLException e) {
			System.out.println(e);
			return 0;
		}
		
	}
	
	int insertIntoLogin(String userName, String password) {
		int customerId;
		String selectCustomerId = "select id from customers order by id desc limit 1;";
		
		try {
			ResultSet rs = st.executeQuery(selectCustomerId);
			rs.next();
			customerId = rs.getInt(1);
			String insertLogin = "insert into login (username,password,customer_id) values ('"+userName+"','"+password+"',"+customerId+");";
			
			int rowsAffected = st.executeUpdate(insertLogin);
			return rowsAffected;
		} 
		catch (SQLException e) {
			System.out.println(e);
			return 0;
		}
	}
	
	int insertIntoAccounts(float balance, account_type accountType) {
		try {
			String insertAccount = "insert into accounts (balance,account_type) values ("+balance+",'"+accountType+"');";
			
			int rowsAffected = st.executeUpdate(insertAccount);
			return rowsAffected;
		} 
		catch (SQLException e) {
			System.out.println(e);
			return 0;
		}
	}
	
	int insertIntoDepositeWithdraw(int customerId) {
		if(user == null) {
			int x = insertIntoDepositeWithdraw();
			return x;
		}
		else {
			
			String selectAccountNumber = "select account_number from accounts order by account_number desc limit 1;";
			ResultSet rs;
			try {
				rs = st.executeQuery(selectAccountNumber);
				rs.next();	
				String insertQuery = "insert into deposite_withdraw (customer_id,acc_no) values ("+customerId+","+rs.getInt(1)+");";
				int rowsAffected = st.executeUpdate(insertQuery);
				return rowsAffected;
			} 
			catch (SQLException e) {
				System.out.println(e);
				return 0;
			}
			
		}
			
			
	}

	int insertIntoDepositeWithdraw() {
		int customerId;
		int accno;
		String selectCustomerId = "select id from customers order by id desc limit 1;";
		String selectAccountNumber = "select account_number from accounts order by account_number desc limit 1;";
		ResultSet rs1,rs2;
		try {
			rs1 = st.executeQuery(selectCustomerId);
			rs1.next();
			customerId = rs1.getInt(1);
			rs2 = st.executeQuery(selectAccountNumber);
			rs2.next();
			accno = rs2.getInt(1);
			
			String insertQuery = "insert into deposite_withdraw (customer_id,acc_no) values ("+customerId+","+accno+");";
			int rowsAffected = st.executeUpdate(insertQuery);
			return rowsAffected;
		} 
		catch (SQLException e) {
			System.out.println(e);
			return 0;
		}
	}
	
	void accountDetails(int customerId,int accno) {
	
		String selectAccountQuery = "select ac.account_number,ac.account_type,ac.balance from customers c" 
				+ " join deposite_withdraw dw on c.id = dw.customer_id"  
				+ " join accounts ac on ac.account_number = dw.acc_no"  
				+ " where c.id = "+customerId+" and ac.account_number = "+accno+";";
		
		try {
			rs = st.executeQuery(selectAccountQuery);
			if(rs.next()) {
				System.out.println("Account Number : "+rs.getInt(1));
				System.out.println("Account Type : "+rs.getString(2));
				System.out.println("Balance : "+rs.getString(3));
				System.out.println("---------------------------------");
				System.out.println();
			}
			else
			{
				System.out.println("Wrong Account Number");
			}
		} 
		catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	void viewAllAccountDetails(int customerId) {
		
		String selectAccountNoQuery = "select ac.account_number,ac.account_type,ac.balance from deposite_withdraw dw"
				+ " join accounts ac on ac.account_number = dw.acc_no"
				+ " where dw.customer_id = "+customerId+";";
		try {
			rs = st.executeQuery(selectAccountNoQuery);
			
			while(rs.next()) {
				System.out.println("Account Number : "+rs.getInt(1));
				System.out.println("Account Type : "+rs.getString(2));
				System.out.println("Balance : "+rs.getString(3));
				System.out.println("---------------------------------");
				System.out.println();
			}
		} 
		catch (SQLException e) {
			System.out.println(e);
		}	
	}
		
	void viewCustomerDetails(int customerId) {
		String selectCustomerDetailQuery = "select first_name,last_name,mobile_no,email from customers where id = "+customerId+";";
		try {
			rs = st.executeQuery(selectCustomerDetailQuery);
			while(rs.next()) {
				System.out.println("First Name : "+rs.getString(1));
				System.out.println("Last Name : "+rs.getString(2));
				System.out.println("Mobile Number : "+rs.getString(3));
				System.out.println("Email ID : "+rs.getString(4));
				
				System.out.println("---------------------------------");
				System.out.println();
			}
		} 
		catch (SQLException e) {
			System.out.println(e);
		}
	}
		
	public int deposit(int customerId,float bal,int accno) throws SQLException {
		
		
		float balance = getMainBalance(customerId,accno);
		if(balance != 0) {
			String updateQuery = "update accounts set balance = "+(balance+bal)+"where account_number = "+accno+";";
			try {
				st.executeUpdate(updateQuery);
				return 1;
			} catch (SQLException e) {
				System.out.println(e);
				return 0;
			}
		}
		else
			return 0;
		

	}

	

	

	float getMainBalance(int customerId,int accno){
		String selectAccountQuery = "select ac.balance from deposite_withdraw dw"  
				+ " join accounts ac on ac.account_number = dw.acc_no"  
				+ " where dw.customer_id = "+customerId+" and dw.acc_no = "+accno+";";
		try {
			rs = st.executeQuery(selectAccountQuery);
			if(rs.next()) {
				return rs.getFloat(1);
			}
			else {
				System.out.println("Wrong Account Number");
				return 0;
			}
		} catch (SQLException e) {
			System.out.println(e);
			return 0;
		}
		
	}

	
	
	int withdraw(int customerId,float bal,int accno) {
	
			float balance = getMainBalance(customerId,accno);
			if(balance >= bal) {
				String updateQuery = "update accounts set balance = "+ (balance-bal) +"where account_number = "+accno+";";
				try {
					st.executeUpdate(updateQuery);
					return 1;
				} catch (SQLException e) {
					System.out.println(e);
					return 0;
				}
			}
			else {
				System.out.println("Insufficient Balance!\nAvailable Main Balance ₹"+balance);
				return 0;

			}
	}		

	int closeAccount(int customerId,int accno) {
		
		String selectAccountQuery = "select ac.balance from deposite_withdraw dw"  
				+ " join accounts ac on ac.account_number = dw.acc_no"  
				+ " where dw.customer_id = "+customerId+" and dw.acc_no = "+accno+";";
		try {
			rs = st.executeQuery(selectAccountQuery);
			if(rs.next()) {
				String deleteAccountQuery = "delete from accounts where account_number = "+accno+";";
				int x = st.executeUpdate(deleteAccountQuery);
				return x;
			}
			else {
				System.out.println("Wrong Account Number");
				return 0;
			}
		}
		catch (SQLException e) {
			System.out.println(e);
			return 0;
		}
	}
	
	

	int countAccount(int customerId) {
		
		String selectQuery = "select count(acc_no) from deposite_withdraw where customer_id = "+customerId+";";
		int count;
		try {
			rs = st.executeQuery(selectQuery);
			rs.next();
			count = rs.getInt(1);
			return count;
		} catch (SQLException e1) {
			System.out.println(e1);
			return 0;
		}
	}
	
	
	void closeConnection() throws SQLException {		
		sc.close();		
		st.close();
		con.close();	
	}
	

}
	
	
