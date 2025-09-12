package trip.model;
import trip.model.Vehicle;

public class Trip {
    private String id;
    private String departure_place;
    private String arrival_place;
    private String departure_date;
    private String departure_time;
    private int price;
    private String vehicle_type;
    private Vehicle vehicle;

    // getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDeparture_place() { return departure_place; }
    public void setDeparture_place(String departure_place) { this.departure_place = departure_place; }

    public String getArrival_place() { return arrival_place; }
    public void setArrival_place(String arrival_place) { this.arrival_place = arrival_place; }

    public String getDeparture_date() { return departure_date; }
    public void setDeparture_date(String departure_date) { this.departure_date = departure_date; }

    public String getDeparture_time() { return departure_time; }
    public void setDeparture_time(String departure_time) { this.departure_time = departure_time; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
}
