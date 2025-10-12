package com.busbooking.dao;

import com.busbooking.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class PaymentDAO {
    
    /**
     * Tính tổng doanh thu theo trạng thái thành công
     */
    public static BigDecimal getTotalRevenue() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COALESCE(SUM(p.finalAmount), 0) FROM Payment p WHERE p.status = 'success'";
            TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        } finally {
            em.close();
        }
    }
    
    /**
     * Tính tổng doanh thu theo tháng hiện tại
     */
    public static BigDecimal getMonthlyRevenue() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            LocalDateTime startOfMonth = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime endOfMonth = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);
            
            String jpql = "SELECT COALESCE(SUM(p.finalAmount), 0) FROM Payment p " +
                         "WHERE p.status = 'success' AND p.paidAt >= :startDate AND p.paidAt <= :endDate";
            TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
            query.setParameter("startDate", startOfMonth);
            query.setParameter("endDate", endOfMonth);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        } finally {
            em.close();
        }
    }
    
    /**
     * Tính tổng doanh thu theo năm hiện tại
     */
    public static BigDecimal getYearlyRevenue() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            LocalDateTime startOfYear = LocalDateTime.now().with(TemporalAdjusters.firstDayOfYear()).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime endOfYear = LocalDateTime.now().with(TemporalAdjusters.lastDayOfYear()).withHour(23).withMinute(59).withSecond(59);
            
            String jpql = "SELECT COALESCE(SUM(p.finalAmount), 0) FROM Payment p " +
                         "WHERE p.status = 'success' AND p.paidAt >= :startDate AND p.paidAt <= :endDate";
            TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
            query.setParameter("startDate", startOfYear);
            query.setParameter("endDate", endOfYear);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        } finally {
            em.close();
        }
    }
    
    /**
     * Đếm tổng số giao dịch thành công
     */
    public static long countSuccessfulPayments() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT COUNT(p) FROM Payment p WHERE p.status = 'success'";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        } finally {
            em.close();
        }
    }
    
    /**
     * Lấy dữ liệu doanh thu và số giao dịch 7 ngày gần nhất
     * Trả về chuỗi JSON để sử dụng trong JavaScript
     */
    public static String getLast7DaysData() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{\"dates\":[");
            
            // Tạo danh sách 7 ngày gần nhất
            LocalDateTime[] dates = new LocalDateTime[7];
            for (int i = 6; i >= 0; i--) {
                dates[6-i] = LocalDateTime.now().minusDays(i).withHour(0).withMinute(0).withSecond(0);
            }
            
            // Lấy dữ liệu cho từng ngày
            StringBuilder dateLabels = new StringBuilder();
            StringBuilder revenueData = new StringBuilder();
            StringBuilder countData = new StringBuilder();
            
            for (int i = 0; i < 7; i++) {
                LocalDateTime startOfDay = dates[i];
                LocalDateTime endOfDay = startOfDay.withHour(23).withMinute(59).withSecond(59);
                
                // Lấy doanh thu của ngày
                String revenueJpql = "SELECT COALESCE(SUM(p.finalAmount), 0) FROM Payment p " +
                                   "WHERE p.status = 'success' AND p.paidAt >= :startDate AND p.paidAt <= :endDate";
                TypedQuery<BigDecimal> revenueQuery = em.createQuery(revenueJpql, BigDecimal.class);
                revenueQuery.setParameter("startDate", startOfDay);
                revenueQuery.setParameter("endDate", endOfDay);
                BigDecimal dayRevenue = revenueQuery.getSingleResult();
                
                // Lấy số giao dịch của ngày
                String countJpql = "SELECT COUNT(p) FROM Payment p " +
                                 "WHERE p.status = 'success' AND p.paidAt >= :startDate AND p.paidAt <= :endDate";
                TypedQuery<Long> countQuery = em.createQuery(countJpql, Long.class);
                countQuery.setParameter("startDate", startOfDay);
                countQuery.setParameter("endDate", endOfDay);
                Long dayCount = countQuery.getSingleResult();
                
                // Format ngày (dd/MM)
                String dayLabel = String.format("%02d/%02d", 
                    startOfDay.getDayOfMonth(), 
                    startOfDay.getMonthValue());
                
                if (i > 0) {
                    dateLabels.append(",");
                    revenueData.append(",");
                    countData.append(",");
                }
                
                dateLabels.append("\"").append(dayLabel).append("\"");
                revenueData.append(dayRevenue.longValue());
                countData.append(dayCount);
            }
            
            jsonBuilder.append(dateLabels.toString());
            jsonBuilder.append("],\"revenue\":[");
            jsonBuilder.append(revenueData.toString());
            jsonBuilder.append("],\"count\":[");
            jsonBuilder.append(countData.toString());
            jsonBuilder.append("]}");
            
            return jsonBuilder.toString();
            
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"dates\":[],\"revenue\":[],\"count\":[]}";
        } finally {
            em.close();
        }
    }
}