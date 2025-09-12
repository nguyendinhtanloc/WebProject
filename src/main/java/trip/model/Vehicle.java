package trip.model;

public class Vehicle {
    private String vehicle_id;
    private String license_plate;
    private int capacity;
    private String type;
    private String status;
    private String company_id;

    // Getters & Setters
    public String getVehicle_id() { return vehicle_id; }
    public void setVehicle_id(String vehicle_id) { this.vehicle_id = vehicle_id; }

    public String getLicense_plate() { return license_plate; }
    public void setLicense_plate(String license_plate) { this.license_plate = license_plate; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCompany_id() { return company_id; }
    public void setCompany_id(String company_id) { this.company_id = company_id; }
}