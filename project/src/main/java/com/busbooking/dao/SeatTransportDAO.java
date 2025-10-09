package com.busbooking.dao;

import com.busbooking.model.SeatTransport;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class SeatTransportDAO {

    public List<SeatTransport> getSeatsByVehicle(Integer vehicleId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em
                    .createQuery("SELECT s FROM SeatTransport s WHERE s.vehicleTransport.vehicleId = :vid",
                            SeatTransport.class)
                    .setParameter("vid", vehicleId)
                    .getResultList();

        } finally {
            em.close();
        }
    }
}
