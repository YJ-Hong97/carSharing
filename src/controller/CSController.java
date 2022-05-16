package controller;

import java.util.Scanner;
import java.util.regex.Pattern;

import csException.SignUpException;
import dto.CSDTO;
import model.CSService;
import view.CSVeiw;

public class CSController {
	static CSService csService = new CSService();
	static CSVeiw csVeiw = new CSVeiw();
	static Scanner scanner = new Scanner(System.in);
	static String customerId;
	public static void main(String[] args) {
		boolean flag = true;
		
		while(flag) {
			displayFirst();
			int key = scanner.nextInt();
			switch (key) {
			case 1:{
				boolean menuFlag = login();
				
				if(menuFlag) {
					while(menuFlag) {
						displayMenu();
						int menuKey = scanner.nextInt();
						switch (menuKey) {
						case 1:
							checkReservation();
							break;
						case 2:
							returnCar();
							break;
						case 3:
							reservation();
							break;
						case 4:
							addTime();
							break;
						case 5:
							updateCustomer();
							customerId = null;
							menuFlag = false;
						case 6:
							selectCustomer();
							break;
						case 7:
						{
							System.out.println("�α׾ƿ� �Ǿ����ϴ�.");
							customerId = null;
							menuFlag = false;
						}
							break;
						case 8:
							menuFlag =false;
							break;
						default:
							break;
						}
					}
					
					
				}
			}
				
				break;
			case 2:
				signUp();
				break;
			
			case 3:
			{
				secession(); 
			}
				break;
			
			case 4:
			{
				System.out.println("�ý����� ����Ǿ����ϴ�.");
				flag = false;
			}
				break;
			default:
				break;
			}
		}
	}
	
	private static void updateCustomer() {
		boolean updateFlag = login();
		if(updateFlag) {
			displayCustomerInfo();
			int key = scanner.nextInt();
			switch (key) {
			case 1:
				System.out.println("���ο� �̸��� �Է��ϼ���>>");
				String newName = scanner.next();
				newName = checkName(newName);
				csService.updateName(newName, customerId);
				System.out.println("�ٽ� �α����ϼ���");
				break;
			case 2:
				System.out.println("���ο� ��ȭ��ȣ�� �Է��ϼ���>>");
				String newPhone = scanner.next();
				newPhone = checkPhone(newPhone);
				csService.updatePhone(newPhone, customerId);
				System.out.println("�ٽ� �α����ϼ���");
				break;
			case 3:
				System.out.println("���ο� ��й�ȣ�� �Է��ϼ���>>");
				String newPW = scanner.next();
				newPW = checkPW(newPW);
				csService.updatePW(newPW,customerId);
				System.out.println("�ٽ� �α����ϼ���");
				break;
			case 4:
				System.out.println("���ο� ���㸦 �Է��ϼ���>>");
				String newLicense = scanner.next();
				newLicense = checkLicense(newLicense);
				csService.updateLicense(newLicense, customerId);
				System.out.println("�ٽ� �α����ϼ���");
				break;
			case 5:
				
				break;

			default:
				break;
			}
			
		}
		else {
			System.out.println("ȸ�� ������ ��ġ���� �ʽ��ϴ�.");
			updateCustomer();
		}
	}
	private static void displayCustomerInfo() {
		System.out.println("================================================================");
		System.out.println("1.�̸� ���� | 2. ��ȭ��ȣ ���� | 3. ��й�ȣ ���� | 4. ���� ���� | 5. �ڷΰ���");
		System.out.println("================================================================");
		
		
	}
	///ȸ�� ���� ��ȸ////////////////////////////////////////////////////////////
	private static void selectCustomer() {
		csVeiw.printCustomer(csService.selectCustomer(customerId));
	}
	
