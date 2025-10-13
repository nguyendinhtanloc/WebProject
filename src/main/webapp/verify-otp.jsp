<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác thực OTP - BusBooking</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .verify-container {
            max-width: 500px;
            margin: 50px auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }

        .verify-header {
            text-align: center;
            margin-bottom: 30px;
        }

        .verify-header h2 {
            color: #2196F3;
            margin-bottom: 10px;
        }

        .verify-header p {
            color: #666;
            font-size: 14px;
        }

        .otp-input {
            width: 100%;
            padding: 15px;
            font-size: 24px;
            text-align: center;
            letter-spacing: 5px;
            border: 2px solid #ddd;
            border-radius: 5px;
            margin-bottom: 20px;
        }

        .otp-input:focus {
            border-color: #2196F3;
            outline: none;
        }

        .btn-verify {
            width: 100%;
            padding: 15px;
            background: #2196F3;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
        }

        .btn-verify:hover {
            background: #1976D2;
        }

        .btn-resend {
            width: 100%;
            padding: 10px;
            background: transparent;
            color: #2196F3;
            border: 1px solid #2196F3;
            border-radius: 5px;
            font-size: 14px;
            cursor: pointer;
            margin-top: 15px;
        }

        .btn-resend:hover {
            background: #2196F3;
            color: white;
        }

        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
        }

        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .back-link {
            display: block;
            text-align: center;
            margin-top: 20px;
            color: #666;
            text-decoration: none;
        }

        .back-link:hover {
            color: #2196F3;
        }

        .email-display {
            background: #f8f9fa;
            padding: 10px;
            border-radius: 5px;
            text-align: center;
            margin-bottom: 20px;
            font-weight: bold;
color: #495057;
        }
    </style>
</head>
<body>
    <div class="verify-container">
        <div class="verify-header">
            <h2>🚌 Xác thực OTP</h2>
            <p>Chúng tôi đã gửi mã xác thực đến email của bạn</p>
        </div>

        <!-- Hiển thị email -->
        <% if (session.getAttribute("registerEmail") != null) { %>
        <div class="email-display">
            📧 <%= session.getAttribute("registerEmail") %>
        </div>
        <% } %>

        <!-- Hiển thị thông báo thành công -->
        <% if (request.getAttribute("success") != null) { %>
        <div class="alert alert-success">
            <%= request.getAttribute("success") %>
        </div>
        <% } %>

        <!-- Hiển thị thông báo thành công -->
        <% if (request.getAttribute("message") != null) { %>
        <div class="alert alert-success">
            <%= request.getAttribute("message") %>
        </div>
        <% } %>

        <!-- Hiển thị lỗi -->
        <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-error">
            <%= request.getAttribute("error") %>
        </div>
        <% } %>

        <!-- Form xác thực OTP -->
        <form action="register" method="post" id="otp-form">
            <input type="hidden" name="action" value="verifyOTP">

            <input type="text"
                   name="otp"
                   id="otp-input"
                   class="otp-input"
                   placeholder="Nhập mã OTP 6 số"
                   maxlength="6"
                   pattern="[0-9]{6}"
                   required
                   autocomplete="off">

            <button type="submit" class="btn-verify" id="btn-verify">
                🔐 Xác thực OTP
            </button>
        </form>

        <!-- Nút gửi lại OTP -->
        <form action="register" method="post" style="margin-top: 15px;">
            <input type="hidden" name="action" value="resendOTP">
            <button type="submit" class="btn-resend">
                📧 Gửi lại mã OTP
            </button>
        </form>

        <a href="register" class="back-link">
            ← Quay lại trang đăng ký
        </a>
    </div>

    <!-- Script tự động chuyển trang -->
    <% if (request.getAttribute("redirectScript") != null) { %>
        <%= request.getAttribute("redirectScript") %>
    <% } %>

    <script>
        let isSubmitting = false; // Flag để ngăn double submission
        let otpInput;
        let verifyButton;
        let otpForm;

        // Khởi tạo elements sau khi DOM load
        document.addEventListener('DOMContentLoaded', function() {
            otpInput = document.getElementById('otp-input');
            verifyButton = document.getElementById('btn-verify');
            otpForm = document.getElementById('otp-form');
console.log('Elements initialized:', {
                otpInput: otpInput !== null,
                verifyButton: verifyButton !== null,
                otpForm: otpForm !== null
            });

            // Tự động focus vào ô nhập OTP
            if (otpInput) {
                otpInput.focus();
            }

            // Chỉ cho phép nhập số
            if (otpInput) {
                otpInput.addEventListener('input', function(e) {
                    this.value = this.value.replace(/[^0-9]/g, '');
                    console.log('Current OTP value:', this.value);

                    // Auto-submit khi nhập đủ 6 số
                    if (this.value.length === 6 && !isSubmitting) {
                        console.log('Auto-submitting with OTP:', this.value);
                        submitForm();
                    }
                });
            }

            // Handle manual submit
            if (otpForm) {
                otpForm.addEventListener('submit', function(e) {
                    if (isSubmitting) {
                        console.log('Preventing duplicate submission');
                        e.preventDefault();
                        return false;
                    }

                    const otpValue = otpInput ? otpInput.value : '';
                    console.log('Manual submit with OTP:', otpValue);

                    if (!otpValue || otpValue.length !== 6) {
                        alert('Vui lòng nhập đủ 6 số OTP!');
                        e.preventDefault();
                        return false;
                    }

                    submitForm();
                });
            }
        });

        function submitForm() {
            if (isSubmitting) return;

            isSubmitting = true;
            console.log('Submitting form...');

            // Disable button nhưng KHÔNG disable input
            if (verifyButton) {
                verifyButton.disabled = true;
                verifyButton.innerHTML = '⏳ Đang xác thực...';
            }

            // Submit form sau một chút delay
            setTimeout(() => {
                if (otpForm) {
                    console.log('Form submitting with OTP value:', otpInput ? otpInput.value : 'null');
                    otpForm.submit();
                }
            }, 100);
        }
    </script>
</body>
</html>
