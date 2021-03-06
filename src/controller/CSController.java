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
							System.out.println("로그아웃 되었습니다.");
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
				System.out.println("시스템이 종료되었습니다.");
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
				System.out.println("새로운 이름을 입력하세요>>");
				String newName = scanner.next();
				newName = checkName(newName);
				csService.updateName(newName, customerId);
				System.out.println("다시 로그인하세요");
				break;
			case 2:
				System.out.println("새로운 전화번호를 입력하세요>>");
				String newPhone = scanner.next();
				newPhone = checkPhone(newPhone);
				csService.updatePhone(newPhone, customerId);
				System.out.println("다시 로그인하세요");
				break;
			case 3:
				System.out.println("새로운 비밀번호를 입력하세요>>");
				String newPW = scanner.next();
				newPW = checkPW(newPW);
				csService.updatePW(newPW,customerId);
				System.out.println("다시 로그인하세요");
				break;
			case 4:
				System.out.println("새로운 면허를 입력하세요>>");
				String newLicense = scanner.next();
				newLicense = checkLicense(newLicense);
				csService.updateLicense(newLicense, customerId);
				System.out.println("다시 로그인하세요");
				break;
			case 5:
				
				break;

			default:
				break;
			}
			
		}
		else {
			System.out.println("회원 정보가 일치하지 않습니다.");
			updateCustomer();
		}
	}
	private static void displayCustomerInfo() {
		System.out.println("================================================================");
		System.out.println("1.이름 변경 | 2. 전화번호 변경 | 3. 비밀번호 변경 | 4. 면허 변경 | 5. 뒤로가기");
		System.out.println("================================================================");
		
		
	}
	///회원 정보 조회////////////////////////////////////////////////////////////
	private static void selectCustomer() {
		csVeiw.printCustomer(csService.selectCustomer(customerId));
	}
	
	///로그인/////////////////////////////////////////////////////////
	public static boolean login() {
		System.out.print("아이디를 입력하세요>>");
		String userId = scanner.next();
		System.out.print("패스워드를 입력하세요>>");
		String PW = scanner.next();
		boolean sucess = csService.login(userId, PW);
		System.out.println(sucess? "로그인에 성공했습니다.":"회원정보가 일치하지 않습니다.");
		customerId = userId;
		return sucess;
	}
	///회원가입///////////////////////////////////////////////////////
	private static void signUp() {
		CSDTO customer = new CSDTO();
		System.out.print("전화번호를 입력하세요(특수문자 '-' 제외)>>");
		String phone = scanner.next();
		phone = checkPhone(phone);
		customer.setPhone_number(phone);
		
		System.out.print("이름을 입력하세요>>");
		String name = scanner.next();
		name = checkName(name);
		customer.setCustomer_name(name);
		
		System.out.print("면허를 입력하세요>>");
		String license = scanner.next();
		license = checkLicense(license);
		customer.setDriver_license(license);
		
		System.out.println("아이디를 입력하세요>>");
		String id = scanner.next();
		id = checkUserId(id);
		customer.setUser_id(id);
		
		System.out.println("비밀번호를 입력하세요>>");
		String pw = scanner.next();
		pw = checkPW(pw);
		customer.setPassword(pw);
		
		csService.customerInsert(customer);
	}
	///탈퇴/////////////////////////////////////////////////////////
	private static void secession() {
		System.out.print("회원아이디를 입력하세요>>");
		String userId = scanner.next();
		System.out.println("비밀번호를 입력하세요>>");
		String PW = scanner.next();
		csService.deleteCustomer(userId, PW);
	}
	///반납 하기////////////////////////////////////////////////////////////////////////////
	private static void returnCar() {
		
		System.out.print("어디로 반납하시나요? 주차장이나 지역을 입력하세요>>");
		String destination = scanner.next();
		csVeiw.printPZ(csService.pzSelectBy("location_name", destination));
		csVeiw.printPZ(csService.pzSelectBy("city_name", destination));
		System.out.print("반납 주차장 번호를 입력하세요>>");
		int returnZone = scanner.nextInt();
		csService.updateReturnZone(customerId, returnZone);
		System.out.println("=============요금=============");
		System.out.print("주행거리를 입력하세요>>");
		csService.updateDriveDistance(scanner.nextDouble(), customerId);
		csService.updateDriveFee(customerId);
		csVeiw.printFee(csService.updateTotalFee(customerId));
		csService.returnCar(customerId);
	}
