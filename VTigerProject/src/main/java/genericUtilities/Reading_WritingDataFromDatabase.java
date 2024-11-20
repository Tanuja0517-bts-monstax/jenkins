package genericUtilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.jdbc.Driver;

public class Reading_WritingDataFromDatabase {
	Connection con;
	public void getConnection(String url, String username,String pwd) throws SQLException, Exception {
		Driver d=new Driver();
		DriverManager.registerDriver(d);
		
		//Class.forName("com.mysql.cj.jdbc.Driver");
		con= DriverManager.getConnection(url, username, pwd);
	}
	
	public void closeConnection() throws SQLException {
		con.close();
	}
	
	public ResultSet executeConSelectQuery(String query) throws SQLException {
		Statement statm=con.createStatement();
		ResultSet set=statm.executeQuery(query);
		
		return set;
	}
	
	public int executeConNonSelectQuery(String query) throws SQLException {
		int set;
		Statement statm=con.createStatement();
		set =  statm.executeUpdate(query);
		return set;
	}
 }
