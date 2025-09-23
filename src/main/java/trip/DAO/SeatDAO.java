package trip.DAO;

import trip.model.Seat;
import trip.util.SupabaseClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeatDAO {
    public List<Seat> getSeatsByVehicle(String vehicle_id) throws Exception {
        String query = "select=id_seat,status_book, status_select,id_vehicle&id_vehicle=eq." + vehicle_id;
        String json = SupabaseClient.get("seat", query);

        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> raw_seats = mapper.readValue(
                json, new TypeReference<List<Map<String, Object>>>() {}
        );

        List<Seat> seats = new ArrayList<>();
        for (Map<String, Object> seat_data : raw_seats) {
            Seat new_seat = new Seat();
            new_seat.setId_seat(seat_data.get("id_seat").toString());
            new_seat.setStatus_book((Boolean) seat_data.get("status_book"));
            new_seat.setStatus_select((Boolean) seat_data.get("status_select"));
            new_seat.setId_vehicle(seat_data.get("id_vehicle").toString());
            seats.add(new_seat);
        }

        seats.sort((s1, s2) -> {
            String prefix1 = s1.getId_seat().replaceAll("\\d", "");
            String prefix2 = s2.getId_seat().replaceAll("\\d", "");
            int cmp = prefix1.compareTo(prefix2);
            if (cmp != 0) return cmp;

            int num1 = Integer.parseInt(s1.getId_seat().replaceAll("\\D", ""));
            int num2 = Integer.parseInt(s2.getId_seat().replaceAll("\\D", ""));
            return Integer.compare(num1, num2);
        });

        return seats;
    }
}