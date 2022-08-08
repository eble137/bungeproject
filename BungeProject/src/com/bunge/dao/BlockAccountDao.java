package com.bunge.dao;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
public class BlockAccountDao {
	// true:신고된 것. false:신고 안된 계좌.
	// 계좌조회
	public boolean getBlockAccountList(String account_number) throws SQLException, ClassNotFoundException {
		//String driver = "oracle.jdbc.driver.OracleDriver";
		//String url = "jdbc:oracle:thin:@localhost:1521:xe";
		//String dbID = "tm1234";
		//String dbPW = "1234";
		
		//Class.forName(driver);
		//Connection conn = DriverManager.getConnection(url, dbID, dbPW);
		Connection conn = DBConnection.getConnection();
		String sql = "SELECT COUNT(*) FROM one_to_one_inquiry o, profile p, profile p2 WHERE o.report_nickname_fk = p.nickname_pk AND o.reported_nickname_fk = p2.nickname_pk AND p2.account_number=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, account_number);
		ResultSet rs = pstmt.executeQuery();
		
		int result = 0;
		if(rs.next()){
			result = rs.getInt("count(*)");
		}
		rs.close();
		pstmt.close();
		return result>=1;
	}
}
