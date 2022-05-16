package view;

import java.util.List;

import dto.CSDTO;

public class CSVeiw {

	public void print(List<CSDTO> cslist) {
		if(cslist==null) {
			System.out.println("검색결과가 없습니다.");
		}
		else {
			for(CSDTO cs: cslist) {
				System.out.println(cs);
			}
		}
		
	}
	public void printPZ(List<CSDTO> cslist) {
		if(cslist==null) {
			System.out.println("검색결과가 없습니다.");
		}
		else {
			for(CSDTO cs: cslist) {
				System.out.println(cs.toStringPZ());
			}
		}
		
	}
	public void printCar(List<CSDTO> cslist) {
		if(cslist==null) {
			System.out.println("검색결과가 없습니다.");
		}
		else {
			for(CSDTO cs: cslist) {
				System.out.println(cs.toStringCar());
			}
		}
		
	}
	public void printCustomer(List<CSDTO> cslist) {
		if(cslist==null) {
			System.out.println("검색결과가 없습니다.");
		}
		else {
			for(CSDTO cs: cslist) {
				System.out.println(cs.toStringCustomer());
			}
		}
		
	}
	public void printFee(List<CSDTO> cslist) {
		if(cslist==null) {
			System.out.println("검색결과가 없습니다.");
		}
		else {
			for(CSDTO cs: cslist) {
				System.out.println(cs.toStringFee());
			}
		}
		
	}
	public void printSS(List<CSDTO> cslist) {
		if(cslist==null) {
			System.out.println("검색결과가 없습니다.");
		}
		else {
			for(CSDTO cs: cslist) {
				System.out.println(cs.toStringSS());
			}
		}
		
	}
	public void printInsurance(List<CSDTO> cslist) {
		if(cslist==null) {
			System.out.println("검색결과가 없습니다.");
		}
		else {
			for(CSDTO cs: cslist) {
				System.out.println(cs.toStringInsurance());
			}
		}
		
	}
	public void printFee(int fee) {
		System.out.println(fee);
	}
}
