<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Câu hỏi thường gặp - BusBooking</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        :root {
            --primary-color: #1e88e5;
            --primary-dark: #1565c0;
            --secondary-color: #64b5f6;
            --success-color: #4caf50;
            --info-color: #29b6f6;
            --warning-color: #ff9800;
            --danger-color: #f44336;
            --light-color: #f8f9fa;
            --dark-color: #2c3e50;
            --text-secondary: #6c757d;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            line-height: 1.6;
        }

        .header {
            background: linear-gradient(135deg, var(--primary-color), var(--primary-dark));
            color: white;
            padding: 3rem 0;
            text-align: center;
            position: relative;
            overflow: hidden;
        }

        .header::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><circle cx="20" cy="20" r="2" fill="rgba(255,255,255,0.1)"/><circle cx="80" cy="20" r="1.5" fill="rgba(255,255,255,0.1)"/><circle cx="40" cy="60" r="1" fill="rgba(255,255,255,0.1)"/><circle cx="90" cy="70" r="2" fill="rgba(255,255,255,0.1)"/><circle cx="10" cy="80" r="1.5" fill="rgba(255,255,255,0.1)"/></svg>');
            animation: float 20s infinite linear;
        }

        @keyframes float {
            0% { transform: translateY(0px) rotate(0deg); }
            100% { transform: translateY(-100px) rotate(360deg); }
        }

        .header-content {
            position: relative;
            z-index: 10;
        }

        .header h1 {
            font-size: 2.5rem;
            font-weight: 700;
            margin-bottom: 0.5rem;
            text-shadow: 0 2px 4px rgba(0,0,0,0.3);
        }

        .header p {
            font-size: 1.1rem;
            opacity: 0.9;
            max-width: 600px;
            margin: 0 auto;
        }

        .faq-container {
            max-width: 900px;
            margin: -80px auto 50px;
            position: relative;
            z-index: 10;
            padding: 0 20px;
        }

        .faq-card {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.1);
            overflow: hidden;
            margin-bottom: 2rem;
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255,255,255,0.2);
        }

        .faq-header {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            color: white;
            padding: 2rem;
            text-align: center;
            position: relative;
        }

        .faq-header::after {
            content: '';
            position: absolute;
            bottom: -10px;
            left: 50%;
            transform: translateX(-50%);
            width: 0;
            height: 0;
            border-left: 20px solid transparent;
            border-right: 20px solid transparent;
            border-top: 10px solid var(--secondary-color);
        }

        .faq-header h3 {
            font-size: 1.5rem;
            font-weight: 600;
            margin: 0;
        }

        .faq-category {
            border-bottom: 1px solid #f0f0f0;
            cursor: pointer;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            position: relative;
        }

        .faq-category:hover {
            background: linear-gradient(90deg, rgba(30, 136, 229, 0.05), rgba(255,255,255,0));
        }

        .faq-category:last-child {
            border-bottom: none;
        }

        .category-header {
            padding: 1.8rem 2rem;
            display: flex;
            align-items: center;
            gap: 1.2rem;
        }

        .category-icon {
            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
            color: white;
            width: 55px;
            height: 55px;
            border-radius: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.3rem;
            box-shadow: 0 8px 25px rgba(30, 136, 229, 0.3);
            transition: all 0.3s ease;
        }

        .faq-category:hover .category-icon {
            transform: translateY(-2px) scale(1.05);
            box-shadow: 0 12px 35px rgba(30, 136, 229, 0.4);
        }

        .category-content {
            flex: 1;
        }

        .category-title {
            font-weight: 600;
            font-size: 1.2rem;
            color: var(--dark-color);
            margin-bottom: 0.3rem;
        }

        .category-subtitle {
            color: var(--text-secondary);
            font-size: 0.95rem;
            font-weight: 400;
        }

        .category-arrow {
            color: var(--text-secondary);
            font-size: 1.1rem;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        }

        .category-questions {
            display: none;
            background: #fafbfc;
            padding: 0;
            border-top: 1px solid #e9ecef;
        }

        .question-item {
            border-bottom: 1px solid #e9ecef;
            cursor: pointer;
            transition: all 0.3s ease;
            background: white;
            margin: 0.5rem;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0,0,0,0.05);
        }

        .question-item:hover {
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            transform: translateY(-1px);
        }

        .question-item:last-child {
            border-bottom: none;
            margin-bottom: 1rem;
        }

        .question-header {
            padding: 1.2rem 1.5rem;
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 1rem;
        }

        .question-text {
            font-weight: 500;
            color: var(--dark-color);
            font-size: 1rem;
            margin: 0;
        }

        .question-arrow {
            color: var(--primary-color);
            font-size: 1rem;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            flex-shrink: 0;
        }

        .answer {
            display: none;
            padding: 0 1.5rem 1.5rem;
            background: #f8f9fa;
            color: var(--dark-color);
            line-height: 1.7;
            border-top: 1px solid #e9ecef;
        }

        .answer p {
            margin-bottom: 0.8rem;
        }

        .answer p:last-child {
            margin-bottom: 0;
        }

        .answer strong {
            color: var(--primary-color);
            font-weight: 600;
        }

        .back-btn {
            position: fixed;
            top: 25px;
            left: 25px;
            background: white;
            border: none;
            border-radius: 50px;
            padding: 15px 25px;
            box-shadow: 0 8px 30px rgba(0,0,0,0.15);
            color: var(--primary-color);
            text-decoration: none;
            display: flex;
            align-items: center;
            gap: 10px;
            font-weight: 500;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            z-index: 1000;
            backdrop-filter: blur(10px);
        }

        .back-btn:hover {
            background: var(--primary-color);
            color: white;
            transform: translateY(-3px);
            box-shadow: 0 15px 40px rgba(30, 136, 229, 0.4);
        }

        .support-card {
            background: linear-gradient(135deg, var(--success-color), #66bb6a);
            color: white;
            padding: 2.5rem;
            border-radius: 20px;
            text-align: center;
            margin-top: 2rem;
            position: relative;
            overflow: hidden;
        }

        .support-card::before {
            content: '';
            position: absolute;
            top: -50%;
            left: -50%;
            width: 200%;
            height: 200%;
            background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
            animation: shimmer 3s infinite;
        }

        @keyframes shimmer {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        .support-content {
            position: relative;
            z-index: 10;
        }

        .support-icon {
            font-size: 3.5rem;
            margin-bottom: 1.5rem;
            opacity: 0.9;
        }

        .support-card h4 {
            font-size: 1.5rem;
            font-weight: 600;
            margin-bottom: 1rem;
        }

        .support-card p {
            font-size: 1rem;
            opacity: 0.9;
            margin-bottom: 2rem;
        }

        .contact-btn {
            background: white;
            color: var(--success-color);
            border: none;
            padding: 15px 35px;
            border-radius: 50px;
            font-weight: 600;
            font-size: 1rem;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            box-shadow: 0 4px 15px rgba(0,0,0,0.2);
            cursor: pointer;
        }

        .contact-btn:hover {
            background: #f8f9fa;
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.3);
        }

        .support-buttons {
            display: flex;
            flex-direction: column;
            gap: 1rem;
            align-items: center;
        }

        .zalo-btn {
            background: #0068FF;
            color: white;
            border: none;
            padding: 15px 35px;
            border-radius: 50px;
            font-weight: 600;
            font-size: 1rem;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            box-shadow: 0 4px 15px rgba(0,104,255,0.3);
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .zalo-btn:hover {
            background: #0051CC;
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(0,104,255,0.4);
        }

        @media (min-width: 480px) {
            .support-buttons {
                flex-direction: row;
                justify-content: center;
            }
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            .header {
                padding: 2rem 0;
            }
            
            .header h1 {
                font-size: 2rem;
            }
            
            .faq-container {
                margin: -50px 15px 30px;
                padding: 0;
            }
            
            .faq-header {
                padding: 1.5rem;
            }
            
            .category-header {
                padding: 1.2rem 1rem;
                gap: 1rem;
            }
            
            .category-icon {
                width: 45px;
                height: 45px;
                font-size: 1.1rem;
            }
            
            .category-title {
                font-size: 1.1rem;
            }
            
            .question-header {
                padding: 1rem;
            }
            
            .back-btn {
                top: 15px;
                left: 15px;
                padding: 12px 20px;
                font-size: 0.9rem;
            }
            
            .support-card {
                padding: 2rem 1.5rem;
            }
        }

        /* Animation for smooth opening */
        .category-questions.show {
            animation: slideDown 0.3s ease-out;
        }

        .answer.show {
            animation: fadeIn 0.3s ease-out;
        }

        @keyframes slideDown {
            from {
                opacity: 0;
                max-height: 0;
            }
            to {
                opacity: 1;
                max-height: 1000px;
            }
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(-10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
    </style>
</head>
<body>
    <a href="/Web-project/" class="back-btn">
        <i class="fas fa-arrow-left"></i>
        Quay lại
    </a>

    <div class="header">
        <div class="container">
            <div class="header-content">
                <h1><i class="fas fa-question-circle me-3"></i>Câu hỏi thường gặp</h1>
                <p>Tìm câu trả lời cho những thắc mắc phổ biến về dịch vụ đặt vé xe của chúng tôi</p>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="faq-container">
            <div class="faq-card">
                <div class="faq-header">
                    <h3><i class="fas fa-life-ring me-2"></i>Trung tâm hỗ trợ BusBooking</h3>
                </div>

                <!-- Đặt vé & Thanh toán -->
                <div class="faq-category" onclick="toggleCategory('booking')">
                    <div class="category-header">
                        <div class="category-icon">
                            <i class="fas fa-ticket-alt"></i>
                        </div>
                        <div class="category-content">
                            <h5 class="category-title">Đặt vé & Thanh toán</h5>
                            <p class="category-subtitle">Hướng dẫn đặt vé và các phương thức thanh toán</p>
                        </div>
                        <i class="fas fa-chevron-right category-arrow" id="arrow-booking"></i>
                    </div>
                    <div class="category-questions" id="questions-booking">
                        <div class="question-item" onclick="toggleQuestion('q1')">
                            <div class="question-header">
                                <p class="question-text">Làm thế nào để đặt vé xe khách online?</p>
                                <i class="fas fa-chevron-down question-arrow" id="arrow-q1"></i>
                            </div>
                            <div class="answer" id="answer-q1">
                                <p><strong>Bước 1:</strong> Truy cập website BusBooking và chọn điểm đi, điểm đến, ngày khởi hành.</p>
                                <p><strong>Bước 2:</strong> Click "Tìm kiếm" để xem danh sách các chuyến xe có sẵn.</p>
                                <p><strong>Bước 3:</strong> Chọn chuyến xe phù hợp và click "Đặt vé".</p>
                                <p><strong>Bước 4:</strong> Chọn ghế ngồi trên sơ đồ xe.</p>
                                <p><strong>Bước 5:</strong> Điền thông tin hành khách và thực hiện thanh toán.</p>
                                <p><strong>Bước 6:</strong> Nhận mã đặt vé qua email/SMS để sử dụng khi lên xe.</p>
                            </div>
                        </div>

                        <div class="question-item" onclick="toggleQuestion('q2')">
                            <div class="question-header">
                                <p class="question-text">Tôi có thể hủy vé đã đặt như thế nào?</p>
                                <i class="fas fa-chevron-down question-arrow" id="arrow-q2"></i>
                            </div>
                            <div class="answer" id="answer-q2">
                                <p>Bạn có thể hủy vé theo các cách sau:</p>
                                <p><strong>• Online:</strong> Đăng nhập tài khoản → Vé của tôi → Chọn vé cần hủy → Click "Hủy vé"</p>
                                <p><strong>• Hotline:</strong> Gọi 1900-6067 với mã đặt vé</p>
                                <p><strong>Lưu ý:</strong> Phí hủy vé tùy thuộc vào thời gian hủy:</p>
                                <p>- Hủy trước 24h: Phí 10% giá vé</p>
                                <p>- Hủy trước 12h: Phí 20% giá vé</p>
                                <p>- Hủy trước 4h: Phí 50% giá vé</p>
                                <p>- Hủy trong vòng 4h: Không được hoàn tiền</p>
                            </div>
                        </div>

                        <div class="question-item" onclick="toggleQuestion('q3')">
                            <div class="question-header">
                                <p class="question-text">Tôi có thể đổi vé bằng cách nào?</p>
                                <i class="fas fa-chevron-down question-arrow" id="arrow-q3"></i>
                            </div>
                            <div class="answer" id="answer-q3">
                                <p>Bạn có thể đổi vé theo quy định sau:</p>
                                <p><strong>• Đổi lịch trình:</strong> Được phép đổi 1 lần, trước giờ khởi hành 4 tiếng</p>
                                <p><strong>• Phí đổi vé:</strong> 20,000 VNĐ + phần chênh lệch giá vé (nếu có)</p>
                                <p><strong>• Cách thức:</strong></p>
                                <p>- Truy cập website → Đăng nhập → Vé của tôi → Chọn "Đổi vé"</p>
                                <p>- Gọi hotline 1900-6067</p>
                                <p>- Đến quầy vé tại bến xe</p>
                                <p><strong>Lưu ý:</strong> Chỉ được đổi sang chuyến cùng tuyến đường và cùng loại xe</p>
                            </div>
                        </div>

                        <div class="question-item" onclick="toggleQuestion('q4')">
                            <div class="question-header">
                                <p class="question-text">Các phương thức thanh toán nào được hỗ trợ?</p>
                                <i class="fas fa-chevron-down question-arrow" id="arrow-q4"></i>
                            </div>
                            <div class="answer" id="answer-q4">
                                <p>BusBooking hỗ trợ các phương thức thanh toán sau:</p>
                                <p><strong>• Ví điện tử:</strong></p>
                                <p>- VNPay, ZaloPay, MoMo, ShopeePay</p>
                                <p><strong>• Thẻ ngân hàng:</strong></p>
                                <p>- Thẻ ATM nội địa (Visa, Mastercard)</p>
                                <p>- Thẻ tín dụng quốc tế</p>
                                <p><strong>• Chuyển khoản ngân hàng:</strong></p>
                                <p>- Internet Banking, Mobile Banking</p>
                                <p><strong>• Thanh toán tại quầy:</strong></p>
                                <p>- Tiền mặt tại các điểm bán vé</p>
                                <p><strong>Lưu ý:</strong> Tất cả giao dịch đều được bảo mật SSL 256-bit</p>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Chính sách chung -->
                <div class="faq-category" onclick="toggleCategory('policy')">
                    <div class="category-header">
                        <div class="category-icon">
                            <i class="fas fa-shield-alt"></i>
                        </div>
                        <div class="category-content">
                            <h5 class="category-title">Chính sách chung</h5>
                            <p class="category-subtitle">Quy định và chính sách sử dụng dịch vụ</p>
                        </div>
                        <i class="fas fa-chevron-right category-arrow" id="arrow-policy"></i>
                    </div>
                    <div class="category-questions" id="questions-policy">
                        <div class="question-item" onclick="toggleQuestion('q5')">
                            <div class="question-header">
                                <p class="question-text">Quy định về hành lý khi đi xe?</p>
                                <i class="fas fa-chevron-down question-arrow" id="arrow-q5"></i>
                            </div>
                            <div class="answer" id="answer-q5">
                                <p><strong>Hành lý xách tay:</strong></p>
                                <p>- Tối đa 7kg, kích thước 56x36x23cm</p>
                                <p>- Miễn phí, để dưới chân hoặc kệ trên đầu</p>
                                <p><strong>Hành lý ký gửi:</strong></p>
                                <p>- Tối đa 20kg/hành khách</p>
                                <p>- Phí: 10,000 VNĐ/kg (từ kg thứ 21)</p>
                                <p><strong>Đồ vật cấm mang:</strong></p>
                                <p>- Chất dễ cháy, nổ, độc hại</p>
                                <p>- Vũ khí, công cụ hỗ trợ</p>
                                <p>- Động vật sống (trừ chó dẫn đường)</p>
                                <p>- Thực phẩm có mùi nặng</p>
                            </div>
                        </div>

                        <div class="question-item" onclick="toggleQuestion('q6')">
                            <div class="question-header">
                                <p class="question-text">Tôi có cần giấy tờ gì khi lên xe?</p>
                                <i class="fas fa-chevron-down question-arrow" id="arrow-q6"></i>
                            </div>
                            <div class="answer" id="answer-q6">
                                <p><strong>Giấy tờ bắt buộc:</strong></p>
                                <p>- CMND/CCCD/Hộ chiếu (bản gốc hoặc photo có công chứng)</p>
                                <p>- Mã đặt vé (SMS/Email xác nhận)</p>
                                <p><strong>Đối với trẻ em:</strong></p>
                                <p>- Dưới 6 tuổi: Giấy khai sinh + đi cùng người lớn</p>
                                <p>- Từ 6-14 tuổi: Giấy khai sinh (có thể đi một mình)</p>
                                <p>- Từ 15 tuổi: CMND/CCCD</p>
                                <p><strong>Lưu ý:</strong> Tên trên vé phải khớp với giấy tờ tùy thân</p>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Hỗ trợ kỹ thuật -->
                <div class="faq-category" onclick="toggleCategory('support')">
                    <div class="category-header">
                        <div class="category-icon">
                            <i class="fas fa-headset"></i>
                        </div>
                        <div class="category-content">
                            <h5 class="category-title">Hỗ trợ kỹ thuật</h5>
                            <p class="category-subtitle">Giải đáp các vấn đề kỹ thuật và sử dụng website</p>
                        </div>
                        <i class="fas fa-chevron-right category-arrow" id="arrow-support"></i>
                    </div>
                    <div class="category-questions" id="questions-support">
                        <div class="question-item" onclick="toggleQuestion('q7')">
                            <div class="question-header">
                                <p class="question-text">Website bị lỗi, tôi không thể đặt vé được?</p>
                                <i class="fas fa-chevron-down question-arrow" id="arrow-q7"></i>
                            </div>
                            <div class="answer" id="answer-q7">
                                <p><strong>Các bước khắc phục:</strong></p>
                                <p><strong>1. Làm mới trang:</strong> Nhấn F5 hoặc Ctrl+F5</p>
                                <p><strong>2. Xóa cache:</strong> Ctrl+Shift+Delete → Xóa dữ liệu duyệt web</p>
                                <p><strong>3. Thử trình duyệt khác:</strong> Chrome, Firefox, Edge</p>
                                <p><strong>4. Kiểm tra kết nối:</strong> Đảm bảo internet ổn định</p>
                                <p><strong>5. Tắt phần mềm chặn quảng cáo</strong></p>
                                <p><strong>Nếu vẫn lỗi:</strong></p>
                                <p>- Gọi hotline: 1900-6067</p>
                                <p>- Chat trực tuyến trên website</p>
                                <p>- Email: support@busbooking.vn</p>
                            </div>
                        </div>

                        <div class="question-item" onclick="toggleQuestion('q8')">
                            <div class="question-header">
                                <p class="question-text">Tôi quên mật khẩu tài khoản, làm sao để lấy lại?</p>
                                <i class="fas fa-chevron-down question-arrow" id="arrow-q8"></i>
                            </div>
                            <div class="answer" id="answer-q8">
                                <p><strong>Cách lấy lại mật khẩu:</strong></p>
                                <p><strong>Bước 1:</strong> Truy cập trang đăng nhập</p>
                                <p><strong>Bước 2:</strong> Click "Quên mật khẩu?"</p>
                                <p><strong>Bước 3:</strong> Nhập email/số điện thoại đã đăng ký</p>
                                <p><strong>Bước 4:</strong> Kiểm tra email/SMS nhận được</p>
                                <p><strong>Bước 5:</strong> Click link đặt lại mật khẩu</p>
                                <p><strong>Bước 6:</strong> Tạo mật khẩu mới và xác nhận</p>
                                <p><strong>Lưu ý:</strong> Link có hiệu lực trong 15 phút. Nếu không nhận được email, kiểm tra thư mục Spam.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Support Card -->
            <div class="support-card">
                <div class="support-content">
                    <div class="support-icon">
                        <i class="fas fa-comments"></i>
                    </div>
                    <h4>Cần hỗ trợ thêm?</h4>
                    <p>Đội ngũ hỗ trợ 24/7 sẵn sàng giải đáp mọi thắc mắc của bạn</p>
                    <div class="support-buttons">
                        <button class="contact-btn" onclick="openSupport()">
                            <i class="fas fa-phone me-2"></i>Liên hệ hỗ trợ
                        </button>
                        <button class="zalo-btn" onclick="openZalo()">
                            <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor" class="me-2">
                                <path d="M12 0C5.373 0 0 5.373 0 12c0 2.176.58 4.22 1.594 5.982L0 24l6.199-1.531C8.048 23.414 9.96 24 12 24c6.627 0 12-5.373 12-12S18.627 0 12 0zm0 21.6c-1.771 0-3.449-.481-4.889-1.32L2.4 21.6l1.374-4.649C2.94 15.512 2.4 13.814 2.4 12c0-5.302 4.298-9.6 9.6-9.6s9.6 4.298 9.6 9.6-4.298 9.6-9.6 9.6z"/>
                                <path d="M17.2 14.4c-.2-.1-1.2-.6-1.4-.7-.2-.1-.3-.1-.4.1-.1.2-.5.7-.6.8-.1.1-.2.1-.4 0-.2-.1-.8-.3-1.5-.9-.6-.5-.9-1.1-1-1.3-.1-.2 0-.3.1-.4.1-.1.2-.2.3-.3.1-.1.1-.2.2-.3.1-.1 0-.2 0-.3-.1-.1-.4-1-.6-1.4-.2-.4-.4-.3-.6-.3h-.5c-.2 0-.5.1-.8.4-.3.3-1.1 1.1-1.1 2.7s1.2 3.1 1.3 3.3c.1.2 2.1 3.2 5.1 4.5.7.3 1.3.5 1.7.6.7.2 1.4.2 1.9.1.6-.1 1.8-.7 2.1-1.4.3-.7.3-1.3.2-1.4-.1-.1-.3-.2-.5-.3z"/>
                            </svg>
                            Chat Zalo
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        function toggleCategory(categoryId) {
            const questions = document.getElementById('questions-' + categoryId);
            const arrow = document.getElementById('arrow-' + categoryId);
            
            if (questions.style.display === 'block') {
                questions.style.display = 'none';
                questions.classList.remove('show');
                arrow.style.transform = 'rotate(0deg)';
            } else {
                // Close all other categories
                document.querySelectorAll('.category-questions').forEach(q => {
                    q.style.display = 'none';
                    q.classList.remove('show');
                });
                document.querySelectorAll('.category-arrow').forEach(a => a.style.transform = 'rotate(0deg)');
                
                // Close all open answers
                document.querySelectorAll('.answer').forEach(a => {
                    a.style.display = 'none';
                    a.classList.remove('show');
                });
                document.querySelectorAll('.question-arrow').forEach(a => a.style.transform = 'rotate(0deg)');
                
                questions.style.display = 'block';
                questions.classList.add('show');
                arrow.style.transform = 'rotate(90deg)';
            }
        }

        function toggleQuestion(questionId) {
            event.stopPropagation();
            const answer = document.getElementById('answer-' + questionId);
            const arrow = document.getElementById('arrow-' + questionId);
            
            if (answer.style.display === 'block') {
                answer.style.display = 'none';
                answer.classList.remove('show');
                arrow.style.transform = 'rotate(0deg)';
            } else {
                answer.style.display = 'block';
                answer.classList.add('show');
                arrow.style.transform = 'rotate(180deg)';
            }
        }

        function openSupport() {
            // Create a nice modal instead of alert
            const modalHtml = `
                <div style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 10000; display: flex; align-items: center; justify-content: center;" onclick="this.remove()">
                    <div style="background: white; padding: 2rem; border-radius: 20px; text-align: center; max-width: 400px; margin: 20px; box-shadow: 0 20px 60px rgba(0,0,0,0.3);" onclick="event.stopPropagation()">
                        <div style="color: #1e88e5; font-size: 3rem; margin-bottom: 1rem;">
                            <i class="fas fa-headset"></i>
                        </div>
                        <h3 style="color: #2c3e50; margin-bottom: 1rem;">Liên hệ hỗ trợ</h3>
                        <div style="text-align: left; margin-bottom: 1.5rem;">
                            <p style="margin: 0.5rem 0;"><strong>📞 Hotline:</strong> 1900-6067</p>
                            <p style="margin: 0.5rem 0;"><strong>📧 Email:</strong> support@busbooking.vn</p>
                            <p style="margin: 0.5rem 0;"><strong>💬 Chat:</strong> 24/7 trực tuyến</p>
                            <p style="margin: 0.5rem 0;"><strong>🕒 Thời gian:</strong> Hỗ trợ 24/7</p>
                        </div>
                        <button onclick="this.closest('[style*=\"position: fixed\"]').remove()" style="background: #1e88e5; color: white; border: none; padding: 12px 30px; border-radius: 50px; cursor: pointer; font-weight: 600;">
                            Đóng
                        </button>
                    </div>
                </div>
            `;
            document.body.insertAdjacentHTML('beforeend', modalHtml);
        }

        function openZalo() {
            // Replace this URL with your Zalo page/chat link
            const zaloUrl = "https://zalo.me/0908894993"; // Update this with your actual Zalo link
            
            // Open Zalo in a new window/tab
            window.open(zaloUrl, '_blank');
            
            // Optional: Show a message if user prefers
            // You can also create a modal like the support function above
        }

        // Smooth scroll and enhanced interactions
        document.addEventListener('DOMContentLoaded', function() {
            // Add smooth scrolling
            document.querySelectorAll('a[href^="#"]').forEach(anchor => {
                anchor.addEventListener('click', function (e) {
                    e.preventDefault();
                    document.querySelector(this.getAttribute('href')).scrollIntoView({
                        behavior: 'smooth'
                    });
                });
            });

            // Add hover effects
            document.querySelectorAll('.faq-category').forEach(category => {
                category.addEventListener('mouseenter', function() {
                    this.style.background = 'linear-gradient(90deg, rgba(30, 136, 229, 0.08), rgba(255,255,255,0))';
                });
                
                category.addEventListener('mouseleave', function() {
                    this.style.background = '';
                });
            });
        });

        // Close categories when clicking outside
        document.addEventListener('click', function(event) {
            if (!event.target.closest('.faq-category') && !event.target.closest('.question-item')) {
                document.querySelectorAll('.category-questions').forEach(q => {
                    q.style.display = 'none';
                    q.classList.remove('show');
                });
                document.querySelectorAll('.category-arrow').forEach(a => a.style.transform = 'rotate(0deg)');
            }
        });

        // Add loading animation
        window.addEventListener('load', function() {
            document.body.style.opacity = '0';
            document.body.style.transform = 'translateY(20px)';
            document.body.style.transition = 'all 0.6s ease';
            
            setTimeout(() => {
                document.body.style.opacity = '1';
                document.body.style.transform = 'translateY(0)';
            }, 100);
        });
    </script>
</body>
</html>