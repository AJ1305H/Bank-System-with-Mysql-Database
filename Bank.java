package bank_system_v2;

import java.sql.ResultSet;
import java.sql.SQLException;


import bank_system_v2.Main.account_type;


public class Bank extends Account {

	
	String beginTransaction = "begin;";
	String commitTransaction = "commit;";
	String rollbackTransaction = "rollback;";
	int accountCount;
	static int customerID;
//	void beginTransaction() {
//		try {
//			st.execute(beginTransaction);
//		} catch (SQLException e) {
//			System.out.println(e);
//		}
//	}
	
//	void commitTransaction() {
//		try {
//			st.execute(commitTransaction);
//		} catch (SQLException e) {
//			System.out.println(e);
//		}
//	}
	
//	void rollbackTranaction() {
//		try {
//			st.execute(rollbackTransaction);
//		} catch (SQLException e) {
//			System.out.println(e);
//		}
//	}
	
	
	void registerUser(String firstName, String lastName, String mobileNumber, String emailId, String userName, String password) {
		
		try {
//			beginTransaction();
			st.execute(beginTransaction);
			int x = insertIntoCustomers(firstName, lastName, mobileNumber, emailId);
			if(x == 1) {
				x = insertIntoLogin(userName, password);
				if (x == 1) {
//					x = insertIntoAccounts(balance,accountType);
//					if(x ==1) {
//						x = insertIntoDepositeWithdraw();
//						if(x == 1) {
//							commitTransaction();
							st.execute(commitTransaction);
							System.out.println("Registration is done Successfully");
//							String getaccno = "select account_number from accounts order by account_number desc limit 1;";
//							ResultSet rs = a.st.executeQuery(getaccno);
//							rs.next();
//							System.out.println("Your Account Number : "+rs.getInt(1));
						}
						else
							st.execute(rollbackTransaction);
//							rollbackTransaction();
					}
					else
						st.execute(rollbackTransaction);
//						rollbackTransaction();
//				}
//				else
//					st.execute(rollbackTransaction);	
//			}
//			else
//				st.execute(rollbackTransaction);
//			
		} catch (SQLException e) {
			try {
				st.execute(rollbackTransaction);
			} catch (SQLException e1) {
				System.out.println(e1);
			}
//			rollbackTransaction();
			System.out.println(e);
		}
	}
	
	
	
	int login(String userName,String password) throws SQLException {
		String selectQuery = "select password from login where username = '"+userName+"';";
		String selectCustomerIdQuery = "select l.customer_id from login l "
				+ "join customers c on l.customer_id = c.id "
				+ "where l.username = '"+userName+"';";
		ResultSet rs = st.executeQuery(selectQuery);
		if(rs.next()) {
			if(password.equals(rs.getString(1))) {				
				rs = st.executeQuery(selectCustomerIdQuery);
				rs.next();
				customerID = rs.getInt(1);
				accountCount = countAccount(customerID);
				return 1;
			}
			else {
				return 0;
			}
		}
		else {
			return 0;
		}
	}

	
	
	void showCustomerDetails() {
		viewCustomerDetails(customerID);
	}
	

	void showAllAccountDetails() {
		if(accountCount > 0) {
			int accno = getAccountNumber();
			accountDetails(customerID, accno);
		}
		else {
			System.out.println("Please Open Account!");
		}
		
		
	}

	void deposite() {
		
		if(accountCount > 0) {
			int accno = getAccountNumber();
			float bal = getBalance();
			try {
				st.execute(beginTransaction);
				int x = deposit(customerID, bal, accno);
				if(x == 1) {
					st.execute(commitTransaction);
					float balance = getMainBalance(customerID, accno);
					System.out.println("CR ₹"+bal+"in your a/c\nAvailable Main Balance ₹"+balance);
				}
				else
					st.execute(rollbackTransaction);
				
			} 
			catch (SQLException e) {
				System.out.println(e);
				try {
					st.execute(rollbackTransaction);
				} 
				catch (SQLException e1) {
					System.out.println(e1);
				}
			}
		}
		else {
			System.out.println("Please Open Account!");
		}	
	}
	
	void withdraw() {
		
		if(accountCount > 0) {
			int accno = getAccountNumber();
			float bal = getBalance();
			try {
				st.execute(beginTransaction);
				int x = withdraw(customerID, bal, accno);
				if(x == 1) {
					st.execute(commitTransaction);
					float balance = getMainBalance(customerID, accno);
					System.out.println("Debited ₹"+bal+"from your a/c\nAvailable Main Balance ₹"+balance);
				}
				else
					st.execute(rollbackTransaction);
				
			} 
			catch (SQLException e) {
				System.out.println(e);
				try {
					st.execute(rollbackTransaction);
				} 
				catch (SQLException e1) {
					System.out.println(e1);
				}
			}
		}
		else {
			System.out.println("Please Open Account!");
		}
	}
	
	void openNewAccount(float balance,account_type accountType) {
		try {
			st.execute(beginTransaction);
			int x = insertIntoAccounts(balance, accountType);
			if(x == 1) {
				x = insertIntoDepositeWithdraw(customerID);
				if(x == 1) {
					st.execute(commitTransaction);
					accountCount++;
					String getaccno = "select account_number from accounts order by account_number desc limit 1;";
					ResultSet rs = st.executeQuery(getaccno);
					rs.next();
					System.out.println("Your Account Number : "+rs.getInt(1));
				}
				else
					st.execute(rollbackTransaction);
			}
			else
				st.execute(rollbackTransaction);
		} 
		catch (SQLException e) {
			System.out.println(e);
			try {
				st.execute(rollbackTransaction);
			} catch (SQLException e1) {
				System.out.println(e1);
			}
		}
		
	}
	
	void closeAccount() {
		
		if(accountCount > 0) {
			int accno = getAccountNumber();
			try {
				st.execute(beginTransaction);
				int x = closeAccount(customerID, accno);
				if(x == 1) {
					st.execute(commitTransaction);
					accountCount--;
					System.out.println("Your Account is been Deleted Successfully");
				}
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		else {
			System.out.println("Please Open Account!");
		}
		
	}
	
	int getAccountNumber() {
		
		System.out.println("Enter Account Number : ");
		int accno = sc.nextInt();
		return accno;
	}
	
	float getBalance() {
		System.out.println("Enter Amount to Deposite : ");
		float bal = sc.nextFloat();
		return bal;
	}
}

/**/