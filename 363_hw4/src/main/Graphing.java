package main;

import java.util.Scanner;

import javax.imageio.stream.MemoryCacheImageInputStream;

import java.nio.channels.SelectableChannel;
import java.sql.*;

public class Graphing 
{
	public static String userName;
	public static String userPassword;
	public static int userChoice;
	public static final String dbURL = "jdbc:mysql://localhost:3306/maxv1";
	
	public static void main(String[] args)
	{
		Connection myConn;
		PreparedStatement query;
		Scanner inputScan = new Scanner(System.in);
		System.out.print("Please enter your username: ");
		userName = inputScan.nextLine();
		System.out.print("Please enter your password: ");
		userPassword = inputScan.nextLine();
		System.out.println("Select from the following choices using the numerical key: \n (1) Insert node \n (2) Insert edge \n (3) Delete node \n (4) List all reachable nodes \n");
		userChoice = inputScan.nextInt();
		try 
		{
			myConn = DriverManager.getConnection(dbURL, userName, userPassword);
			if (userChoice == 1)
			{
				query = myConn.prepareStatement("insert into nodes values (?, ?)");
				System.out.println("Insert node selected. Input node name and importance value.");
				System.out.print("New node name: ");
				String nodeName = inputScan.nextLine();
				query.setString(1, nodeName);
				System.out.print("New node importance: ");
				int nodeImp = inputScan.nextInt();
				query.setInt(2, nodeImp);
				query.executeUpdate();
			}
			if (userChoice == 2)
			{
				System.out.println("Insert edge selected. Input edge start, end, and cost value.");
				System.out.print("New edge start point: ");
				String edgeStart = inputScan.nextLine();
				System.out.print("New edge end point: ");
				String edgeEnd = inputScan.nextLine();
				System.out.print("New edge end cost: ");
				int edgeCost = inputScan.nextInt();
				
				int tempImp;
				query = myConn.prepareStatement("select nodename from nodes where nodename= ?");
				query.setString(1, edgeStart);
				ResultSet temp = query.executeQuery();
				if (!temp.next())
				{
					System.out.println("Node " + edgeStart + " does not exist, therefore a new node will be created.");
					System.out.print("Enter the importance value for this new node: ");
					tempImp = inputScan.nextInt();
					query = myConn.prepareStatement("insert into nodes values (?, ?)");
					query.setString(1, edgeStart);
					query.setInt(2, tempImp);
				}
				query = myConn.prepareStatement("select nodename from nodes where nodename= ?");
				query.setString(1, edgeEnd);
				temp = query.executeQuery();
				if (!temp.next())
				{
					System.out.println("Node " + edgeEnd + " does not exist, therefore a new node will be created.");
					System.out.print("Enter the importance value for this new node: ");
					tempImp = inputScan.nextInt();
					query = myConn.prepareStatement("insert into nodes values (?, ?)");
					query.setString(1, edgeEnd);
					query.setInt(2, tempImp);
				}
				
				query = myConn.prepareStatement("insert into edges values (?,?,?)");
				query.setString(1, edgeStart);
				query.setString(2, edgeEnd);
				query.setInt(3, edgeCost);
				query.executeUpdate();
			}
			if (userChoice == 3)
			{
				System.out.println("Delete node selected. Input name of node to delete.");
				System.out.println("Node to delete: ");
				String delName = inputScan.nextLine();
				query = myConn.prepareStatement("DELETE FROM edges WHERE startnode= ?; DELETE FROM edges WHERE endnode = ?; DELETE FROM nodes WHERE nodename= ?;");
				query.setString(1, delName);
				query.setString(2, delName);
				query.setString(3, delName);
				query.executeUpdate();
			}
			if (userChoice == 4)
			{
				CallableStatement callQuery;
				System.out.println("Return reachable nodes selected. Input name of starting node.");
				System.out.println("Start node: ");
				String startName = inputScan.nextLine();
			}
		}
		catch (SQLException e) 
		{
			System.out.println("SQL error - possibly wrong username/password");
		}

	}
	
}