///예약하기///////////////////////////////////////////////////////////////////////////////
	private static void reservation() {
		CSDTO ss = new CSDTO();
		System.out.print("출발지를 입력하세요(주차장 이름, 지역)>>");
		String start = scanner.next();
		csVeiw.printPZ(csService.pzSelectBy("location_name", start));
		csVeiw.printPZ(csService.pzSelectBy("city_name", start));
		System.out.print("주차장 번호를 입력하세요>>");
		ss.setRental_zone_id(scanner.nextInt());
		csVeiw.printCar(csService.selectCarByZone(ss.getRental_zone_id()));
		System.out.print("자동차를 선택하세요>>");
		ss.setCar_id(scanner.nextInt());
		System.out.print("대여 시간을 입력하세요(1시간단위)>>");
		ss.setInitial_rental_time(scanner.nextDouble());
		System.out.println("보험을 선택하세요(high | medium | low)");
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
			System.out.println("high||medium||low 중 하나를 입력하세요");
			return checkInsurance(newInsurace);
		}
		return newInsurace;
	}

	///예약확인/////////////////////////////////////////////////////////////////////////////////
	private static void checkReservation() {
		csVeiw.printSS(csService.ssSelectById(customerId));
	}
	///시간 추가///////////////////////////////////////////////////////////////////////////
	private static void addTime() {
		System.out.print("추가할 시간을 입력하세요(1시간 단위)>>");
		csService.updateAddTime(scanner.nextDouble(), customerId);
		csService.updateAddInsurance(customerId);
		csService.updateAddFee(customerId);
		
	}
	
	///회원가입 유효성 검증////////////////////////////////////////////////////////////////////////////////
	private static String checkUserId(String userId) {
		String newId = userId;
		//회원 정보 유효성 검사
		String idPattern= "^[a-z][a-z]*[0-9]*$";
		try {
			if(Pattern.matches(idPattern, newId)==false) throw new SignUpException("아이디 형식에 맞지 않습니다."); 
		} catch (SignUpException e) {
			System.out.println(e.getMessage());
			System.out.print("아이디를 다시 입력하세요>>");
			Scanner scanner = new Scanner(System.in);
			newId = scanner.next();
			checkUserId(newId);
		}
		
		return newId;
	}

	private static String checkLicense(String license) {
		String newLicense = license;
		//회원 정보 유효성 검사
		String licensePattern = "^[0-9]{2}-[0-9]{2}-[0-9]{6}-[0-9]{2}$";
		try {
			if(Pattern.matches(licensePattern, newLicense)==false) throw new SignUpException("운전면허 형식에 맞지 않습니다."); 
		} catch (SignUpException e) {
			System.out.println(e.getMessage());
			System.out.print("운전면허를 다시 입력하세요>>");
			Scanner scanner = new Scanner(System.in);
			newLicense = scanner.next();
			checkLicense(newLicense);
		}
		
		return newLicense;
	}

	private static String checkName(String name) {
		String newName = name;
		//회원 정보 유효성 검사
		String namePattern = "^[가-힣]*$";
		try {
			if(Pattern.matches(namePattern, newName)==false) throw new SignUpException("이름은 한글로만 작성해야합니다."); 
		} catch (SignUpException e) {
			System.out.println(e.getMessage());
			System.out.print("이름을 다시 입력하세요>>");
			Scanner scanner = new Scanner(System.in);
			newName = scanner.next();
			checkName(newName);
		}
		
		return newName;
	}

	private static String checkPhone(String phone) {
		String newPhone = phone;
		//회원 정보 유효성 검사
		String phonePattern = "^[0,1]{3}[0-9]{3,4}[0-9]{4}$";
		try {
			if(Pattern.matches(phonePattern, newPhone)==false) throw new SignUpException("전화번호 형식에 맞지 않습니다."); 
		} catch (SignUpException e) {
			System.out.println(e.getMessage());
			System.out.print("전화번호를 다시 입력하세요>>");
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
			//길이
			if(password.length()<8&&password.length()>20) throw new SignUpException("패스워드는 8~20글자입니다.");
			//소문자 포함
			int count = 0;
			for(char c ='a';c<='z';c++) {
				if(password.contains(c+""))count++;
			}
			if(count == 0) throw new SignUpException("영문 소문자가 필요합니다.");
			//대문자 포함
			count = 0;
			for(char c = 'A';c<='Z';c++) {
				if(password.contains(c+""))count++;
			}
			if(count == 0) throw new SignUpException("영문 대문자가 필요합니다.");
			//숫자 포함 
			count = 0;
			for(int i = 0; i<9;i++) {
				if(password.contains(i+"")) count++;
			}
			if(count==0) throw new SignUpException("숫자가 필요합니다.");
		} catch (SignUpException e) {
			System.out.println(e.getMessage());
			System.out.print("패스워드를 다시 입력하세요>>");
			newPassword = scanner.next();
			checkPW(newPassword);
			
		}
		return newPassword;
		
	}
	public static void displayFirst() {
		System.out.println("===================================");
		System.out.println("1.로그인 | 2.회원가입 | 3.회원탈퇴 | 4.종료");
		System.out.println("===================================");
	}
	
	public static void displayMenu() {
		System.out.println("============================================================================================");
		System.out.println("1.예약확인 | 2.반납하기 | 3. 예약하기 | 4.대여 시간 추가 | 5.회원정보변경 | 6.회원정보조회 | 7.로그아웃 | 8.뒤로가기");
		System.out.println("============================================================================================");
		
	}
	
}
