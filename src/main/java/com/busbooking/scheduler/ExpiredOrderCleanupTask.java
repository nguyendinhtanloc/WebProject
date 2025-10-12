package com.busbooking.scheduler;

import com.busbooking.dao.OrderRepository;
import com.busbooking.dao.SeatDAO;
import com.busbooking.entity.Order;
import com.busbooking.entity.Seat;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Lớp này vừa là Listener để lắng nghe sự kiện khởi động/dừng của ứng dụng,
 * vừa chứa Task để thực hiện công việc dọn dẹp định kỳ.
 */
@WebListener // Annotation quan trọng để server tự động nhận diện
public class ExpiredOrderCleanupTask implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    /**
     * Phương thức này được gọi KHI ỨNG DỤNG KHỞI ĐỘNG 🚀
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newSingleThreadScheduledExecutor();

        // Lên lịch cho tác vụ chạy 5 phút một lần,
        // bắt đầu sau 1 phút kể từ khi khởi động.
        scheduler.scheduleAtFixedRate(new CleanupTask(), 1, 1, TimeUnit.MINUTES);

        System.out.println("=================================================");
        System.out.println("  BỘ HẸN GIỜ DỌN DẸP ĐƠN HÀNG ĐÃ KHỞI ĐỘNG!  ");
        System.out.println("=================================================");
    }

    /**
     * Phương thức này được gọi KHI ỨNG DỤNG DỪNG LẠI 🛑
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
        System.out.println("==============================================");
        System.out.println("  BỘ HẸN GIỜ DỌN DẸP ĐƠN HÀNG ĐÃ TẮT.  ");
        System.out.println("==============================================");
    }

    /**
     * Đây là lớp nội chứa logic của tác vụ dọn dẹp.
     * Vì nó là private, chỉ có lớp ExpiredOrderCleanupScheduler thấy và sử dụng được.
     */
    private static class CleanupTask implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println("SCHEDULER: Bắt đầu quét các đơn hàng hết hạn lúc " + LocalDateTime.now());

                OrderRepository orderRepository = new OrderRepository();

                // Chỉ cần gọi MỘT phương thức duy nhất
                int deletedCount = orderRepository.cleanupExpiredOrders();

                if (deletedCount > 0) {
                    System.out.println("SCHEDULER: Đã dọn dẹp và xóa " + deletedCount + " đơn hàng hết hạn.");
                } else {
                    System.out.println("SCHEDULER: Không tìm thấy đơn hàng nào hết hạn để dọn dẹp.");
                }

            } catch (Exception e) {
                System.err.println("SCHEDULER: Đã xảy ra lỗi nghiêm trọng trong quá trình dọn dẹp.");
                e.printStackTrace();
            }
        }
    }
}