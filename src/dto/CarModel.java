package dto;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dbutil.DBUtil;


public class CarModel {
	public static void main(String[] args) throws IOException, SQLException {
		String url = "https://www.greencar.co.kr/greencar/charge.do";
		Connection conn = Jsoup.connect(url);
		Document doc = conn.get();
		Elements elem = doc.select("div[table id=\"tab_ba\" class=\"board-view2 bg\"]");
		Elements elemtbody = doc.select("tbody");
		int count = 0;
		ArrayList<String> carModelList = new ArrayList<String>();
		ArrayList<String> carTypeList = new ArrayList<String>();
		ArrayList<Integer> basicRentalFeeList = new ArrayList<>();
		ArrayList<Integer> basicDrivingFeeList = new ArrayList<Integer>();
		for(Element e : elemtbody.select("tr")) {
			if(count>=22&&count<=35) {
				String str = e.text();
				String car_type = ""+str.charAt(0)+str.charAt(1)+str.charAt(2);
				String car_model = str.replaceAll("\\d{5}원", "").replaceAll("\\d{3}원", "").replaceAll(car_type, "").trim();
				
				String[] arr = str.replaceAll(car_model, "").replaceAll(car_type, "").replaceAll("원", "").trim().split(" ");
				int basic_rental_fee = Integer.parseInt(arr[arr.length-2]);
				int basic_driving_fee = Integer.parseInt(arr[arr.length-1]);
				
				carModelList.add(car_model);
				carTypeList.add(car_type);
				basicDrivingFeeList.add(basic_driving_fee);
				basicRentalFeeList.add(basic_rental_fee);
			}
			else if(count >=36&&count<=37) {
				String str = e.text();
				String car_type = ""+str.charAt(0)+str.charAt(1);
				String car_model = str.replaceAll("\\d{5}원", "").replaceAll("\\d{3}원", "").replaceAll(car_type, "").trim();
				
				String[] arr = str.replaceAll(car_model, "").replaceAll(car_type, "").replaceAll("원", "").trim().split(" ");
				int basic_rental_fee = Integer.parseInt(arr[arr.length-2]);
				int basic_driving_fee = Integer.parseInt(arr[arr.length-1]);
				
				carModelList.add(car_model);
				carTypeList.add(car_type);
				basicDrivingFeeList.add(basic_driving_fee);
				basicRentalFeeList.add(basic_rental_fee);
			}
			else if(count>=38&&count<42) {
				String str = e.text();
				String car_type = ""+str.charAt(0)+str.charAt(1)+str.charAt(2);
				String car_model = str.replaceAll("\\d{5}원", "").replaceAll("\\d{2}원", "").replaceAll(car_type, "").trim();
				
				String[] arr = str.replaceAll(car_model, "").replaceAll(car_type, "").replaceAll("원", "").trim().split(" ");
				int basic_rental_fee = Integer.parseInt(arr[arr.length-2]);
				int basic_driving_fee = Integer.parseInt(arr[arr.length-1]);
				
				carModelList.add(car_model);
				carTypeList.add(car_type);
				basicDrivingFeeList.add(basic_driving_fee);
				basicRentalFeeList.add(basic_rental_fee);
			}
			
			count++;
		}
		System.out.println("-------------------");
		System.out.println(carModelList.size());
		System.out.println(carTypeList.size());
		System.out.println(basicDrivingFeeList.size());
		System.out.println(basicRentalFeeList.size());
		DBUtil dbUtil = new DBUtil();
		java.sql.Connection conn2 = dbUtil.getConnection();
		String sql = "insert into tbl_car_model values(?,?,?,?)";
		PreparedStatement pst = conn2.prepareStatement(sql);
		int ret =0;
		for(int i =0; i<20; i++) {
			pst.clearParameters();
			pst.setString(1, carModelList.get(i));
			pst.setString(2, carTypeList.get(i));
			pst.setInt(3, basicRentalFeeList.get(i));
			pst.setInt(4, basicDrivingFeeList.get(i));
			ret = pst.executeUpdate();
			System.out.println(ret);
		}
	}
}