	///�α���/////////////////////////////////////////////////////////
	public static boolean login() {
		System.out.print("���̵� �Է��ϼ���>>");
		String userId = scanner.next();
		System.out.print("�н����带 �Է��ϼ���>>");
		String PW = scanner.next();
		boolean sucess = csService.login(userId, PW);
		System.out.println(sucess? "�α��ο� �����߽��ϴ�.":"ȸ�������� ��ġ���� �ʽ��ϴ�.");
		customerId = userId;
		return sucess;
	}
	///ȸ������///////////////////////////////////////////////////////
	private static void signUp() {
		CSDTO customer = new CSDTO();
		System.out.print("��ȭ��ȣ�� �Է��ϼ���(Ư������ '-' ����)>>");
		String phone = scanner.next();
		phone = checkPhone(phone);
		customer.setPhone_number(phone);
		
		System.out.print("�̸��� �Է��ϼ���>>");
		String name = scanner.next();
		name = checkName(name);
		customer.setCustomer_name(name);
		
		System.out.print("���㸦 �Է��ϼ���>>");
		String license = scanner.next();
		license = checkLicense(license);
		customer.setDriver_license(license);
		
		System.out.println("���̵� �Է��ϼ���>>");
		String id = scanner.next();
		id = checkUserId(id);
		customer.setUser_id(id);
		
		System.out.println("��й�ȣ�� �Է��ϼ���>>");
		String pw = scanner.next();
		pw = checkPW(pw);
		customer.setPassword(pw);
		
		csService.customerInsert(customer);
	}
	///Ż��/////////////////////////////////////////////////////////
	private static void secession() {
		System.out.print("ȸ�����̵� �Է��ϼ���>>");
		String userId = scanner.next();
		System.out.println("��й�ȣ�� �Է��ϼ���>>");
		String PW = scanner.next();
		csService.deleteCustomer(userId, PW);
	}
	///�ݳ� �ϱ�////////////////////////////////////////////////////////////////////////////
	private static void returnCar() {
		
		System.out.print("���� �ݳ��Ͻó���? �������̳� ������ �Է��ϼ���>>");
		String destination = scanner.next();
		csVeiw.printPZ(csService.pzSelectBy("location_name", destination));
		csVeiw.printPZ(csService.pzSelectBy("city_name", destination));
		System.out.print("�ݳ� ������ ��ȣ�� �Է��ϼ���>>");
		int returnZone = scanner.nextInt();
		csService.updateReturnZone(customerId, returnZone);
		System.out.println("=============���=============");
		System.out.print("����Ÿ��� �Է��ϼ���>>");
		csService.updateDriveDistance(scanner.nextDouble(), customerId);
		csService.updateDriveFee(customerId);
		csVeiw.printFee(csService.updateTotalFee(customerId));
		csService.returnCar(customerId);
	}
///�����ϱ�///////////////////////////////////////////////////////////////////////////////
	private static void reservation() {
		CSDTO ss = new CSDTO();
		System.out.print("������� �Է��ϼ���(������ �̸�, ����)>>");
		String start = scanner.next();
		csVeiw.printPZ(csService.pzSelectBy("location_name", start));
		csVeiw.printPZ(csService.pzSelectBy("city_name", start));
		System.out.print("������ ��ȣ�� �Է��ϼ���>>");
		ss.setRental_zone_id(scanner.nextInt());
		csVeiw.printCar(csService.selectCarByZone(ss.getRental_zone_id()));
		System.out.print("�ڵ����� �����ϼ���>>");
		ss.setCar_id(scanner.nextInt());
		System.out.print("�뿩 �ð��� �Է��ϼ���(1�ð�����)>>");
		ss.setInitial_rental_time(scanner.nextDouble());
		System.out.println("������ �����ϼ���(high | medium | low)");
		csVeiw.printInsurance(csService.selectInsurance(ss.getCar_id()));
		String insurance = checkInsurance(scanner.next());
		ss.setInsurance(insurance);
		ss.setUser_id(customerId);
		csService.ssInsert(ss);
		csService.insert_fee(customerId);
		csService.updateRentalFee(customerId);
		csService.updateInitInsurance(customerId);
		csService.updateZoneId(ss.getCar_id());
//		csService.updateTollId(customer_phone, ss);
		
	}
	private static String checkInsurance(String next) {
		String newInsurace = next;
		if(newInsurace.equals("high")) {
			
		}else if(newInsurace.equals("medium")) {
			
		}else if(newInsurace.equals("low")) {
			
		}else {
			System.out.println("high||medium||low �� �ϳ��� �Է��ϼ���");
			return checkInsurance(newInsurace);
		}
		return newInsurace;
	}

	///����Ȯ��/////////////////////////////////////////////////////////////////////////////////
	private static void checkReservation() {
		csVeiw.printSS(csService.ssSelectById(customerId));
	}
	///�ð� �߰�///////////////////////////////////////////////////////////////////////////
	private static void addTime() {
		System.out.print("�߰��� �ð��� �Է��ϼ���(1�ð� ����)>>");
		csService.updateAddTime(scanner.nextDouble(), customerId);
		csService.updateAddInsurance(customerId);
		csService.updateAddFee(customerId);
		
	}
	
