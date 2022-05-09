package model;


import java.util.List;

import DTO.CSDTO;

public class CSService {
	CSDAO csdao = new CSDAO();
	//ȸ������
	public int customerInsert(CSDTO customer) {
		return csdao.customerInsert(customer);
	}
	
	// ���� ��ȸ
	public String customerSelectByLicense(String phone) {
		return csdao.customerSelectByLicense(phone);
	}
	//�� ���� ����
	//update ��ȭ��ȣ
	public void customerUpdatePhone(String phone, String license) {
		csdao.customerUpdatePhone(phone, license);
	}

	// update ����
	public void customerUpdateLicense(String license, String phone) {
		csdao.customerUpdateLicense(license, phone);
	}
	//����Ȯ��
		//ssselect
	public List<CSDTO> ssSelectById(String id){
		return csdao.ssSelectById(id);
	}
	//�����ϱ�
		//ssinsert
	
		//�� ��� �����ֱ�
	public int updateTotalFee(String customerId) {
		return csdao.updateTotalFee(customerId);
	}
		//�� �ݳ�
	public void returnCar(String Id) {
		csdao.returnCar(Id);
		
	}
	
	//�����ϱ�
		//insert ss
	public int ssInsert(CSDTO ss) {
		 return csdao.ssInsert(ss);
	}
		//������ ����
	public List<CSDTO> pzSelectBy(String column, String value){
		return csdao.pzSelectBy(column, value);
	}
		//�ڵ��� ����
	
		//�ð� ����
	//�߰��ð�
	public void updateAddTime(double time, String userId) {
		csdao.updateAddTime(time, userId);
	}
	public void updateAddFee(String customerId) {
		csdao.updateAddFee(customerId);
	}
	//�⺻�ð�
	public void updateRentalFee(String userId) {
		csdao.updateRentalFee(userId);
	}
		//���� ����
	public void updateInitInsurance(String userId) {
		csdao.updateInitInsurance(userId);
	}
	public void updateAddInsurance(String customerId) {
		csdao.updateAddInsurance(customerId);
	}
	//���
		//insertfee
	public void insert_fee(String userId) {
		csdao.insert_fee(userId);
		
	}
		//�����Ʈ
	public void callCalToll(CSDTO ss) {
		csdao.callCalToll(ss);
	}
		//����Ÿ�
	public void updateDriveDistance(double d, String customerId) {
		csdao.updateDriveDistance(d, customerId);
	}
	public void updateDriveFee(String customerId) {
		csdao.updateDriveFee(customerId);
	
	}
	
	
	//�α���
		public boolean login(String phone, String name) {
			return csdao.login(phone, name);
		}
		
		//�����忡 �ִ� �� ��ȸ
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
		///ȸ�� ���� �����ϱ�///////////////////////////////////////////////////////////
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
