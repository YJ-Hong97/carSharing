package model;

import java.io.BufferedOutputStream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import csException.LoginException;
import dbutil.DBUtil;
import dto.CSDTO;

public class CSDAO {
	DBUtil dbUtil = new DBUtil();
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	CallableStatement cst = null;
	Statement st = null;

	// 로그인
	static final String SQL_PASSWORD = "select password from customer where user_id = ?";
	// 회원 가입
	static final String SQL_INSERT_CUSTOMER = "insert into customer values(?,?,?,?,?)";
	// 회원 탈퇴
	static final String SQL_DELETE_CUSTOMER = "delete from customer where user_id = ?";
	// 예약 하기
	static final String SQL_INSERT_SS = "insert into sharing_Service values(service_seq.nextval,?,?,null,null,?,null,null,null,1,?,?)";
	static final String SQL_INSERT_FEE = "insert into fee values(null,null,null,null,null,?)";
	// 서비스 아이디 조회
	static final String SQL_SELECT_SERVICE_ID = "select service_id from sharing_service where user_id  = ? and state = 1";
	// 예약 확인
	static final String SQL_SELECT_SS_BY_ID = "select * from sharing_service where user_id = ? and state = 1 ";
	// 추가 시간
	static final String SQL_UPDATE_ADD_TIME = "update sharing_service set addtional_rental_time = ? where user_id = ? and state = 1";
	//// customer SQL/////
	static final String SQL_SELECT_CUSTOMER = "select * from customer where user_id= ?";
	static final String SQL_SELECT_BY_PHONE = "select * from customer where phone_number = ?";
	static final String SQL_CALL_LICENSE_PROCEDURE = "{call driving_license(?)}";

	
	
	//// 대여 관련 SQL/////
	static final String SQL_SELECT_CAR_BY_ZONE = "select * from car where zone_id = ?";
	static final String SQL_SELECT_ZONEID_BY_CITY = "select zone_id from parking_zone where city_name = LIKE('%'||?||'%')";
	static final String SQL_SELECT_ZONEID_BY_LOCATION = "select zone_id from parking_zone where location_name = LiKE('%'||?||'%')";

	// 톨비 추가 함수 호출
	static final String SQL_CALL_PROCEDURE_TOLL_FEE = "{call cal_toll_fee(?,?,?)}";
	// 추가시간 sql
	static final String SQL_UPDATE_ADDIONAL_FEE = "{call update_additional_fee(?)}";
	// 총비용
	static final String SQL_UPDATE_TOTAL_FEE = "{call cal_total_fee(?,?)}";

	// 주행비용
	static final String SQL_UPDATE_DRIVING_FEE = "{call update_driving_fee(?)}";
	// 주행거리
	static final String SQL_UPDATE_DRIVE_DISTANCE = "update sharing_service set distance_drive = ? where service_id = ?";

	// 대여 -> fee

	// 반납
	static final String SQL_UPDATE_STATE = "update sharing_service set state = 0 where service_id = ?";

	// 기본 시간
	static final String SQL_UPDATE_RENTAL_FEE = "{call update_rental_fee(?)}";
	// 초기 보험
	static final String SQL_UPDATE_INIT_INSURANCE = "{call cal_initial_insurance_fee(?)}";
	// 추가 보험
	static final String SQL_UPDATE_ADD_INSURANCE = "{call cal_add_insurance_fee(?)}";
	// 차종에 따른 보험료 보여주기
	static final String SQL_SELECT_TBL_INSURANCE_FEE = "select * from tbl_insurance_fee where car_type = ?";
	static final String SQL_UPDATE_RETURN_ZONE_ID = "update sharing_service set return_zone_id = ? where user_id = ? and state = 1";
	// toll_id update
	static final String SQL_UPDATE_TOLL_ID = "update sharing_service set toll_id = ? where phone_number = ? and state = 1";

