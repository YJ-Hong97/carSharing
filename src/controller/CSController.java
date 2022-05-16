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
							System.out.println("·Î±×¾Æ¿ô µÇ¾ú½À´Ï´Ù.");
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
				System.out.println("½Ã½ºÅÛÀÌ Á¾·áµÇ¾ú½À´Ï´Ù.");
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
				System.out.println("»õ·Î¿î ÀÌ¸§À» ÀÔ·ÂÇÏ¼¼¿ä>>");
				String newName = scanner.next();
				newName = checkName(newName);
				csService.updateName(newName, customerId);
				System.out.println("´Ù½Ã ·Î±×ÀÎÇÏ¼¼¿ä");
				break;
			case 2:
				System.out.println("»õ·Î¿î ÀüÈ­¹øÈ£¸¦ ÀÔ·ÂÇÏ¼¼¿ä>>");
				String newPhone = scanner.next();
				newPhone = checkPhone(newPhone);
				csService.updatePhone(newPhone, customerId);
				System.out.println("´Ù½Ã ·Î±×ÀÎÇÏ¼¼¿ä");
				break;
			case 3:
				System.out.println("»õ·Î¿î ºñ¹Ð¹øÈ£¸¦ ÀÔ·ÂÇÏ¼¼¿ä>>");
				String newPW = scanner.next();
				newPW = checkPW(newPW);
				csService.updatePW(newPW,customerId);
				System.out.println("´Ù½Ã ·Î±×ÀÎÇÏ¼¼¿ä");
				break;
			case 4:
				System.out.println("»õ·Î¿î ¸éÇã¸¦ ÀÔ·ÂÇÏ¼¼¿ä>>");
				String newLicense = scanner.next();
				newLicense = checkLicense(newLicense);
				csService.updateLicense(newLicense, customerId);
				System.out.println("´Ù½Ã ·Î±×ÀÎÇÏ¼¼¿ä");
				break;
			case 5:
				
				break;

			default:
				break;
			}
			
		}
		else {
			System.out.println("È¸¿ø Á¤º¸°¡ ÀÏÄ¡ÇÏÁö ¾Ê½À´Ï´Ù.");
			updateCustomer();
		}
	}
	private static void displayCustomerInfo() {
		System.out.println("================================================================");
		System.out.println("1.ÀÌ¸§ º¯°æ | 2. ÀüÈ­¹øÈ£ º¯°æ | 3. ºñ¹Ð¹øÈ£ º¯°æ | 4. ¸éÇã º¯°æ | 5. µÚ·Î°¡±â");
		System.out.println("================================================================");
		
		
	}
	///È¸¿ø Á¤º¸ Á¶È¸////////////////////////////////////////////////////////////
	private static void selectCustomer() {
		csVeiw.printCustomer(csService.selectCustomer(customerId));
	}
	
	///·Î±×ÀÎ/////////////////////////////////////////////////////////
	public static boolean login() {
		System.out.print("¾ÆÀÌµð¸¦ ÀÔ·ÂÇÏ¼¼¿ä>>");
		String userId = scanner.next();
		System.out.print("ÆÐ½º¿öµå¸¦ ÀÔ·ÂÇÏ¼¼¿ä>>");
		String PW = scanner.next();
		boolean sucess = csService.login(userId, PW);
		System.out.println(sucess? "·Î±×ÀÎ¿¡ ¼º°øÇß½À´Ï´Ù.":"È¸¿øÁ¤º¸°¡ ÀÏÄ¡ÇÏÁö ¾Ê½À´Ï´Ù.");
		customerId = userId;
		return sucess;
	}
	///È¸¿ø°¡ÀÔ///////////////////////////////////////////////////////
	private static void signUp() {
		CSDTO customer = new CSDTO();
		System.out.print("ÀüÈ­¹øÈ£¸¦ ÀÔ·ÂÇÏ¼¼¿ä(Æ¯¼ö¹®ÀÚ '-' Á¦¿Ü)>>");
		String phone = scanner.next();
		phone = checkPhone(phone);
		customer.setPhone_number(phone);
		
		System.out.print("ÀÌ¸§À» ÀÔ·ÂÇÏ¼¼¿ä>>");
		String name = scanner.next();
		name = checkName(name);
		customer.setCustomer_name(name);
		
		System.out.print("¸éÇã¸¦ ÀÔ·ÂÇÏ¼¼¿ä>>");
		String license = scanner.next();
		license = checkLicense(license);
		customer.setDriver_license(license);
		
		System.out.println("¾ÆÀÌµð¸¦ ÀÔ·ÂÇÏ¼¼¿ä>>");
		String id = scanner.next();
		id = checkUserId(id);
		customer.setUser_id(id);
		
		System.out.println("ºñ¹Ð¹øÈ£¸¦ ÀÔ·ÂÇÏ¼¼¿ä>>");
		String pw = scanner.next();
		pw = checkPW(pw);
		customer.setPassword(pw);
		
		csService.customerInsert(customer);
	}
	///Å»Åð/////////////////////////////////////////////////////////
	private static void secession() {
		System.out.print("È¸¿ø¾ÆÀÌµð¸¦ ÀÔ·ÂÇÏ¼¼¿ä>>");
		String userId = scanner.next();
		System.out.println("ºñ¹Ð¹øÈ£¸¦ ÀÔ·ÂÇÏ¼¼¿ä>>");
		String PW = scanner.next();
		csService.deleteCustomer(userId, PW);
	}
	///¹Ý³³ ÇÏ±â////////////////////////////////////////////////////////////////////////////
	private static void returnCar() {
		
		System.out.print("¾îµð·Î ¹Ý³³ÇÏ½Ã³ª¿ä? ÁÖÂ÷ÀåÀÌ³ª Áö¿ªÀ» ÀÔ·ÂÇÏ¼¼¿ä>>");
		String destination = scanner.next();
		csVeiw.printPZ(csService.pzSelectBy("location_name", destination));
		csVeiw.printPZ(csService.pzSelectBy("city_name", destination));
		System.out.print("¹Ý³³ ÁÖÂ÷Àå ¹øÈ£¸¦ ÀÔ·ÂÇÏ¼¼¿ä>>");
		int returnZone = scanner.nextInt();
		csService.updateReturnZone(customerId, returnZone);
		System.out.println("=============¿ä±Ý=============");
		System.out.print("ÁÖÇà°Å¸®¸¦ ÀÔ·ÂÇÏ¼¼¿ä>>");
		csService.updateDriveDistance(scanner.nextDouble(), customerId);
		csService.updateDriveFee(customerId);
		csVeiw.printFee(csService.updateTotalFee(customerId));
		csService.returnCar(customerId);
	}
