package com.busbooking.dao;

import com.busbooking.model.VehicleTransport;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

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

    public boolean insert(VehicleTransport vehicle) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(vehicle);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.out.println("Lỗi insert: " + e.getMessage());
            e.printStackTrace();
            return false;
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

    public List<VehicleTransport> selectAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<VehicleTransport> query = em.createQuery(
                "SELECT v FROM VehicleTransport v LEFT JOIN FETCH v.transportCompany LEFT JOIN FETCH v.updatedBy", VehicleTransport.class);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Lỗi selectAll: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    // === Xóa phương tiện ===
    public boolean delete(String vehicleId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Integer id = Integer.parseInt(vehicleId);
            VehicleTransport vehicle = em.find(VehicleTransport.class, id);
            if (vehicle != null) {
                em.remove(vehicle);
                tx.commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            System.out.println("Lỗi delete: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    public VehicleTransport getById(String vehicleId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Integer id = Integer.parseInt(vehicleId);
            return em.createQuery(
                "SELECT v FROM VehicleTransport v LEFT JOIN FETCH v.transportCompany LEFT JOIN FETCH v.updatedBy WHERE v.vehicleId = :vehicleId", VehicleTransport.class)
                .setParameter("vehicleId", id)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public static long countAllVehicles() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(v) FROM VehicleTransport v", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }

    // Lấy danh sách Vehicle theo trang
    public List<VehicleTransport> getVehiclesByPage(int pageNumber, int pageSize) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT v FROM VehicleTransport v " +
                            "LEFT JOIN FETCH v.transportCompany " +
                            "LEFT JOIN FETCH v.updatedBy " +
                            "ORDER BY v.licensePlate ASC",
                    VehicleTransport.class)
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Lấy tổng số Vehicle
    public int getTotalVehicleCount() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(v) FROM VehicleTransport v", Long.class)
                    .getSingleResult();
            return count.intValue(); // ép Long -> int
        } finally {
            em.close();
        }
    }
}
