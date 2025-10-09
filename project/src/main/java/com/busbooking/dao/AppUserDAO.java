package com.busbooking.dao;

import com.busbooking.model.AppUser;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class AppUserDAO {

    /** Tìm user theo email */
    public AppUser findByEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT u FROM AppUser u WHERE u.email = :email",
                    AppUser.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
