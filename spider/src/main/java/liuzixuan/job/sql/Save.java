package liuzixuan.job.sql;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * 
 * 打开MySQL数据库的引擎，并插入数据
 * 
 */
public class Save {
	
	private Connection con;
	private String url = "jdbc:mysql://localhost:3306/job?useUnicode=true&characterEncoding=utf-8";

	private String username ="root";
	private String password = "123456";
	
	public void connect() throws SQLException{
		this.con = DriverManager.getConnection(this.url, this.username, this.password);	
	}
	
	public void insert(HashMap<String, String> list) throws SQLException{
		
		
		PreparedStatement ps = this.con.prepareStatement("insert into job (school,title,name,salary,position,education,property,num,time,interview,requires,resume,url,stamp) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1, list.get("school"));
		ps.setString(2, list.get("title"));
		ps.setString(3, list.get("name"));
		ps.setString(4, list.get("salary"));
		ps.setString(5, list.get("position"));
		ps.setString(6, list.get("education"));
		ps.setString(7, list.get("property"));
		ps.setString(8, list.get("num"));
		ps.setString(9, list.get("time"));
		ps.setString(10, list.get("interview"));
		ps.setString(11, list.get("require"));
		ps.setString(12, list.get("resume"));
		ps.setString(13, list.get("url"));
		ps.setString(14, list.get("stamp"));
		ps.executeUpdate();
		ps.close();
		this.con.close();
		
//		Statement st = this.con.createStatement();
//		String sql = "insert into job (school,title,name,salary,position,education,property,num,time,interview,requires,resume,url) values('"+list.get("school")+"','"+list.get("title")+"','"+list.get("name")+"','"+list.get("salary")+"','"+list.get("position")+"','"+list.get("education")+"','"+list.get("property")+"','"+list.get("num")+"','"+list.get("time")+"','"+list.get("interview")+"','"+list.get("require")+"','"+list.get("resume")+"','"+list.get("url")+"')";
//		System.out.println(sql);
//		st.execute(sql);
		
	}
		  
}
