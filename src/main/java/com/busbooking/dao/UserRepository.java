package com.busbooking.dao;

import com.busbooking.entity.AppUser;
import com.busbooking.util.JPAUtil;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
// import java.util.UUID; // Import lớp UUID

public class UserRepository {

    public AppUser findById(int userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {

            // ✅ BƯỚC 2: Dùng đối tượng UUID để tìm kiếm
            return em.find(AppUser.class, userId);
        } catch (IllegalArgumentException e) {

            return null;
        } finally {
            em.close();
        }
    }

    // Trong file UserRepository.java

    public AppUser findByEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<AppUser> query = em.createQuery(
                    "SELECT u FROM AppUser u WHERE u.email = :email", AppUser.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // Trả về null nếu không tìm thấy
        } finally {
            em.close();
        }
    }
}