	///ȸ������ ��ȿ�� ����////////////////////////////////////////////////////////////////////////////////
	private static String checkUserId(String userId) {
		String newId = userId;
		//ȸ�� ���� ��ȿ�� �˻�
		String idPattern= "^[a-z][a-z]*[0-9]*$";
		try {
			if(Pattern.matches(idPattern, newId)==false) throw new SignUpException("���̵� ���Ŀ� ���� �ʽ��ϴ�."); 
		} catch (SignUpException e) {
			System.out.println(e.getMessage());
			System.out.print("���̵� �ٽ� �Է��ϼ���>>");
			Scanner scanner = new Scanner(System.in);
			newId = scanner.next();
			checkUserId(newId);
		}
		
		return newId;
	}

	private static String checkLicense(String license) {
		String newLicense = license;
		//ȸ�� ���� ��ȿ�� �˻�
		String licensePattern = "^[0-9]{2}-[0-9]{2}-[0-9]{6}-[0-9]{2}$";
		try {
			if(Pattern.matches(licensePattern, newLicense)==false) throw new SignUpException("�������� ���Ŀ� ���� �ʽ��ϴ�."); 
		} catch (SignUpException e) {
			System.out.println(e.getMessage());
			System.out.print("�������㸦 �ٽ� �Է��ϼ���>>");
			Scanner scanner = new Scanner(System.in);
			newLicense = scanner.next();
			checkLicense(newLicense);
		}
		
		return newLicense;
	}

	private static String checkName(String name) {
		String newName = name;
		//ȸ�� ���� ��ȿ�� �˻�
		String namePattern = "^[��-�R]*$";
		try {
			if(Pattern.matches(namePattern, newName)==false) throw new SignUpException("�̸��� �ѱ۷θ� �ۼ��ؾ��մϴ�."); 
		} catch (SignUpException e) {
			System.out.println(e.getMessage());
			System.out.print("�̸��� �ٽ� �Է��ϼ���>>");
			Scanner scanner = new Scanner(System.in);
			newName = scanner.next();
			checkName(newName);
		}
		
		return newName;
	}

	private static String checkPhone(String phone) {
		String newPhone = phone;
		//ȸ�� ���� ��ȿ�� �˻�
		String phonePattern = "^[0,1]{3}[0-9]{3,4}[0-9]{4}$";
		try {
			if(Pattern.matches(phonePattern, newPhone)==false) throw new SignUpException("��ȭ��ȣ ���Ŀ� ���� �ʽ��ϴ�."); 
		} catch (SignUpException e) {
			System.out.println(e.getMessage());
			System.out.print("��ȭ��ȣ�� �ٽ� �Է��ϼ���>>");
			Scanner scanner = new Scanner(System.in);
			newPhone = scanner.next();
			checkPhone(newPhone);
		}
		
		return newPhone;
}
	private static String checkPW(String password) {
		boolean check = true;
		String newPassword = password;
		Scanner scanner = new Scanner(System.in);
		try {
			//����
			if(password.length()<8&&password.length()>20) throw new SignUpException("�н������ 8~20�����Դϴ�.");
			//�ҹ��� ����
			int count = 0;
			for(char c ='a';c<='z';c++) {
				if(password.contains(c+""))count++;
			}
			if(count == 0) throw new SignUpException("���� �ҹ��ڰ� �ʿ��մϴ�.");
			//�빮�� ����
			count = 0;
			for(char c = 'A';c<='Z';c++) {
				if(password.contains(c+""))count++;
			}
			if(count == 0) throw new SignUpException("���� �빮�ڰ� �ʿ��մϴ�.");
			//���� ���� 
			count = 0;
			for(int i = 0; i<9;i++) {
				if(password.contains(i+"")) count++;
			}
			if(count==0) throw new SignUpException("���ڰ� �ʿ��մϴ�.");
		} catch (SignUpException e) {
			System.out.println(e.getMessage());
			System.out.print("�н����带 �ٽ� �Է��ϼ���>>");
			newPassword = scanner.next();
			checkPW(newPassword);
			
		}
		return newPassword;
		
	}
	public static void displayFirst() {
		System.out.println("===================================");
		System.out.println("1.�α��� | 2.ȸ������ | 3.ȸ��Ż�� | 4.����");
		System.out.println("===================================");
	}
	
	public static void displayMenu() {
		System.out.println("============================================================================================");
		System.out.println("1.����Ȯ�� | 2.�ݳ��ϱ� | 3. �����ϱ� | 4.�뿩 �ð� �߰� | 5.ȸ���������� | 6.ȸ��������ȸ | 7.�α׾ƿ� | 8.�ڷΰ���");
		System.out.println("============================================================================================");
		
	}
	
}
