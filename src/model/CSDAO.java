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

	// �α���
	static final String SQL_PASSWORD = "select password from customer where user_id = ?";
	// ȸ�� ����
	static final String SQL_INSERT_CUSTOMER = "insert into customer values(?,?,?,?,?)";
	// ȸ�� Ż��
	static final String SQL_DELETE_CUSTOMER = "delete from customer where user_id = ?";
	// ���� �ϱ�
	static final String SQL_INSERT_SS = "insert into sharing_Service values(service_seq.nextval,?,?,null,null,?,null,null,null,1,?,?)";
	static final String SQL_INSERT_FEE = "insert into fee values(null,null,null,null,null,?)";
	// ���� ���̵� ��ȸ
	static final String SQL_SELECT_SERVICE_ID = "select service_id from sharing_service where user_id  = ? and state = 1";
	// ���� Ȯ��
	static final String SQL_SELECT_SS_BY_ID = "select * from sharing_service where user_id = ? and state = 1 ";
	// �߰� �ð�
	static final String SQL_UPDATE_ADD_TIME = "update sharing_service set addtional_rental_time = ? where user_id = ? and state = 1";
	//// customer SQL/////
	static final String SQL_SELECT_CUSTOMER = "select * from customer where user_id= ?";
	static final String SQL_SELECT_BY_PHONE = "select * from customer where phone_number = ?";
	static final String SQL_CALL_LICENSE_PROCEDURE = "{call driving_license(?)}";

	
	
	//// �뿩 ���� SQL/////
	static final String SQL_SELECT_CAR_BY_ZONE = "select * from car where zone_id = ?";
	static final String SQL_SELECT_ZONEID_BY_CITY = "select zone_id from parking_zone where city_name = LIKE('%'||?||'%')";
	static final String SQL_SELECT_ZONEID_BY_LOCATION = "select zone_id from parking_zone where location_name = LiKE('%'||?||'%')";

	// ��� �߰� �Լ� ȣ��
	static final String SQL_CALL_PROCEDURE_TOLL_FEE = "{call cal_toll_fee(?,?,?)}";
	// �߰��ð� sql
	static final String SQL_UPDATE_ADDIONAL_FEE = "{call update_additional_fee(?)}";
	// �Ѻ��
	static final String SQL_UPDATE_TOTAL_FEE = "{call cal_total_fee(?,?)}";

	// ������
	static final String SQL_UPDATE_DRIVING_FEE = "{call update_driving_fee(?)}";
	// ����Ÿ�
	static final String SQL_UPDATE_DRIVE_DISTANCE = "update sharing_service set distance_drive = ? where service_id = ?";

	// �뿩 -> fee

	// �ݳ�
	static final String SQL_UPDATE_STATE = "update sharing_service set state = 0 where service_id = ?";

	// �⺻ �ð�
	static final String SQL_UPDATE_RENTAL_FEE = "{call update_rental_fee(?)}";
	// �ʱ� ����
	static final String SQL_UPDATE_INIT_INSURANCE = "{call cal_initial_insurance_fee(?)}";
	// �߰� ����
	static final String SQL_UPDATE_ADD_INSURANCE = "{call cal_add_insurance_fee(?)}";
	// ������ ���� ����� �����ֱ�
	static final String SQL_SELECT_TBL_INSURANCE_FEE = "select * from tbl_insurance_fee where car_type = ?";
	static final String SQL_UPDATE_RETURN_ZONE_ID = "update sharing_service set return_zone_id = ? where user_id = ? and state = 1";
	// toll_id update
	static final String SQL_UPDATE_TOLL_ID = "update sharing_service set toll_id = ? where phone_number = ? and state = 1";

	static final String SQL_UPDATE_ZONE_ID = "update car set zone_id = null where car_id = ?";
	//ȸ������ �����ϱ�
	private static final String SQL_UPDATE_NAME = "update customer set customer_name = ? where user_id = ?";
	static final String SQL_UPDATE_PHONE = "update customer set phone_number = ? where user_id = ?";
	private static final String SQL_UPDATE_ID = "update customer set user_id = ? where user_id = ?";
	private static final String SQL_SS_UPDATE_ID = "update sharing_service set user_id = ? where user_id =?";
	private static final String SQL_UPDATE_PW = "update customer set password = ? where user_id = ?";
	static final String SQL_UPDATE_LICENSE = "update customer set driver_license = ? where user_id = ?";
