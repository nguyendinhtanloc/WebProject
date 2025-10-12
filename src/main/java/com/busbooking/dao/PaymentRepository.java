package com.busbooking.dao;

import com.busbooking.entity.Payment;
import com.busbooking.entity.PaymentMethod;
import com.busbooking.entity.Voucher;
import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class PaymentRepository {

    public void save(Object entity) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Lỗi khi lưu entity: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public <T> T update(T entity) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            T mergedEntity = em.merge(entity); // Lệnh merge để cập nhật
            transaction.commit();
            return mergedEntity;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Lỗi khi cập nhật entity: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public Payment findPaymentById(Long paymentId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Payment.class, paymentId);
        } finally {
            em.close();
        }
    }

    public Payment findPaymentByTransactionNo(String transactionNo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Payment> query = em.createQuery(
                    "SELECT p FROM Payment p WHERE p.transactionNo = :txnNo", Payment.class);
            query.setParameter("txnNo", transactionNo);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // Trả về null nếu không tìm thấy
        } finally {
            em.close();
        }
    }

    public PaymentMethod findPaymentMethodByName(String name) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<PaymentMethod> query = em.createQuery(
                    "SELECT m FROM PaymentMethod m WHERE m.name = :name AND m.status = 'active'", PaymentMethod.class);
            query.setParameter("name", name);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public Voucher findVoucherByCode(String code) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Voucher> query = em.createQuery(
                    "SELECT v FROM Voucher v WHERE v.code = :code AND v.status = 'active'", Voucher.class);
            query.setParameter("code", code);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}