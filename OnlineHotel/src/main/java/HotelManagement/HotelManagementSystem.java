package HotelManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HotelManagementSystem {
	private static final String url="jdbc:mysql://localhost:3306/hotel";
	private static final String userName="root";
	private static final String password="admin";
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		try {
			Connection con = DriverManager.getConnection(url, userName, password);
			Staffs staffs = new Staffs(con, scan);
			Menu menu = new Menu(con, scan);
			while(true) {
				System.out.println("Welcome to online Hotel");
				System.out.println("1. Add Staff \n2. View Staffs \n3. Add Dish \n4. View Menu \n5. Order a Dish \n6. Exit");
				System.out.println("Enter your choice : ");
				int choice = scan.nextInt();
				switch (choice) {
				case 1 :
					staffs.addStaff();
					System.out.println();
					break;
				case 2 :
					staffs.viewStaffs();
					System.out.println();
					break;
				case 3 :
					menu.addDish();
					System.out.println();
					break;
				case 4 :
					menu.viewMenu();
					System.out.println();
					break;
				case 5 :
					orderDish(menu, con, scan);
					System.out.println();
					break;
				case 6 :
					System.out.println("Thank you for choosing our Online Hotel");
					return;
				default:
					System.out.println("Enter a valid choice!!!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void orderDish(Menu menu,Connection con,Scanner scan) {
		try {
			System.out.println("Enter Menu Id : ");
			int menuId = scan.nextInt();
			System.out.println("Enter Required Quantity : ");
			int ordercount = scan.nextInt();
			if(menu.getMenuById(menuId)) {
				if(checkDishAvailability(menuId, ordercount, con)) {
					String dishQuery = "Insert into menuorder(menu_id, ordercount) values(?, ?)";
					try {
						PreparedStatement prepstmt = con.prepareStatement(dishQuery);
						prepstmt.setInt(1, menuId);
						prepstmt.setInt(2, ordercount);
						int affectedRow = prepstmt.executeUpdate();
						if(affectedRow>0) {
							System.out.println("Order Booked Successfully");
						}
						else {
							System.out.println("Failed to Book Order!!!");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					System.out.println("Sorry Required quantity is not available in the hotel!!!");
				}
			}
			else {
				System.out.println("Sorry Menu is not available in the Hotel!!!");
			}
		} catch (Exception e) {
			System.out.println("Enter a valid count!!!");
			e.printStackTrace();
		}
	}
	
	public static boolean checkDishAvailability(int menuId, int orderCount, Connection con) {
		String selectquery = "select availcount from menu where id = ?";
		String updatequery = "update menu set availcount = availcount - ? where id = ?";
		try {
			con.setAutoCommit(false);
			try {
				PreparedStatement prepstmt = con.prepareStatement(selectquery);
				prepstmt.setInt(1, menuId);
				ResultSet resset = prepstmt.executeQuery();
				if(resset.next()) {
					int availcount = resset.getInt("availcount");
					if(availcount>=orderCount) {
						PreparedStatement updateprepstmt = con.prepareStatement(updatequery);
						updateprepstmt.setInt(1, orderCount);
						updateprepstmt.setInt(2, menuId);
						updateprepstmt.executeUpdate();
						return true;
					}
				}
			}
			finally {
				con.setAutoCommit(true);
			}
		} catch (Exception e) {
			 try {
		            con.rollback();
		        } catch (SQLException ex) {
		            System.out.println("Rollback failed!!! " + ex.getMessage());
		        }
			 System.out.println("Database error!!!" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
}
