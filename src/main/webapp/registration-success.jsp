<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký thành công - BusBooking</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .success-container {
            max-width: 600px;
            margin: 100px auto;
            padding: 40px;
            background: white;
            border-radius: 15px;
            box-shadow: 0 0 30px rgba(0,0,0,0.1);
            text-align: center;
        }

        .success-icon {
            font-size: 80px;
            color: #4CAF50;
            margin-bottom: 20px;
        }

        .success-title {
            color: #4CAF50;
            font-size: 32px;
            font-weight: bold;
            margin-bottom: 15px;
        }

        .success-message {
            color: #666;
            font-size: 18px;
            margin-bottom: 30px;
            line-height: 1.5;
        }

        .countdown {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 30px;
            font-size: 16px;
            color: #495057;
        }

        .btn-login {
            display: inline-block;
            padding: 15px 40px;
            background: #2196F3;
            color: white;
            text-decoration: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: bold;
            transition: background 0.3s;
        }

        .btn-login:hover {
            background: #1976D2;
            color: white;
            text-decoration: none;
        }

        .animation {
            animation: fadeInUp 0.8s ease-out;
        }

        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .checkmark {
            display: inline-block;
            width: 80px;
            height: 80px;
            border-radius: 50%;
            background: #4CAF50;
            position: relative;
            margin-bottom: 20px;
            animation: checkmarkPop 0.6s ease-in-out;
        }

        .checkmark::after {
            content: '';
            position: absolute;
            left: 25px;
            top: 35px;
            width: 15px;
            height: 25px;
            border: solid white;
            border-width: 0 3px 3px 0;
            transform: rotate(45deg);
        }

        @keyframes checkmarkPop {
            0% { transform: scale(0); }
            50% { transform: scale(1.2); }
            100% { transform: scale(1); }
        }
    </style>
</head>
<body>
<div class="success-container animation">
        <div class="checkmark"></div>

        <h1 class="success-title">🎉 Đăng ký thành công!</h1>

        <p class="success-message">
            Tài khoản của bạn đã được tạo thành công.<br>
            Bạn có thể đăng nhập ngay bây giờ với email và mật khẩu vừa đăng ký.
        </p>

        <div class="countdown">
            <span id="countdown-text">Tự động chuyển đến trang đăng nhập sau <strong id="countdown">3</strong> giây...</span>
        </div>

        <a href="login.jsp" class="btn-login">
            🔐 Đăng nhập ngay
        </a>
    </div>

    <script>
        // Countdown timer
        let countdown = 3;
        const countdownElement = document.getElementById('countdown');

        const timer = setInterval(() => {
            countdown--;
            countdownElement.textContent = countdown;

            if (countdown <= 0) {
                clearInterval(timer);
                window.location.href = 'login.jsp';
            }
        }, 1000);

        // Optional: Add confetti effect
        setTimeout(() => {
            // Simple confetti effect
            for (let i = 0; i < 50; i++) {
                createConfetti();
            }
        }, 500);

        function createConfetti() {
            const confetti = document.createElement('div');
            confetti.style.position = 'fixed';
            confetti.style.width = '10px';
            confetti.style.height = '10px';
            confetti.style.backgroundColor = ['#ff6b6b', '#4ecdc4', '#45b7d1', '#96ceb4', '#ffeaa7'][Math.floor(Math.random() * 5)];
            confetti.style.left = Math.random() * 100 + 'vw';
            confetti.style.top = '-10px';
            confetti.style.zIndex = '1000';
            confetti.style.pointerEvents = 'none';

            document.body.appendChild(confetti);

            const animation = confetti.animate([
                { transform: 'translateY(-10px) rotateZ(0deg)', opacity: 1 },
                { transform: 'translateY(100vh) rotateZ(360deg)', opacity: 0 }
            ], {
                duration: 3000,
                easing: 'cubic-bezier(0.5, 0, 0.5, 1)'
            });

            animation.onfinish = () => confetti.remove();
        }
    </script>
</body>
</html>
