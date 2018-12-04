package DhApi;

import java.sql.*;
import java.util.List;
import java.util.Locale;


public class SqlModule {


	public SqlModule() {
	}

	public static void setDbUrl(String DbName) {
        DB_URL = "jdbc:mysql://192.168.1.30:3306/" + DbName;
        DB = DbName;
    }

	// JDBC 驱动名及数据库 URL
	// static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //static final String DB_URL = "jdbc:mysql://localhost:3306/sdg_new";
    //static String DB_URL = "jdbc:mysql://172.19.2.12:3306/sdg_new";
    static String DB_URL = "";

	// 数据库的用户名与密码，需要根据自己的设置
	static String USER = "develop";
	static String PASS = "RBr7BPsVPPdlRvD6";
	public static String DB = "";


    /**
     *
     * 插入一条定位信息
     *
     * @param sim_id
     * @param car_no
     * @param gps_time
     * @param longitude
     * @param latitude
     * @param speed
     * @param angle
     */
	public void insertGpsInfo(String sim_id, String car_no, String gps_time, double longitude, double latitude, float speed, float angle) {
		Connection conn = null;
		try {
			// 注册 JDBC 驱动
			Class.forName("com.mysql.jdbc.Driver");

			// 打开链接
			//System.out.println("getConnection...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			// 执行查询
			//System.out.println(" createStatement...");

            String sql = "INSERT INTO `" + DB + "`.`report_mileage_today_realtime` " +
                    "(`sim_id`, `car_no`, `gps_time`, `longitude`, `latitude`, `speed`, `angle`) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";

			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, sim_id);
			ps.setString(2, car_no);
			ps.setString(3, gps_time);
			ps.setDouble(4, longitude);
			ps.setDouble(5, latitude);
			ps.setFloat(6, speed);
			ps.setFloat(7, angle);

			int result = ps.executeUpdate();
			System.out.println("result: " + result);

			ps.close();
			conn.close();
		} catch (SQLException se) {
			// 处理 JDBC 错误
			se.printStackTrace();
		} catch (Exception e) {
			// 处理 Class.forName 错误
			e.printStackTrace();
		} finally {
			// 关闭资源
			try {
				if (conn != null) {
                    conn.close();
                }
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

}
