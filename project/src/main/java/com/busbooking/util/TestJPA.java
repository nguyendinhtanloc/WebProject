// package com.busbooking.util;

// import javax.persistence.EntityManager;
// import javax.persistence.Query;

// public class TestJPA {

//     public static void main(String[] args) {
//         System.out.println("=== Test JPA/Hibernate kết nối DB ===");

//         EntityManager em = null;
//         try {
//             // Lấy EntityManager từ JPAUtil
//             em = JPAUtil.getEntityManager();
//             System.out.println("-> EntityManager khởi tạo thành công!");

//             // Thực hiện truy vấn test (SELECT 1)
//             Query query = em.createNativeQuery("SELECT 1");
//             Object result = query.getSingleResult();

//             System.out.println("-> Truy vấn test thành công, kết quả: " + result);

//         } catch (Exception e) {
//             System.err.println("-> Lỗi khi test JPA/Hibernate:");
//             e.printStackTrace();
//         } finally {
//             if (em != null && em.isOpen()) {
//                 em.close();
//             }
//             // Đóng EntityManagerFactory
//             JPAUtil.close();
//         }

//         System.out.println("=== Kết thúc test ===");
//     }
// }
