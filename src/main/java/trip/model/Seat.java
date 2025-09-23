package trip.model;

public class Seat {
    private String id_seat;
    private boolean status_book;
    private boolean status_select;
    private String id_vehicle;

    public String getId_seat() {
        return id_seat;
    }

    public void setId_seat(String id_seat) {
        this.id_seat = id_seat;
    }

    public boolean isStatus_book() {
        return status_book;
    }

    public void setStatus_book(boolean status_book) {
        this.status_book = status_book;
    }

    public boolean isStatus_select() {
        return status_select;
    }

    public void setStatus_select(boolean status_select) {
        this.status_select = status_select;
    }

    public String getId_vehicle() {
        return id_vehicle;
    }

    public void setId_vehicle(String id_vehicle) {
        this.id_vehicle = id_vehicle;
    }
}
