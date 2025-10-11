package com.busbooking.dao;

import com.busbooking.entity.Trip;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TripDAO {
    
    public List<Trip> searchTrips(String departureCity, String arrivalCity, LocalDate departureDate) {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            String jpql = "SELECT t FROM Trip t " +
                         "LEFT JOIN FETCH t.busCompany " +
                         "LEFT JOIN FETCH t.vehicle " +
                         "WHERE LOWER(t.departureCity) LIKE LOWER(:departureCity) " +
                         "AND LOWER(t.arrivalCity) LIKE LOWER(:arrivalCity) " +
                         "AND t.departureDate = :departureDate " +
                         "ORDER BY t.departureTime";
            
            TypedQuery<Trip> query = em.createQuery(jpql, Trip.class);
            query.setParameter("departureCity", "%" + departureCity + "%");
            query.setParameter("arrivalCity", "%" + arrivalCity + "%");
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
    
    public List<String> getAllDepartureCities() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT t.departureCity FROM Trip t " +
                         "WHERE t.departureCity IS NOT NULL " +
                         "ORDER BY t.departureCity";
            
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<String> getAllArrivalCities() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT t.arrivalCity FROM Trip t " +
                         "WHERE t.arrivalCity IS NOT NULL " +
                         "ORDER BY t.arrivalCity";
            
            TypedQuery<String> query = em.createQuery(jpql, String.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Trip findById(Integer tripId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Trip.class, tripId);
        } finally {
            em.close();
        }
    }
}