package DTO;

import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;


import dbutil.DBUtil;

public class tollInsert {


	public static void main(String[] args) throws FileNotFoundException, SQLException {
		
		String line = null;
		String sql = "insert into toll values(toll_seq.nextval,?,?,?,?,?,?,?,?)";
		DBUtil dbutil = new DBUtil();
		Connection conn = dbutil.getConnection();
		PreparedStatement pst = conn.prepareStatement(sql);
		int ret=0;
		int count = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/hhhyj/Downloads/한국도로공사_통행요금조회_20211201.csv"));){
			while ((line=reader.readLine())!=null ) {
				if(count != 0) {
					
					String[] arr = line.split(",");
					pst.clearParameters();
					pst.setString(1, arr[0]);
					pst.setString(2, arr[1]);
					int fee_1 = Integer.parseInt(arr[2]);
					int fee_2 = Integer.parseInt(arr[3]);
					int fee_3 = Integer.parseInt(arr[4]);
					int fee_4 = Integer.parseInt(arr[5]);
					int fee_5 = Integer.parseInt(arr[6]);
					int fee_light = Integer.parseInt(arr[7]);
					pst.setInt(3, fee_1);
					pst.setInt(4, fee_2);
					pst.setInt(5, fee_3);
					pst.setInt(6, fee_4);
					pst.setInt(7, fee_5);
					pst.setInt(8, fee_light);
					ret = pst.executeUpdate();
					System.out.println(ret);
				}
				count++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbutil.dbClose(null,null, pst,null, conn);
		}
				
	}

}