///¿¹¾àÇÏ±â///////////////////////////////////////////////////////////////////////////////
	private static void reservation() {
		CSDTO ss = new CSDTO();
		System.out.print("Ãâ¹ßÁö¸¦ ÀÔ·ÂÇÏ¼¼¿ä(ÁÖÂ÷Àå ÀÌ¸§, Áö¿ª)>>");
		String start = scanner.next();
		csVeiw.printPZ(csService.pzSelectBy("location_name", start));
		csVeiw.printPZ(csService.pzSelectBy("city_name", start));
		System.out.print("ÁÖÂ÷Àå ¹øÈ£¸¦ ÀÔ·ÂÇÏ¼¼¿ä>>");
		ss.setRental_zone_id(scanner.nextInt());
		csVeiw.printCar(csService.selectCarByZone(ss.getRental_zone_id()));
		System.out.print("ÀÚµ¿Â÷¸¦ ¼±ÅÃÇÏ¼¼¿ä>>");
		ss.setCar_id(scanner.nextInt());
		System.out.print("´ë¿© ½Ã°£À» ÀÔ·ÂÇÏ¼¼¿ä(1½Ã°£´ÜÀ§)>>");
		ss.setInitial_rental_time(scanner.nextDouble());
		System.out.println("º¸ÇèÀ» ¼±ÅÃÇÏ¼¼¿ä(high | medium | low)");
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
			System.out.println("high||medium||low Áß ÇÏ³ª¸¦ ÀÔ·ÂÇÏ¼¼¿ä");
			return checkInsurance(newInsurace);
		}
		return newInsurace;
	}

	///¿¹¾àÈ®ÀÎ/////////////////////////////////////////////////////////////////////////////////
	private static void checkReservation() {
		csVeiw.printSS(csService.ssSelectById(customerId));
	}
	///½Ã°£ Ãß°¡///////////////////////////////////////////////////////////////////////////
	private static void addTime() {
		System.out.print("Ãß°¡ÇÒ ½Ã°£À» ÀÔ·ÂÇÏ¼¼¿ä(1½Ã°£ ´ÜÀ§)>>");
		csService.updateAddTime(scanner.nextDouble(), customerId);
		csService.updateAddInsurance(customerId);
		csService.updateAddFee(customerId);
		
	}
	
	///È¸¿ø°¡ÀÔ À¯È¿¼º °ËÁõ////////////////////////////////////////////////////////////////////////////////
	private static String checkUserId(String userId) {
		String newId = userId;
		//È¸¿ø Á¤º¸ À¯È¿¼º °Ë»ç
		String idPattern= "^[a-z][a-z]*[0-9]*$";
		try {
			if(Pattern.matches(idPattern, newId)==false) throw new SignUpException("¾ÆÀÌµð Çü½Ä¿¡ ¸ÂÁö ¾Ê½À´Ï´Ù."); 
		} catch (SignUpException e) {
			System.out.println(e.getMessage());
			System.out.print("¾ÆÀÌµð¸¦ ´Ù½Ã ÀÔ·ÂÇÏ¼¼¿ä>>");
			Scanner scanner = new Scanner(System.in);
			newId = scanner.next();
			checkUserId(newId);
		}
		
		return newId;
	}

	private static String checkLicense(String license) {
		String newLicense = license;
		//È¸¿ø Á¤º¸ À¯È¿¼º °Ë»ç
		String licensePattern = "^[0-9]{2}-[0-9]{2}-[0-9]{6}-[0-9]{2}$";
		try {
			if(Pattern.matches(licensePattern, newLicense)==false) throw new SignUpException("¿îÀü¸éÇã Çü½Ä¿¡ ¸ÂÁö ¾Ê½À´Ï´Ù."); 
		} catch (SignUpException e) {
			System.out.println(e.getMessage());
			System.out.print("¿îÀü¸éÇã¸¦ ´Ù½Ã ÀÔ·ÂÇÏ¼¼¿ä>>");
			Scanner scanner = new Scanner(System.in);
			newLicense = scanner.next();
			checkLicense(newLicense);
		}
		
		return newLicense;
	}

	private static String checkName(String name) {
		String newName = name;
		//È¸¿ø Á¤º¸ À¯È¿¼º °Ë»ç
		String namePattern = "^[°¡-ÆR]*$";
		try {
			if(Pattern.matches(namePattern, newName)==false) throw new SignUpException("ÀÌ¸§Àº ÇÑ±Û·Î¸¸ ÀÛ¼ºÇØ¾ßÇÕ´Ï´Ù."); 
		} catch (SignUpException e) {
			System.out.println(e.getMessage());
			System.out.print("ÀÌ¸§À» ´Ù½Ã ÀÔ·ÂÇÏ¼¼¿ä>>");
			Scanner scanner = new Scanner(System.in);
			newName = scanner.next();
			checkName(newName);
		}
		
		return newName;
	}

	private static String checkPhone(String phone) {
		String newPhone = phone;
		//È¸¿ø Á¤º¸ À¯È¿¼º °Ë»ç
		String phonePattern = "^[0,1]{3}[0-9]{3,4}[0-9]{4}$";
		try {
			if(Pattern.matches(phonePattern, newPhone)==false) throw new SignUpException("ÀüÈ­¹øÈ£ Çü½Ä¿¡ ¸ÂÁö ¾Ê½À´Ï´Ù."); 
		} catch (SignUpException e) {
			System.out.println(e.getMessage());
			System.out.print("ÀüÈ­¹øÈ£¸¦ ´Ù½Ã ÀÔ·ÂÇÏ¼¼¿ä>>");
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
			//±æÀÌ
			if(password.length()<8&&password.length()>20) throw new SignUpException("ÆÐ½º¿öµå´Â 8~20±ÛÀÚÀÔ´Ï´Ù.");
			//¼Ò¹®ÀÚ Æ÷ÇÔ
			int count = 0;
			for(char c ='a';c<='z';c++) {
				if(password.contains(c+""))count++;
			}
			if(count == 0) throw new SignUpException("¿µ¹® ¼Ò¹®ÀÚ°¡ ÇÊ¿äÇÕ´Ï´Ù.");
			//´ë¹®ÀÚ Æ÷ÇÔ
			count = 0;
			for(char c = 'A';c<='Z';c++) {
				if(password.contains(c+""))count++;
			}
			if(count == 0) throw new SignUpException("¿µ¹® ´ë¹®ÀÚ°¡ ÇÊ¿äÇÕ´Ï´Ù.");
			//¼ýÀÚ Æ÷ÇÔ 
			count = 0;
			for(int i = 0; i<9;i++) {
				if(password.contains(i+"")) count++;
			}
			if(count==0) throw new SignUpException("¼ýÀÚ°¡ ÇÊ¿äÇÕ´Ï´Ù.");
		} catch (SignUpException e) {
			System.out.println(e.getMessage());
			System.out.print("ÆÐ½º¿öµå¸¦ ´Ù½Ã ÀÔ·ÂÇÏ¼¼¿ä>>");
			newPassword = scanner.next();
			checkPW(newPassword);
			
		}
		return newPassword;
		
	}
	public static void displayFirst() {
		System.out.println("===================================");
		System.out.println("1.·Î±×ÀÎ | 2.È¸¿ø°¡ÀÔ | 3.È¸¿øÅ»Åð | 4.Á¾·á");
		System.out.println("===================================");
	}
	
	public static void displayMenu() {
		System.out.println("============================================================================================");
		System.out.println("1.¿¹¾àÈ®ÀÎ | 2.¹Ý³³ÇÏ±â | 3. ¿¹¾àÇÏ±â | 4.´ë¿© ½Ã°£ Ãß°¡ | 5.È¸¿øÁ¤º¸º¯°æ | 6.È¸¿øÁ¤º¸Á¶È¸ | 7.·Î±×¾Æ¿ô | 8.µÚ·Î°¡±â");
		System.out.println("============================================================================================");
		
	}
	
}