////////////////////�α���////////////////////////////
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
				throw new LoginException("ȸ�������� ��ġ���� �ʽ��ϴ�.");
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

/////////////////ȸ������/////////////////////////////
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
			// ��й�ȣ ��ȣȭ
			HashMap<String, String> keyMap = RSA.createKeypairAsString();
			String publicKey = keyMap.get("publicKey");
			String privatekey = keyMap.get("privateKey");
			// Ű ����
			RSA.saveKeyFair(keyMap, "./src");
			String encryptPW = RSA.encode(password, publicKey);
			pst.setString(5, encryptPW);

			ret = pst.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("�̹� �����ϴ� ȸ���Դϴ�.");
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

///Ż��/////////////////////////////////////////////////////////////////////////
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
			// ��� ��ȣȭ
			FileReader fr = new FileReader("./src/private.key");
			BufferedReader br = new BufferedReader(fr);
			String privateKey = br.readLine();
			String decryptPW = RSA.decode(PW2, privateKey);

			// ��� Ȯ��
			if (decryptPW.equals(PW)) {
				pst = conn.prepareStatement(SQL_DELETE_CUSTOMER);
				pst.setString(1, userId);
				pst.executeUpdate();
				System.out.println("Ż�� �����߽��ϴ�.");

			} else {
				System.out.println("ȸ�������� ��ġ���� �ʽ��ϴ�.");
			}
		} catch (SQLException e) {
			System.out.println("ȸ�������� ��ġ���� �ʽ��ϴ�.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbUtil.dbClose(rs, null, pst, null, conn);
		}
	}

	/// �����ϱ�
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

	// ����
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

	// ������ ����
	// ������ �˻�
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

	// �����忡 �ִ� �� ��ȸ
	public List<CSDTO> selectCarByZone(int zoneId) {
		List<CSDTO> carlist = new ArrayList<CSDTO>();
		conn = dbUtil.getConnection();
		try {
			pst = conn.prepareStatement(SQL_SELECT_CAR_BY_ZONE, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			pst.setInt(1, zoneId);
			rs = pst.executeQuery();
			if (rs.next() == false) {
				System.out.println("�ش� �����忡 �ڵ����� �����ϴ�.");
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


	
	/// ����Ȯ��////////////////////////////////////////////////////////////////////
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

	/// �ð� �߰�///////////////////////////////////////////////////////////////
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
	///�ݳ��ϱ�/////////////////////////////////////////////////////////////////////////
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
			System.out.println(ret==1?"�ݳ��Ǿ����ϴ�":"�ݳ��� �����߽��ϴ�.");
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
	// ����Ÿ�
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
		// �Ѻ��
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

	// �뿩�� zone null
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

	

	// ȸ�� ��ȸ
	// ��� ����
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

	// ��ȭ��ȣ�� ��ȸ
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

	// ���� ��ȸ
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

	// ȸ�� ���� ����
	// update ��ȭ��ȣ
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
		System.out.println(ret == 1 ? "����Ǿ����ϴ�." : "����");
	}

	// update ����
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
		System.out.println(ret == 1 ? "����Ǿ����ϴ�." : "����");
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

	

	/// �� ���ν��� ȣ��
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
			System.out.println(ret==1? "�̸��� �����߽��ϴ�.":"���濡 �����߽��ϴ�.");
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
			System.out.println(ret ==1? "��ȭ��ȣ�� ����Ǿ����ϴ�.":"���濡 �����߽��ϴ�.");
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
			// ��й�ȣ ��ȣȭ
			HashMap<String, String> keyMap = RSA.createKeypairAsString();
			String publicKey = keyMap.get("publicKey");
			String privatekey = keyMap.get("privateKey");
			// Ű ����
			RSA.saveKeyFair(keyMap, "./src");
			String encryptPW = RSA.encode(newPW, publicKey);
			
			pst = conn.prepareStatement(SQL_UPDATE_PW);
			pst.setString(1, encryptPW);
			pst.setString(2, customerId);
			ret = pst.executeUpdate();
			System.out.println(ret==1?"��й�ȣ�� ����Ǿ����ϴ�.":"���濡 �����߽��ϴ�.");
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
			System.out.println(ret==1? "���㰡 ����Ǿ����ϴ�.":"���濡 �����߽��ϴ�.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			dbUtil.dbClose(null, null, pst,null, conn);
		}
		
	}
	///ȸ�� ���� ��ȸ/////////////////////////////////////////////////////////
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
