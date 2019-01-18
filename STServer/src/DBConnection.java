import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
	Connection con;
	public static DBConnection DB;
	String dbPath = "jdbc:mysql://localhost:3306/snifftransport";
	String dbUser = "root";
	String dbPassword = "123456";

	
	public static DBConnection getInstance() {
		if(DB == null) {
			DB = new DBConnection();
		}
		return DB;
	}
	
	public DBConnection() {
		//constructor
	}

	public boolean init() throws SQLException {
		connectDB();
		ResultSet rs;
		boolean tableExist = false;
		try {

			java.sql.DatabaseMetaData dbm = con.getMetaData();

			rs = dbm.getTables(null, null, "data", null);
			if (rs.next()) {
				tableExist = true;
			} else {
				tableExist = false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (tableExist) {
			disconnectDB();
			return true;
		} else {
			createTable("data");
			disconnectDB();
			return true;
		}
	}

	public boolean createTable(String tableName) {
		try {
			java.sql.Statement statement = con.createStatement();

			String sql = "CREATE TABLE " + tableName + "(id VARCHAR(50), value VARCHAR(50), PRIMARY KEY (id));";

			((java.sql.Statement) statement).executeUpdate(sql);
			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public boolean connectDB() {

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(dbPath, dbUser, dbPassword);

		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public String insert(String key, String value) {

		try {
			java.sql.Statement statement = con.createStatement();

			String query = "INSERT INTO data VALUES(\"" + key + "\",\"" + value + "\");";

			((java.sql.Statement) statement).executeUpdate(query);

			System.out.println("SERVER: <" + key + ", " + value + "> has been inserted into the database.");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public String get(String key) {
		String value = "";
		try {

			Statement statement = (Statement) con.createStatement();

			String query = "SELECT value FROM data WHERE KEY=\"" + key + "\";";
			ResultSet result = ((java.sql.Statement) statement).executeQuery(query);

			result.first();
			value = result.getString("value");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("SERVER: <" + key + ", " + value + "> has been inserted into the database.");

		return value;
	}

	public String delete() {
		String value = "";

		// No need for it right now

		return value;
	}

	public boolean disconnectDB() {

		try {
			con.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
