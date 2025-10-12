package com.busbooking.dao;

import com.busbooking.model.DriverTransport;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
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
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
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
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // === Thêm tài xế mới ===
    public boolean insert(DriverTransport driver) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(driver);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.out.println("Lỗi insert driver: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    // === Xóa tài xế ===
    public boolean delete(String driverId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Integer id = Integer.parseInt(driverId);
            DriverTransport driver = em.find(DriverTransport.class, id);
            if (driver != null) {
                em.remove(driver);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.out.println("Lỗi delete driver: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // === Lấy tất cả tài xế với eager loading ===
    public List<DriverTransport> selectAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<DriverTransport> query = em.createQuery(
                "SELECT d FROM DriverTransport d LEFT JOIN FETCH d.transportCompany LEFT JOIN FETCH d.updatedBy", 
                DriverTransport.class);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Lỗi selectAll drivers: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    // === Lấy tài xế theo ID ===
    public DriverTransport getById(String driverId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Integer id = Integer.parseInt(driverId);
            return em.createQuery(
                "SELECT d FROM DriverTransport d LEFT JOIN FETCH d.transportCompany LEFT JOIN FETCH d.updatedBy WHERE d.driverId = :driverId", 
                DriverTransport.class)
                .setParameter("driverId", id)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.out.println("Lỗi getById driver: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    public static long countAllDrivers() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(d) FROM DriverTransport d", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    // Lấy danh sách Driver theo trang
    public List<DriverTransport> getDriversByPage(int pageNumber, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT d FROM DriverTransport d " +
                            "LEFT JOIN FETCH d.transportCompany " +
                            "LEFT JOIN FETCH d.updatedBy " +
                            "ORDER BY d.name ASC",
                    DriverTransport.class)
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Lấy tổng số Driver
    public int getTotalDriverCount() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(d) FROM DriverTransport d", Long.class)
                    .getSingleResult();
            return count.intValue(); // ép Long -> int
        } finally {
            em.close();
        }
    }
}
