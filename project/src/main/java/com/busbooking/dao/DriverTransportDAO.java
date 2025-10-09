package com.busbooking.dao;

import com.busbooking.model.DriverTransport;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class DriverTransportDAO {

    public boolean driverExists(Integer driverId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(d) FROM DriverTransport d WHERE d.driverId = :id", Long.class)
                    .setParameter("id", driverId)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public List<DriverTransport> getAllDrivers() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT d FROM DriverTransport d ORDER BY d.name ASC", DriverTransport.class)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    public void save(DriverTransport driver) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(driver);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void update(DriverTransport driver) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(driver);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
