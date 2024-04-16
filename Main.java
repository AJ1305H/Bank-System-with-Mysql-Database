package bank_system_v2;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	public enum account_type {Saving,Current}
	
	public static void main(String[] args) throws SQLException {
		Bank bank = new Bank();
		String userName,password,firstName,lastName,mobileNumber, emailId;
		float balance;
		int log;
		int ch,ch1;
		account_type accountType; 
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("------MENU------");
			System.out.println("1. Login\n2. Sign Up\n3. Exit");
			System.out.println("----------------");
			System.out.println("Enter Your Choice : ");
			ch = sc.nextInt();  
			switch(ch) {
			default:
				System.out.println("Wrong Choice!");
				break;
			case 1:
				System.out.println("Enter Username : ");
				userName = sc.next();
				System.out.println("Enter Password : ");
				password = sc.next();
				try {
					log = bank.login(userName, password);
				} catch (SQLException e) {
					System.out.println(e);
					break;
				}
				if(log == 1) {
					System.out.println("Logged In Successfully!!!\n");
					do {
						
						System.out.println("------MENU------");
						System.out.println("1.Open New Account\n2.View Profile\n3.Show Account Details\n4.Deposit\n5.Withdraw\n6.Close Account\n7.Logout");
						System.out.println("----------------");
						System.out.println("Enter Your Choice : ");
						ch1 = sc.nextInt();
						switch(ch1) {
							default:
								System.out.println("Wrong Choice!");
								break;
							case 1:
								System.out.println("Enter Amount to depostie : ");
								balance = sc.nextFloat();
								System.out.println("1.Saving\n2.Current\nSelect Account Type : ");
								int x = sc.nextInt();
								switch(x) {
									default:
										System.out.println("Invalid Account Type!!!");
									case 1:
										accountType = account_type.Saving;
										break;
									case 2:
										accountType = account_type.Current;
								}
								bank.openNewAccount(balance, accountType);
								break;
							case 2:	
								bank.showCustomerDetails();
								
								/*
								do {
									System.out.println("------MENU------");
									System.out.println("1.Update Mobile Number\n2.Update Email Id\n3.Back");
									System.out.println("----------------");
									System.out.println("Enter Your Choice: ");
									int ch2 = sc.nextInt();
									switch(ch2) {
										default:
											System.out.println("Wrong Choice!");
											break;
										case 1:
											break;
										case 2:
											break;
										case 3:
											break;
									
									}
								}while(ch2 <= 3);*/
								break;
							case 3:
								bank.showAllAccountDetails();
								break;
							case 4:
								bank.deposite();
								break;
							case 5:
								bank.withdraw();
								break;
							case 6:
								bank.closeAccount();
								break;
							case 7:
								System.out.println("Logged Out Successfully");
								break;
						}
					}while(ch1 != 7);
				}
				else {
					System.out.println("Wrong Username or Password!!!");
				}
				break;
			case 2:
				System.out.println("Enter First Name: ");
				firstName = sc.next();
				System.out.println("Enter Last Name: ");
				lastName = sc.next();
				System.out.println("Enter Mobile Number: ");
				mobileNumber = sc.next();
				System.out.println("Enter Email:");
				emailId =  sc.next();
				System.out.println("Enter Username : ");
				userName = sc.next();
				System.out.println("Enter Password : ");
				password = sc.next();
				bank.registerUser(firstName,lastName,mobileNumber,emailId,userName,password);
				break;
			case 3:
				sc.close();
				bank.closeConnection();
				System.exit(0);
				break;
			}
		}while(ch != 3);
	}
}
