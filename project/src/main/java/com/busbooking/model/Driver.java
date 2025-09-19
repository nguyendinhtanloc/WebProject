package model;

public class Driver {
	private String driver_id;
	private String company_id;
	private String name;
	private String phone;
	private String license_no;
	private int experience_years;
	private String status;
	public Driver() {
		// TODO Auto-generated constructor stub
	}
	
	public Driver(String driver_id, String company_id, String name, String phone, String license_no,
			int experience_years, String status) {
		super();
		this.driver_id = driver_id;
		this.company_id = company_id;
		this.name = name;
		this.phone = phone;
		this.license_no = license_no;
		this.experience_years = experience_years;
		this.status = status;
	}

	public String getDriver_id() {
		return driver_id;
	}
	public void setDriver_id(String driver_id) {
		this.driver_id = driver_id;
	}
	public String getCompany_id() {
		return company_id;
	}
	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLicense_no() {
		return license_no;
	}
	public void setLicense_no(String license_no) {
		this.license_no = license_no;
	}
	public int getExperience_years() {
		return experience_years;
	}
	public void setExperience_years(int experience_years) {
		this.experience_years = experience_years;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
