package com.busbooking.dao;

import com.busbooking.model.TransportCompany;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class TransportCompanyDAO {

    public boolean companyExists(Integer companyId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(c) FROM TransportCompany c WHERE c.companyId = :id", Long.class)
                    .setParameter("id", companyId)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public List<TransportCompany> getAllCompanies() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM TransportCompany c ORDER BY c.name ASC", TransportCompany.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void save(TransportCompany company) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(company);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void update(TransportCompany company) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(company);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public TransportCompany getById(Integer companyId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(TransportCompany.class, companyId);
        } finally {
            em.close();
        }
    }
}
