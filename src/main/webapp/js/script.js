// Navigation Menu Toggle
document.addEventListener('DOMContentLoaded', function() {
    const hamburger = document.querySelector('.hamburger');
    const navMenu = document.querySelector('.nav-menu');
    
    if (hamburger && navMenu) {
        hamburger.addEventListener('click', function() {
            hamburger.classList.toggle('active');
            navMenu.classList.toggle('active');
        });
    }
    
    // Set minimum date to today
    const dateInput = document.getElementById('date');
    if (dateInput) {
        const today = new Date().toISOString().split('T')[0];
        dateInput.setAttribute('min', today);
        
        // Set default value to today if empty
        if (!dateInput.value) {
            dateInput.value = today;
        }
    }
    
    // Auto-complete functionality
    setupAutoComplete();
});

// Search form toggle for results page
function toggleSearchForm() {
    const container = document.getElementById('searchFormContainer');
    if (container) {
        if (container.style.display === 'none' || container.style.display === '') {
            container.style.display = 'block';
            container.scrollIntoView({ behavior: 'smooth' });
        } else {
            container.style.display = 'none';
        }
    }
}

// Book ticket function (placeholder)
function bookTicket(tripId) {
    alert('Chức năng đặt vé sẽ được triển khai sau. Trip ID: ' + tripId);
    // Ở đây bạn có thể redirect đến trang đặt vé
    // window.location.href = '/booking?tripId=' + tripId;
}

// View trip details function (placeholder)
function viewDetails(tripId) {
    alert('Chức năng xem chi tiết sẽ được triển khai sau. Trip ID: ' + tripId);
    // Ở đây bạn có thể mở modal hoặc redirect đến trang chi tiết
    // window.location.href = '/trip-details?tripId=' + tripId;
}

// Auto-complete setup
function setupAutoComplete() {
    const departureInput = document.getElementById('departure');
    const arrivalInput = document.getElementById('arrival');
    
    if (departureInput) {
        departureInput.addEventListener('input', function() {
            // Có thể thêm logic gọi API để lấy gợi ý địa điểm
            console.log('Searching for departure places:', this.value);
        });
    }
    
    if (arrivalInput) {
        arrivalInput.addEventListener('input', function() {
            // Có thể thêm logic gọi API để lấy gợi ý địa điểm
            console.log('Searching for arrival places:', this.value);
        });
    }
}

// Form validation
function validateSearchForm() {
    const departure = document.getElementById('departure').value.trim();
    const arrival = document.getElementById('arrival').value.trim();
    const date = document.getElementById('date').value;
    
    if (!departure) {
        alert('Vui lòng nhập điểm đi');
        return false;
    }
    
    if (!arrival) {
        alert('Vui lòng nhập điểm đến');
        return false;
    }
    
    if (departure.toLowerCase() === arrival.toLowerCase()) {
        alert('Điểm đi và điểm đến không thể giống nhau');
        return false;
    }
    
    if (!date) {
        alert('Vui lòng chọn ngày đi');
        return false;
    }
    
    const selectedDate = new Date(date);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    if (selectedDate < today) {
        alert('Ngày đi không thể là ngày trong quá khứ');
        return false;
    }
    
    return true;
}

// Add form validation to search forms
document.addEventListener('DOMContentLoaded', function() {
    const searchForms = document.querySelectorAll('.search-form');
    searchForms.forEach(form => {
        form.addEventListener('submit', function(e) {
            if (!validateSearchForm()) {
                e.preventDefault();
            }
        });
    });
});

// Smooth scrolling for anchor links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});

// Header scroll effect
window.addEventListener('scroll', function() {
    const header = document.querySelector('.header');
    if (header) {
        if (window.scrollY > 100) {
            header.classList.add('scrolled');
        } else {
            header.classList.remove('scrolled');
        }
    }
});

// Loading state for search button
function showSearchLoading(button) {
    const originalText = button.innerHTML;
    button.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang tìm...';
    button.disabled = true;
    
    // Reset after form submission (this would normally be handled by page navigation)
    setTimeout(() => {
        button.innerHTML = originalText;
        button.disabled = false;
    }, 2000);
}

// Add loading state to search buttons
document.addEventListener('DOMContentLoaded', function() {
    const searchButtons = document.querySelectorAll('button[type="submit"]');
    searchButtons.forEach(button => {
        button.addEventListener('click', function() {
            if (validateSearchForm()) {
                showSearchLoading(this);
            }
        });
    });
});

// Format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount);
}

// Date formatting
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('vi-VN', {
        weekday: 'long',
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });
}

// Time formatting
function formatTime(timeString) {
    const time = new Date('2000-01-01T' + timeString);
    return time.toLocaleTimeString('vi-VN', {
        hour: '2-digit',
        minute: '2-digit'
    });
}