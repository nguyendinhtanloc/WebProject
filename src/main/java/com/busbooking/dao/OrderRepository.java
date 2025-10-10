package com.busbooking.dao;

import com.busbooking.entity.Order;
import com.busbooking.util.JPAUtil;
import javax.persistence.EntityManager;

public class OrderRepository {

    public Order findById(Long orderId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Order.class, orderId);
        } finally {
            em.close();
        }
    }
}
