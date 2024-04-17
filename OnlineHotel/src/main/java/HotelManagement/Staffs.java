package HotelManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Staffs {
	private Connection con;
	private Scanner scan;
	
	public Staffs(Connection con, Scanner scan) {
		this.con=con;
		this.scan=scan;
	}
	
	public void addStaff() {
		System.out.println("Enter Staff Name : ");
		String name = scan.next();
		System.out.println("Enter Role of the Staff : ");
		String role = scan.next();
		try {
			String query="Insert into staffs(name, role) values(?, ?)";
			PreparedStatement prepstmt=con.prepareStatement(query);
			prepstmt.setString(1, name);
			prepstmt.setString(2, role);
			int affectedrows=prepstmt.executeUpdate();
			if(affectedrows>0) {
				System.out.println("Staff added successfully ");
			}
			else {
				System.out.println("Failed to add Staff!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void viewStaffs() {
		String query="select * from staffs";
		try {
			PreparedStatement prepstmt=con.prepareStatement(query);
			ResultSet resset=prepstmt.executeQuery();
			System.out.println("Staffs : ");
			System.out.println("+----------+---------------+--------------+");
			System.out.println("| Staff Id | Name          | Role         |");
			System.out.println("+----------+---------------+--------------+");
			while (resset.next()) {
				int id = resset.getInt("id");
				String name = resset.getString("name");
				String role = resset.getString("role");
				System.out.printf("|%-10s|%-15s|%-14s|\n", id, name, role);
				System.out.println("+----------+---------------+--------------+");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
