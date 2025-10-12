package com.busbooking.dao;

import com.busbooking.entity.Order;
import com.busbooking.util.JPAUtil;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class OrderRepository {

    public Order findById(Long orderId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Order.class, orderId);
        } finally {
            em.close();
        }
    }

    public Order addOrder(Order order) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            order = em.merge(order);
            tx.commit();
            return order;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }
}
