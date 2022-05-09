package model;


import java.util.List;

import DTO.CSDTO;

public class CSService {
	CSDAO csdao = new CSDAO();
	//회원가입
	public int customerInsert(CSDTO customer) {
		return csdao.customerInsert(customer);
	}
	
	// 면허 조회
	public String customerSelectByLicense(String phone) {
		return csdao.customerSelectByLicense(phone);
	}
	//고객 정보 변경
	//update 전화번호
	public void customerUpdatePhone(String phone, String license) {
		csdao.customerUpdatePhone(phone, license);
	}

	// update 면허
	public void customerUpdateLicense(String license, String phone) {
		csdao.customerUpdateLicense(license, phone);
	}
	//예약확인
		//ssselect
	public List<CSDTO> ssSelectById(String id){
		return csdao.ssSelectById(id);
	}
	//예약하기
		//ssinsert
	
		//총 비용 보여주기
	public int updateTotalFee(String customerId) {
		return csdao.updateTotalFee(customerId);
	}
		//차 반납
	public void returnCar(String Id) {
		csdao.returnCar(Id);
		
	}
	
	//예약하기
		//insert ss
	public int ssInsert(CSDTO ss) {
		 return csdao.ssInsert(ss);
	}
		//주차장 선텍
	public List<CSDTO> pzSelectBy(String column, String value){
		return csdao.pzSelectBy(column, value);
	}
		//자동차 선택
	
		//시간 선택
	//추가시간
	public void updateAddTime(double time, String userId) {
		csdao.updateAddTime(time, userId);
	}
	public void updateAddFee(String customerId) {
		csdao.updateAddFee(customerId);
	}
	//기본시간
	public void updateRentalFee(String userId) {
		csdao.updateRentalFee(userId);
	}
		//보험 선택
	public void updateInitInsurance(String userId) {
		csdao.updateInitInsurance(userId);
	}
	public void updateAddInsurance(String customerId) {
		csdao.updateAddInsurance(customerId);
	}
	//비용
		//insertfee
	public void insert_fee(String userId) {
		csdao.insert_fee(userId);
		
	}
		//톨게이트
	public void callCalToll(CSDTO ss) {
		csdao.callCalToll(ss);
	}
		//주행거리
	public void updateDriveDistance(double d, String customerId) {
		csdao.updateDriveDistance(d, customerId);
	}
	public void updateDriveFee(String customerId) {
		csdao.updateDriveFee(customerId);
	
	}
	
	
	//로그인
		public boolean login(String phone, String name) {
			return csdao.login(phone, name);
		}
		
		//주자장에 있는 차 조회
		public List<CSDTO> selectCarByZone(int zoneId){
			return csdao.selectCarByZone(zoneId);
		}
		public List<CSDTO> selectInsurance(int carId){
			return csdao.selectInsurance(carId);
		}
		
		public void updateReturnZone(String customerId, int zoneId) {
			csdao.updateReturnZone(customerId, zoneId);
		}
//		public void updateTollId(String phone_number, CSDTO ss) {
//			
//			csdao.updateTollId(phone_number, ss);
//		}
		public void updateZoneId(int car_id) {
			csdao.updateZoneId(car_id);
		}
		public void deleteCustomer(String phone, String name) {
			csdao.deleteCustomer(phone, name);
		}
		///회원 정보 변경하기///////////////////////////////////////////////////////////
		public void updateName(String newName, String customerId) {
			csdao.updateName(newName, customerId);
		}
		public void updatePhone(String newPhone, String customerId) {
			csdao.updatePhone(newPhone, customerId);
		}
		public void updateId(String newId, String customerId) {
			csdao.updateId(newId, customerId);
		}
		public void updatePW(String newPW, String customerId) {
			csdao.updatePW(newPW,customerId);
		}
		public void updateLicense(String newLicense, String customerId) {
			csdao.updateLicense(newLicense,customerId);
		}
		public List<CSDTO> selectCustomer(String customerId) {
			return csdao.selectCustomer(customerId);
		}
}
