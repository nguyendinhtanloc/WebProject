package com.busbooking.model;

/**
 * Đây là một lớp Model (hoặc Entity, POJO) siêu cơ bản để chứa thông tin người dùng.
 * Hiện tại mình chỉ cần lưu email của người dùng sau khi họ đăng nhập thành công là đủ.
 * Sau này có thể mở rộng thêm các trường khác như fullName, role, userId,...
 */
public class User {
    // Trường private để lưu email, tuân thủ nguyên tắc đóng gói (encapsulation).
    private String email;

    /**
     * Constructor không tham số (default constructor).
     * Rất quan trọng! Cần có constructor rỗng này để một số thư viện
     * (ví dụ như Jackson khi chuyển đổi JSON sang Object) có thể khởi tạo đối tượng dễ dàng.
     */
    public User() { 
    }
    
    /**
     * Constructor có tham số để tiện cho việc tạo nhanh một đối tượng User
     * và gán email ngay lập tức.
     * @param email Email của người dùng.
     */
    public User(String email) {
        this.email = email;
    }

    // --- Các hàm getter và setter tiêu chuẩn ---
    // Mấy hàm này thì quá quen thuộc rồi, để lấy và gán giá trị cho các trường private.

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}