package com.busbooking.model; // Sửa package cho đúng

public class Driver {
    // Đổi tên biến theo quy tắc camelCase
    private String driverId;
    private String companyId;
    private String name;
    private String phone;
    private String licenseNo;
    private int experienceYears;
    private String status;
    
    public Driver() {}

    // Getters and Setters đã được cập nhật
    public String getDriverId() {
        return driverId;
    }
    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }
    public String getCompanyId() {
        return companyId;
    }
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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
    public String getLicenseNo() {
        return licenseNo;
    }
    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }
    public int getExperienceYears() {
        return experienceYears;
    }
    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}