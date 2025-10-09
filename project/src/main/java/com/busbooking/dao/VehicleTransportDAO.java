package com.busbooking.dao;

import com.busbooking.model.VehicleTransport;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class VehicleTransportDAO {

    public boolean vehicleExists(Integer vehicleId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(v) FROM VehicleTransport v WHERE v.vehicleId = :id", Long.class)
                    .setParameter("id", vehicleId)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public List<VehicleTransport> getAllVehicles() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT v FROM VehicleTransport v ORDER BY v.licensePlate ASC",
                    VehicleTransport.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public void save(VehicleTransport vehicle) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(vehicle);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(VehicleTransport vehicle) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(vehicle);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
