// package com.busbooking.util;

// import com.busbooking.util.JPAUtil;

// import javax.persistence.EntityManager;
// import javax.servlet.ServletException;
// import javax.servlet.annotation.WebServlet;
// import javax.servlet.http.*;
// import java.io.IOException;

// @WebServlet("/test-db")
// public class TestDBServlet extends HttpServlet {
//     @Override
//     protected void doGet(HttpServletRequest request, HttpServletResponse response)
//             throws ServletException, IOException {
//         try {
//             EntityManager em = JPAUtil.getEntityManager();
//             Object result = em.createNativeQuery("SELECT 1").getSingleResult();
//             em.close();
//             response.getWriter().println("DB connection OK, SELECT 1 = " + result);
//         } catch (Exception e) {
//             e.printStackTrace(response.getWriter());
//         }
//     }
// }
