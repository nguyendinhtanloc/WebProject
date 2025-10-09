// package com.busbooking.controller;

// import com.busbooking.model.AppUser;
// import com.busbooking.util.JPAUtil;

// import javax.persistence.EntityManager;
// import javax.servlet.ServletException;
// import javax.servlet.annotation.WebServlet;
// import javax.servlet.http.*;
// import java.io.IOException;

// @WebServlet("/test-user")
// public class TestUserServlet extends HttpServlet {
//     @Override
//     protected void doGet(HttpServletRequest request, HttpServletResponse response)
//             throws ServletException, IOException {

//         String testEmail = request.getParameter("email"); // ví dụ ?email=abc@xyz.com

//         try {
//             EntityManager em = JPAUtil.getEntityManager();
//             AppUser user = em.createQuery(
//                     "SELECT u FROM AppUser u WHERE LOWER(u.email) = LOWER(:email)", AppUser.class)
//                     .setParameter("email", testEmail)
//                     .getSingleResult();
//             em.close();
//             response.getWriter().println("User found: " + user.getName() + " | ID: " + user.getUserId());
//         } catch (javax.persistence.NoResultException e) {
//             response.getWriter().println("User not found!");
//         } catch (Exception e) {
//             e.printStackTrace(response.getWriter());
//         }
//     }
// }
