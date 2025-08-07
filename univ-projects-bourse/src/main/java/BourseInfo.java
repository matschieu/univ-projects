
import java.sql.*;

public class BourseInfo {

	private static BourseInfo info = new BourseInfo();

	public static final char MAKER_ROLE = 'm';
	public static final char USER_ROLE  = 'u';

	private String driver = "";
	private String url 	  = "";
	private String id	  = "";
	private String pwd	  = "";

	private BourseInfo() { 
		try {
			if (System.getProperty("os.name").equals("Windows XP")) {
				driver = "sun.jdbc.odbc.JdbcOdbcDriver";
				url = "jdbc:odbc:myodbc";
				id = pwd = "";
			}
			if (System.getProperty("os.name").equals("Linux")) {
				driver = "com.mysql.cj.jdbc.Driver";
				url = "jdbc:mysql://mysql:3306/bourse";
				id = "root";
				pwd = "password";
			}
			Class.forName(driver);
			System.out.println("Driver correctement charge : ");
		}
		catch(ClassNotFoundException nf) { nf.printStackTrace(); }
		catch(Exception e) { e.printStackTrace(); }
	}

	public Connection getDBConnection() throws SQLException {
		return DriverManager.getConnection(url, id, pwd);
	}

	public static BourseInfo getInstance() { return info; }

}
