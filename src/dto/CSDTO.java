package dto;

public class CSDTO {
	private String phone_number;
	private String customer_name;
	private String driver_license;
	private String user_id;
	private String password;
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getDriver_license() {
		return driver_license;
	}
	public void setDriver_license(String driver_license) {
		this.driver_license = driver_license;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


	private double rental_fee;
	private double addtional_fee;
	private double driving_fee;
	private double insurance_fee;
	private double toll_fee;
	private int service_id;
	public double getRental_fee() {
		return rental_fee;
	}
	public void setRental_fee(double rental_fee) {
		this.rental_fee = rental_fee;
	}
	public double getAddtional_fee() {
		return addtional_fee;
	}
	public void setAddtional_fee(double addtional_fee) {
		this.addtional_fee = addtional_fee;
	}
	public double getDriving_fee() {
		return driving_fee;
	}
	public void setDriving_fee(double driving_fee) {
		this.driving_fee = driving_fee;
	}
	public double getInsurance_fee() {
		return insurance_fee;
	}
	public void setInsurance_fee(double insurance_fee) {
		this.insurance_fee = insurance_fee;
	}
	public double getToll_fee() {
		return toll_fee;
	}
	public void setToll_fee(double toll_fee) {
		this.toll_fee = toll_fee;
	}
	public int getService_id() {
		return service_id;
	}
	public void setService_id(int service_id) {
		this.service_id = service_id;
	}
	
	private int zone_id;
	private int parking_space;
	private int remaining_space;
	private String location_name;
	private String city_name;
	public int getZone_id() {
		return zone_id;
	}
	public void setZone_id(int zone_id) {
		this.zone_id = zone_id;
	}
	public int getParking_space() {
		return parking_space;
	}
	public void setParking_space(int parking_space) {
		this.parking_space = parking_space;
	}
	public int getRemaining_space() {
		return remaining_space;
	}
	public void setRemaining_space(int remaining_space) {
		this.remaining_space = remaining_space;
	}
	public String getLocation_name() {
		return location_name;
	}
	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	private int car_id;
	private double initial_rental_time;
	private double addtional_rental_time;
	private double distance_drive;
	private int rental_zone_id;
	private int return_zone_id;
	private double total_fee;
	private int toll_id;
	private int state;
	private String insurance;
	
	public int getCar_id() {
		return car_id;
	}
	public void setCar_id(int carid) {
		this.car_id= carid;
	}
	public double getInitial_rental_time() {
		return initial_rental_time;
	}
	public void setInitial_rental_time(double initial_rental_time) {
		this.initial_rental_time = initial_rental_time;
	}
	public double getAddtional_rental_time() {
		return addtional_rental_time;
	}
	public void setAddtional_rental_time(double addtional_rental_time) {
		this.addtional_rental_time = addtional_rental_time;
	}
	public double getDistance_drive() {
		return distance_drive;
	}
	public void setDistance_drive(double distance_drive) {
		this.distance_drive = distance_drive;
	}
	public int getRental_zone_id() {
		return rental_zone_id;
	}
	public void setRental_zone_id(int rental_zone_id) {
		this.rental_zone_id = rental_zone_id;
	}
	public int getReturn_zone_id() {
		return return_zone_id;
	}
	public void setReturn_zone_id(int return_zone_id) {
		this.return_zone_id = return_zone_id;
	}
	public double getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(double total_fee) {
		this.total_fee = total_fee;
	}
	public int getToll_id() {
		return toll_id;
	}
	public void setToll_id(int toll_id) {
		this.toll_id = toll_id;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getInsurance() {
		return insurance;
	}
	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}
	
	private String car_type;
	private String car_model;
	private String car_color;
	private int residual_fuel;
	public String getCar_type() {
		return car_type;
	}
	public void setCar_type(String car_type) {
		this.car_type = car_type;
	}
	public String getCar_model() {
		return car_model;
	}
	public void setCar_model(String car_model) {
		this.car_model = car_model;
	}
	public String getCar_color() {
		return car_color;
	}
	public void setCar_color(String car_color) {
		this.car_color = car_color;
	}
	public int getResidual_fuel() {
		return residual_fuel;
	}
	public void setResidual_fuel(int residual_fuel) {
		this.residual_fuel = residual_fuel;
	}
	private int high_self_pay;
	private int medium_self_pay;
	private int low_self_pay;
	public int getHigh_self_pay() {
		return high_self_pay;
	}
	public void setHigh_self_pay(int high_self_pay) {
		this.high_self_pay = high_self_pay;
	}
	public int getMedium_self_pay() {
		return medium_self_pay;
	}
	public void setMedium_self_pay(int medium_self_pay) {
		this.medium_self_pay = medium_self_pay;
	}
	public int getLow_self_pay() {
		return low_self_pay;
	}
	public void setLow_self_pay(int low_self_pay) {
		this.low_self_pay = low_self_pay;
	}
	public String toStringPZ() {
		return "CSDTO [zone_id=" + zone_id + ", parking_space=" + parking_space + ", remaining_space=" + remaining_space
				+ ", location_name=" + location_name + ", city_name=" + city_name + "]";
	}
	public String toStringCar() {
		return "CSDTO [zone_id=" + zone_id + ", car_id=" + car_id + ", car_type=" + car_type + ", car_model="
				+ car_model + ", car_color=" + car_color + ", residual_fuel=" + residual_fuel + "]";
	}
	public String toStringCustomer() {
		return "CSDTO [phone_number=" + phone_number + ", customer_name=" + customer_name + ", driver_license="
				+ driver_license + "]";
	}
	public String toStringFee() {
		return "CSDTO [rental_fee=" + rental_fee + ", addtional_fee=" + addtional_fee + ", driving_fee=" + driving_fee
				+ ", insurance_fee=" + insurance_fee + ", toll_fee=" + toll_fee + ", service_id=" + service_id + "]";
	}
	public String toStringSS() {
		return "CSDTO [service_id=" + service_id + ", car_id=" + car_id
				+ ", initial_rental_time=" + initial_rental_time + ", addtional_rental_time=" + addtional_rental_time
				+ ", distance_drive=" + distance_drive + ", rental_zone_id=" + rental_zone_id + ", return_zone_id="
				+ return_zone_id + ", total_fee=" + total_fee + ", toll_id=" + toll_id + ", state=" + state
				+ ", insurance=" + insurance + "]";
	}
	public String toStringInsurance() {
		return "CSDTO [car_type=" + car_type + ", high_self_pay=" + high_self_pay + ", medium_self_pay="
				+ medium_self_pay + ", low_self_pay=" + low_self_pay + "]";
	}
	
	
}




