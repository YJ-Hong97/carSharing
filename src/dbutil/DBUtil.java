package dbutil;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class DBUtil {

	private Connection conn;
	
	private String userid ="ADMIN", pw = "Dbwls235689!";
	public Connection getConnection() {
			
		String url = "jdbc:oracle:thin:@db202204071815_medium?TNS_ADMIN=C:/java_hyj/Wallet_DB202204071815/";
		try {
			Class.forName("oracle.jdbc.OracleDriver");
				
			conn = DriverManager.getConnection(url,userid,pw);
				
				
		} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		return conn;
	}
	
	public void dbClose(ResultSet rs, Statement st,PreparedStatement pst, CallableStatement cst, Connection conn) {
		try {
			if(rs!= null)rs.close();
			if(st!=null)st.close();
			if(pst!=null)pst.close();
			if(cst!=null)cst.close();
			if(conn!= null)conn.close();
		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
}

