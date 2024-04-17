package HotelManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Menu {
	private Connection con;
	private Scanner scan;
	
	public Menu(Connection con, Scanner scan) {
		this.con=con;
		this.scan=scan;
	}
	
	public void addDish() {
		System.out.println("Enter Dish Name : ");
		String name=scan.next();
		System.out.println("Enter Dish Price : ");
		int price=scan.nextInt();
		System.out.println("Enter Dish Count : ");
		int count=scan.nextInt();
		
		try {
			String query="Insert into menu(name, price, availcount) values(?, ?, ?)";
			PreparedStatement prepstmt=con.prepareStatement(query);
			prepstmt.setString(1, name);
			prepstmt.setInt(2, price);
			prepstmt.setInt(3, count);
			int affectedrows=prepstmt.executeUpdate();
			if(affectedrows>0) {
				System.out.println("Dish added to Menu successfully");
			}
			else {
				System.out.println("Failed to add Dish to Menu");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void viewMenu() {
		String query="select * from menu";
		try {
			PreparedStatement prepstmt=con.prepareStatement(query);
			ResultSet resset=prepstmt.executeQuery();
			System.out.println("Menu : ");
			System.out.println("+---------+---------------+-------+-----------+");
			System.out.println("| Menu Id | Name          | Price | Available |");
			System.out.println("+---------+---------------+-------+-----------+");
			while (resset.next()) {
				int id = resset.getInt("id");
				String name = resset.getString("name");
				int price = resset.getInt("price");
				int avail = resset.getInt("availcount");
				System.out.printf("|%-9s|%-15s|%-7s|%-11s|\n", id, name, price, avail);
				System.out.println("+---------+---------------+-------+-----------+");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean getMenuById(int id) {
		String query = "select * from menu where id = ?";
		try {
			PreparedStatement prepstmt=con.prepareStatement(query);
			prepstmt.setInt(1, id);
			ResultSet resset=prepstmt.executeQuery();
			if(resset.next()) {
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