	static final String SQL_UPDATE_ZONE_ID = "update car set zone_id = null where car_id = ?";
	//회원정보 변경하기
	private static final String SQL_UPDATE_NAME = "update customer set customer_name = ? where user_id = ?";
	static final String SQL_UPDATE_PHONE = "update customer set phone_number = ? where user_id = ?";
	private static final String SQL_UPDATE_ID = "update customer set user_id = ? where user_id = ?";
	private static final String SQL_SS_UPDATE_ID = "update sharing_service set user_id = ? where user_id =?";
	private static final String SQL_UPDATE_PW = "update customer set password = ? where user_id = ?";
	static final String SQL_UPDATE_LICENSE = "update customer set driver_license = ? where user_id = ?";
////////////////////로그인////////////////////////////
	public boolean login(String user_id, String password) {
		CSDTO csdto = new CSDTO();
		conn = dbUtil.getConnection();
		boolean sucess = false;
		try {
			FileReader fileReader = new FileReader("./src/private.key");
			BufferedReader br = new BufferedReader(fileReader);
			String privateKey = br.readLine();
			pst = conn.prepareStatement(SQL_PASSWORD);
			pst.setString(1, user_id);
			rs = pst.executeQuery();
			if (rs.next() == false) {
				throw new LoginException("회원정보가 일치하지 않습니다.");
			}
			String encryptPW = rs.getString(1);
			String decryptPW = RSA.decode(encryptPW, privateKey);
			if (password.equals(decryptPW))
				sucess = true;
		} catch (LoginException e) {
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbUtil.dbClose(rs, null, pst, null, conn);
		}
		return sucess;
	}

/////////////////회원가입/////////////////////////////
	public int customerInsert(CSDTO customer) {
		int ret = 0;
		CSDTO csdto = new CSDTO();
		conn = dbUtil.getConnection();
		try {
			pst = conn.prepareStatement(SQL_INSERT_CUSTOMER);
			String phone = customer.getPhone_number();
			String name = customer.getCustomer_name();
			String license = customer.getDriver_license();
			String userId = customer.getUser_id();
			String password = customer.getPassword();

			pst.setString(1, phone);
			pst.setString(2, name);
			pst.setString(3, license);
			pst.setString(4, userId);
			// 비밀번호 암호화
			HashMap<String, String> keyMap = RSA.createKeypairAsString();
			String publicKey = keyMap.get("publicKey");
			String privatekey = keyMap.get("privateKey");
			// 키 저장
			RSA.saveKeyFair(keyMap, "./src");
			String encryptPW = RSA.encode(password, publicKey);
			pst.setString(5, encryptPW);

			ret = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("이미 존재하는 회원입니다.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbUtil.dbClose(null, null, pst, null, conn);
		}
		return ret;
	}

	private CSDTO makeCustomer(ResultSet rs2) {
		CSDTO customer = new CSDTO();

		try {
			customer.setPhone_number(rs2.getString(1));
			customer.setCustomer_name(rs2.getString(2));
			customer.setDriver_license(rs2.getString(3));
			customer.setUser_id(rs2.getString(4));
			customer.setPassword(rs2.getString(5));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return customer;
	}

///탈퇴/////////////////////////////////////////////////////////////////////////
	public void deleteCustomer(String userId, String PW) {
		int ret = 0;
		CSDTO csdto = new CSDTO();
		conn = dbUtil.getConnection();
		try {
			pst = conn.prepareStatement(SQL_PASSWORD);
			pst.setString(1, userId);
			rs = pst.executeQuery();
			rs.next();
			String PW2 = rs.getString(1);
			// 비번 복호화
			FileReader fr = new FileReader("./src/private.key");
			BufferedReader br = new BufferedReader(fr);
			String privateKey = br.readLine();
			String decryptPW = RSA.decode(PW2, privateKey);

			// 비번 확인
			if (decryptPW.equals(PW)) {
				pst = conn.prepareStatement(SQL_DELETE_CUSTOMER);
				pst.setString(1, userId);
				pst.executeUpdate();
				System.out.println("탈퇴가 성공했습니다.");

			} else {
				System.out.println("회원정보가 일치하지 않습니다.");
			}
		} catch (SQLException e) {
			System.out.println("회원정보가 일치하지 않습니다.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbUtil.dbClose(rs, null, pst, null, conn);
		}
	}

	/// 예약하기
	/// ///////////////////////////////////////////////////////////////////////////////////////////
	// insert ss
	public int ssInsert(CSDTO ss) {
		int ret = 0;
		conn = dbUtil.getConnection();
		try {
			pst = conn.prepareStatement(SQL_INSERT_SS);
			pst.setInt(1, ss.getCar_id());
			pst.setDouble(2, ss.getInitial_rental_time());
			pst.setInt(3, ss.getRental_zone_id());
			pst.setString(4, ss.getInsurance());
			pst.setString(5, ss.getUser_id());
			ret = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbUtil.dbClose(null, null, pst, null, conn);
		}
		return ret;
	}

	// insert fee
	public void insert_fee(String userId) {
		conn = dbUtil.getConnection();
		try {
			pst = conn.prepareStatement(SQL_SELECT_SERVICE_ID);
			pst.setString(1, userId);
			rs = pst.executeQuery();
			rs.next();
			int serviceId = rs.getInt(1);

			pst = conn.prepareCall(SQL_INSERT_FEE);
			pst.setInt(1, serviceId);
			pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbUtil.dbClose(null, null, pst, null, conn);
		}

	}

	// update rental fee
	public void updateRentalFee(String userId) {
		conn = dbUtil.getConnection();
		try {
			pst = conn.prepareStatement(SQL_SELECT_SERVICE_ID);
			pst.setString(1, userId);
			rs = pst.executeQuery();
			rs.next();
			int serviceId = rs.getInt(1);

			cst = conn.prepareCall(SQL_UPDATE_RENTAL_FEE);
			cst.setInt(1, serviceId);
			cst.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbUtil.dbClose(rs, null, pst, cst, conn);
		}
	}

	// 보험
	public void updateInitInsurance(String userId) {
		conn = dbUtil.getConnection();
		try {
			pst = conn.prepareStatement(SQL_SELECT_SERVICE_ID);
			pst.setString(1, userId);
			rs = pst.executeQuery();
			rs.next();
			int serviceId = rs.getInt(1);

			cst = conn.prepareCall(SQL_UPDATE_INIT_INSURANCE);
			cst.setInt(1, serviceId);
			cst.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbUtil.dbClose(rs, null, pst, cst, conn);
		}
	}

	// 주차장 정보
	// 주차장 검색
	public List<CSDTO> pzSelectBy(String column, String value) {
		conn = dbUtil.getConnection();
		String sql = "select * from parking_zone where " + column + " LIKE '%" + value + "%'";
		List<CSDTO> cslist = new ArrayList<CSDTO>();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				CSDTO customer = makePz(rs);
				cslist.add(customer);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbUtil.dbClose(rs, st, null, null, conn);
		}
		return cslist;
	}

	private CSDTO makePz(ResultSet rs2) {
		CSDTO pz = new CSDTO();
		try {
			pz.setZone_id(rs2.getInt(1));
			pz.setParking_space(rs2.getInt(2));
			pz.setRemaining_space(rs2.getInt(3));
			pz.setLocation_name(rs2.getString(4));
			pz.setCity_name(rs2.getString(5));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pz;
	}

	// 주자장에 있는 차 조회
	public List<CSDTO> selectCarByZone(int zoneId) {
		List<CSDTO> carlist = new ArrayList<CSDTO>();
		conn = dbUtil.getConnection();
		try {
			pst = conn.prepareStatement(SQL_SELECT_CAR_BY_ZONE, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			pst.setInt(1, zoneId);
			rs = pst.executeQuery();
			if (rs.next() == false) {
				System.out.println("해당 주차장에 자동차가 없습니다.");
			} else {
				rs.previous();
				while (rs.next()) {
					CSDTO car = makecar(rs);
					carlist.add(car);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbUtil.dbClose(rs, null, pst, null, conn);
		}
		return carlist;
	}

	private CSDTO makecar(ResultSet rs2) {
		CSDTO car = new CSDTO();
		try {
			car.setCar_id(rs2.getInt(1));
			car.setCar_type(rs2.getString(2));
			car.setCar_model(rs2.getString(3));
			car.setCar_color(rs2.getString(4));
			car.setResidual_fuel(rs2.getInt(5));
			car.setZone_id(rs2.getInt(6));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return car;
	}

	public List<CSDTO> selectInsurance(int carId) {
		List<CSDTO> insuranceList = new ArrayList<CSDTO>();
		conn = dbUtil.getConnection();
		try {
			pst = conn.prepareStatement("select car_type from car where car_id = ?");
			pst.setInt(1, carId);
			rs = pst.executeQuery();
			rs.next();
			pst = conn.prepareStatement(SQL_SELECT_TBL_INSURANCE_FEE);
			pst.setString(1, rs.getString(1));
			rs = pst.executeQuery();
			while (rs.next()) {
				CSDTO insurance = makeInsurance(rs);
				insuranceList.add(insurance);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbUtil.dbClose(rs, null, pst, null, conn);
		}
		return insuranceList;
	}
	private CSDTO makeInsurance(ResultSet rs2) {
		CSDTO insurance = new CSDTO();
		try {
			insurance.setCar_type(rs2.getString(1));
			insurance.setHigh_self_pay(rs2.getInt(2));
			insurance.setMedium_self_pay(rs2.getInt(3));
			insurance.setLow_self_pay(rs2.getInt(4));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return insurance;
	}


	
	/// 예약확인////////////////////////////////////////////////////////////////////
	public List<CSDTO> ssSelectById(String Id) {
		List<CSDTO> ssList = new ArrayList<CSDTO>();
		conn = dbUtil.getConnection();
		try {
			pst = conn.prepareStatement(SQL_SELECT_SS_BY_ID);
			pst.setString(1, Id);
			rs = pst.executeQuery();
			while (rs.next()) {
				CSDTO ss = makess(rs);
				ssList.add(ss);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbUtil.dbClose(rs, null, pst, null, conn);
		}
		return ssList;
	}

	private CSDTO makess(ResultSet rs2) {
		CSDTO ss = new CSDTO();
		try {
			ss.setService_id(rs2.getInt(1));
			ss.setCar_id(rs2.getInt(2));
			ss.setInitial_rental_time(rs2.getDouble(3));
			ss.setAddtional_rental_time(rs2.getDouble(4));
			ss.setDistance_drive(rs2.getDouble(5));
			ss.setRental_zone_id(rs2.getInt(6));
			ss.setReturn_zone_id(rs2.getInt(7));
			ss.setTotal_fee(rs2.getInt(8));
			ss.setToll_id(rs2.getInt(9));
			ss.setState(rs2.getInt(10));
			ss.setInsurance(rs2.getString(11));
			ss.setUser_id(rs2.getString(12));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ss;
	}

	/// 시간 추가///////////////////////////////////////////////////////////////
	public void updateAddTime(double time, String userId) {
		conn = dbUtil.getConnection();
		try {
			pst = conn.prepareStatement(SQL_UPDATE_ADD_TIME);
			pst.setDouble(1, time);
			pst.setString(2, userId);
			pst.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbUtil.dbClose(null, null, pst, null, conn);
		}

	}
	public void updateAddInsurance(String customerId) {
		Connection conn = dbUtil.getConnection();
		try {
			pst = conn.prepareStatement(SQL_SELECT_SERVICE_ID);
			pst.setString(1, customerId);
			rs = pst.executeQuery();
			rs.next();
			int serviceId = rs.getInt(1);
			cst = conn.prepareCall(SQL_UPDATE_ADD_INSURANCE);
			cst.setInt(1, serviceId);
			cst.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbUtil.dbClose(rs, null, pst, cst, conn);
		}
	}
	public void updateAddFee(String cusotmerId) {
		Connection conn = dbUtil.getConnection();
		try {
			pst = conn.prepareStatement(SQL_SELECT_SS_BY_ID);
			pst.setString(1, cusotmerId);
			rs = pst.executeQuery();
			rs.next();
			cst = conn.prepareCall(SQL_UPDATE_ADDIONAL_FEE);
			cst.setInt(1, rs.getInt(1));
			cst.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbUtil.dbClose(rs, null, pst, cst, conn);
		}

	}
	///반납하기/////////////////////////////////////////////////////////////////////////
	public void returnCar(String Id) {
		Connection conn =dbUtil.getConnection();
		int ret = 0;
		try {
			cst = conn.prepareCall(SQL_SELECT_SERVICE_ID);
			cst.setString(1, Id);
			rs = cst.executeQuery();
			rs.next();
			int serviceId = rs.getInt(1);
			cst = conn.prepareCall(SQL_UPDATE_STATE);
			cst.setInt(1, serviceId);
			ret = cst.executeUpdate();
			System.out.println(ret==1?"반납되었습니다":"반납이 실패했습니다.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbUtil.dbClose(rs, null, null, cst, conn);
		}

	}
	public void updateReturnZone(String customerId, int zoneId) {
		Connection conn =dbUtil.getConnection();
		try {
			pst = conn.prepareStatement(SQL_UPDATE_RETURN_ZONE_ID);
			pst.setInt(1, zoneId);
			pst.setString(2, customerId);
			pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbUtil.dbClose(null, null, pst, null, conn);
		}

	}
	// 주행거리
		public void updateDriveDistance(double d, String customerId) {
			Connection conn =dbUtil.getConnection();
			try {
				pst = conn.prepareStatement(SQL_SELECT_SERVICE_ID);
				pst.setString(1, customerId);
				rs = pst.executeQuery();
				rs.next();
				int serviceId = rs.getInt(1);
				pst = conn.prepareCall(SQL_UPDATE_DRIVE_DISTANCE);
				pst.setDouble(1, d);
				pst.setInt(2, serviceId);
				pst.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				dbUtil.dbClose(rs, null, pst, null, conn);
			}
		}

		public void updateDriveFee(String customerId) {
			Connection conn =dbUtil.getConnection();
			try {
				pst = conn.prepareCall(SQL_SELECT_SERVICE_ID);
				pst.setString(1, customerId);
				rs = pst.executeQuery();
				rs.next();
				int serviceId = rs.getInt(1);
				cst = conn.prepareCall(SQL_UPDATE_DRIVING_FEE);
				cst.setInt(1, serviceId);
				cst.executeQuery();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				dbUtil.dbClose(rs, null, pst, cst, conn);
			}

		}
		// 총비용
		public int updateTotalFee(String customerId) {
			Connection conn =dbUtil.getConnection();
			int totalFee = 0;
			try {
				pst = conn.prepareStatement(SQL_SELECT_SERVICE_ID);
				pst.setString(1, customerId);
				rs = pst.executeQuery();
				rs.next();
				int serviceId = rs.getInt(1);
				cst = conn.prepareCall(SQL_UPDATE_TOTAL_FEE);
				cst.setInt(1, serviceId);
				cst.registerOutParameter(2, Types.NUMERIC);
				cst.execute();
				totalFee = cst.getInt(2);

			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally {
				dbUtil.dbClose(rs, null, pst, cst, conn);
			}

			return totalFee;
		}

	// 대여후 zone null
	public void updateZoneId(int car_id) {
		DBUtil dbUtil = new DBUtil();
		Connection conn = dbUtil.getConnection();
		PreparedStatement pst = null;
		try {
			pst = conn.prepareStatement(SQL_UPDATE_ZONE_ID);
			pst.setInt(1, car_id);
			pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbUtil.dbClose(null, null, pst, null, conn);
		}
	}

	

	// 회원 조회
	// 모두 조히
	public List<CSDTO> customerSelectAll() {
		List<CSDTO> cslist = new ArrayList<CSDTO>();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(SQL_SELECT_CUSTOMER);
			while (rs.next()) {
				CSDTO customer = makeCustomer(rs);
				cslist.add(customer);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cslist;
	}

	// 전화번호로 조회
	public List<CSDTO> customerSelectByPhone(String phone) {
		List<CSDTO> cslist = new ArrayList<CSDTO>();
		try {
			pst = conn.prepareStatement(SQL_SELECT_BY_PHONE);
			pst.setString(1, phone);
			rs = pst.executeQuery();
			while (rs.next()) {
				CSDTO customer = makeCustomer(rs);
				cslist.add(customer);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cslist;

	}

	// 면허 조회
	public String customerSelectByLicense(String phone) {
		String license = null;
		try {
			cst = conn.prepareCall(SQL_CALL_LICENSE_PROCEDURE);
			cst.setString(1, phone);
			cst.execute();
			license = cst.getString(2);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return license;
	}

	// 회원 정보 변경
	// update 전화번호
	public void customerUpdatePhone(String phone, String license) {
		int ret = 0;
		try {
			pst = conn.prepareStatement(SQL_UPDATE_PHONE);
			pst.setString(1, phone);
			pst.setString(2, license);
			ret = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(ret == 1 ? "변경되었습니다." : "에러");
	}

	// update 면허
	public void customerUpdateLicense(String license, String phone) {
		int ret = 0;
		try {
			pst = conn.prepareStatement(SQL_UPDATE_LICENSE);
			pst.setString(1, license);
			pst.setString(2, phone);
			ret = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(ret == 1 ? "변경되었습니다." : "에러");
	}

	

	

	private CSDTO makefee(ResultSet rs2) {
		CSDTO fee = new CSDTO();
		try {
			fee.setRental_fee(rs2.getDouble(1));
			fee.setAddtional_fee(rs2.getDouble(2));
			fee.setDriving_fee(rs2.getDouble(3));
			fee.setInsurance_fee(rs2.getDouble(4));
			fee.setToll_fee(rs2.getDouble(5));
			fee.setService_id(rs2.getInt(6));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fee;
	}

	

	/// 톨 프로시저 호출
	public void callCalToll(CSDTO ss) {
		try {

			cst = conn.prepareCall(SQL_CALL_PROCEDURE_TOLL_FEE);
			cst.setInt(1, ss.getToll_id());
			cst.setInt(2, ss.getService_id());
			cst.setInt(3, ss.getCar_id());
			cst.executeQuery();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateName(String newName, String customerId) {
		// TODO Auto-generated method stub
		 conn = dbUtil.getConnection();
		int ret = 0;
		try {
			pst = conn.prepareStatement(SQL_UPDATE_NAME);
			pst.setString(1, newName);
			pst.setString(2, customerId);
			ret = pst.executeUpdate();
			System.out.println(ret==1? "이름을 변경했습니다.":"변경에 실패했습니다.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbUtil.dbClose(null, null, pst, null, conn);
		}
	}

	public void updatePhone(String newPhone, String customerId) {
		 conn = dbUtil.getConnection();
		int ret = 0;
		try {
			pst = conn.prepareStatement(SQL_UPDATE_PHONE);
			pst.setString(1, newPhone);
			pst.setString(2, customerId);
			ret = pst.executeUpdate();
			System.out.println(ret ==1? "전화번호가 변경되었습니다.":"변경에 실패했습니다.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbUtil.dbClose(null, null, pst, null, conn);
		}
	}

	public void updatePW(String newPW, String customerId) {
		 conn = dbUtil.getConnection();
		int ret = 0;
		try {
			// 비밀번호 암호화
			HashMap<String, String> keyMap = RSA.createKeypairAsString();
			String publicKey = keyMap.get("publicKey");
			String privatekey = keyMap.get("privateKey");
			// 키 저장
			RSA.saveKeyFair(keyMap, "./src");
			String encryptPW = RSA.encode(newPW, publicKey);
			
			pst = conn.prepareStatement(SQL_UPDATE_PW);
			pst.setString(1, encryptPW);
			pst.setString(2, customerId);
			ret = pst.executeUpdate();
			System.out.println(ret==1?"비밀번호가 변경되었습니다.":"변경에 실패했습니다.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbUtil.dbClose(null, null, pst, null, conn);
		}
	}

	public void updateLicense(String newLicense, String customerId) {
		 conn = dbUtil.getConnection();
		 int ret = 0;
		 try {
			pst = conn.prepareStatement(SQL_UPDATE_LICENSE);
			pst.setString(1, newLicense);
			pst.setString(2, customerId);
			ret = pst.executeUpdate();
			System.out.println(ret==1? "면허가 변경되었습니다.":"변경에 실패했습니다.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbUtil.dbClose(null, null, pst,null, conn);
		}
		
	}
	///회원 정보 조회/////////////////////////////////////////////////////////
	public List<CSDTO> selectCustomer(String customerId) {
		conn = dbUtil.getConnection();
		List<CSDTO> customerList = new ArrayList<>();
		try {
			pst = conn.prepareStatement(SQL_SELECT_CUSTOMER);
			pst.setString(1, customerId);
			rs = pst.executeQuery();
			while(rs.next()) {
				CSDTO customer = makeCustomer(rs);
				customerList.add(customer);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customerList;
	}

	

	

}
