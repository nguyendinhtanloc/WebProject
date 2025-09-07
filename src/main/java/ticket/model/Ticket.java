package ticket.model;

public class Ticket {
    private String ticketId;
    private String userId;
    private String tripId;
    private int seatNumber;
    private String bookingTime;
    private String status;

    private String startLocation;
    private String endLocation;
    private String startTime;
    
    public Ticket(String ticketId, String userId, String tripId, int seatNumber, String bookingTime, String status,
                  String startLocation, String endLocation, String startTime) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.tripId = tripId;
        this.seatNumber = seatNumber;
        this.bookingTime = bookingTime;
        this.status = status;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startTime = startTime;
    }

    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTripId() { return tripId; }
    public void setTripId(String tripId) { this.tripId = tripId; }

    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }

    public String getBookingTime() { return bookingTime; }
    public void setBookingTime(String bookingTime) { this.bookingTime = bookingTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStartLocation() { return startLocation; }
    public void setStartLocation(String startLocation) { this.startLocation = startLocation; }

    public String getEndLocation() { return endLocation; }
    public void setEndLocation(String endLocation) { this.endLocation = endLocation; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
}