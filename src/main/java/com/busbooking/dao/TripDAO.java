package com.busbooking.dao;

import com.busbooking.entity.Trip;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TripDAO {
    
    public List<Trip> searchTrips(String departurePlace, String arrivalPlace, LocalDate departureDate) {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            String jpql = "SELECT t FROM Trip t " +
                         "LEFT JOIN FETCH t.busCompany " +
                         "LEFT JOIN FETCH t.vehicle " +
                         "LEFT JOIN FETCH t.driver " +
                         "WHERE LOWER(t.departurePlace) LIKE LOWER(:departurePlace) " +
                         "AND LOWER(t.arrivalPlace) LIKE LOWER(:arrivalPlace) " +
                         "AND t.departureDate = :departureDate " +
                         "ORDER BY t.departureTime";
            
            TypedQuery<Trip> query = em.createQuery(jpql, Trip.class);
            query.setParameter("departurePlace", "%" + departurePlace + "%");
            query.setParameter("arrivalPlace", "%" + arrivalPlace + "%");
            query.setParameter("departureDate", departureDate);
            
            List<Trip> results = query.getResultList();
            System.out.println("✅ Found " + results.size() + " trips");
            return results;
            
        } catch (Exception e) {
            System.err.println("❌ Error searching trips: " + e.getMessage());
            e.printStackTrace();
            // Return empty list instead of throwing exception
            return new ArrayList<>();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public List<String> getAllDeparturePlaces() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT t.departurePlace FROM Trip t " +
                         "ORDER BY t.departurePlace";
            
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<String> getAllArrivalPlaces() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT t.arrivalPlace FROM Trip t " +
                         "ORDER BY t.arrivalPlace";
            
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }    public Trip findById(String tripId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Trip.class, java.util.UUID.fromString(tripId));
        } finally {
            em.close();
        }
    }
}