/**
 * BusBooking - Search Results JavaScript
 * Interactive functionality for search results page
 */

// Toggle trip details
function toggleTripDetails(tripId) {
    console.log('Toggle details for trip:', tripId);

    // Create modal or expand card functionality
    const message = `Chi tiết chuyến xe ID: ${tripId}\n(Tính năng đang phát triển)`;

    // You can replace this with a modal implementation
    alert(message);

    // TODO: Implement actual trip details modal
    // showTripDetailsModal(tripId);
}

// Scroll to search form and focus
function showSearchForm() {
    const searchSection = document.querySelector('.search-section');

    if (searchSection) {
        // Smooth scroll to search section
        searchSection.scrollIntoView({
            behavior: 'smooth',
            block: 'start'
        });

        // Focus on departure city input after scroll completes
        setTimeout(() => {
            const departureCityInput = document.querySelector('#departureCity');
            if (departureCityInput) {
                departureCityInput.focus();
                departureCityInput.select();
            }
        }, 500);
    }
}

// Sort trips functionality
function sortTrips(sortBy) {
    console.log('Sort trips by:', sortBy);

    const sortMessages = {
        'time': 'Đang sắp xếp theo giờ khởi hành...',
        'price': 'Đang sắp xếp theo giá vé...',
        'company': 'Đang sắp xếp theo nhà xe...'
    };

    if (sortMessages[sortBy]) {
        showNotification(sortMessages[sortBy]);

        // TODO: Implement actual sorting logic
        // You can either:
        // 1. Sort client-side using JavaScript
        // 2. Reload page with sort parameter

        // Example: window.location.href = window.location.pathname + '?sort=' + sortBy;
    }
}

// Change view type (list/grid)
function setView(viewType) {
    const buttons = document.querySelectorAll('.view-options .btn');
    const tripList = document.querySelector('.trip-list');

    if (!tripList) return;

    // Update active button
    buttons.forEach(btn => btn.classList.remove('active'));
    event.target.closest('.btn').classList.add('active');

    // Apply view style
    if (viewType === 'grid') {
        tripList.style.display = 'grid';
        tripList.style.gridTemplateColumns = 'repeat(auto-fit, minmax(450px, 1fr))';
        tripList.style.gap = '1.5rem';
    } else {
        tripList.style.display = 'flex';
        tripList.style.flexDirection = 'column';
        tripList.style.gap = '1.5rem';
    }

    // Save preference to localStorage (optional)
    try {
        localStorage.setItem('tripViewPreference', viewType);
    } catch (e) {
        console.log('Could not save view preference');
    }
}

// Show notification toast
function showNotification(message, duration = 2000) {
    // Create notification element
    const notification = document.createElement('div');
    notification.textContent = message;
    notification.style.cssText = `
        position: fixed;
        top: 100px;
        right: 30px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        padding: 16px 24px;
        border-radius: 12px;
        z-index: 1000;
        font-weight: 600;
        box-shadow: 0 8px 24px rgba(102, 126, 234, 0.4);
        animation: slideInRight 0.3s ease;
        max-width: 300px;
    `;

    document.body.appendChild(notification);

    // Remove notification after duration
    setTimeout(() => {
        notification.style.animation = 'slideOutRight 0.3s ease';
        setTimeout(() => {
            if (notification.parentNode) {
                document.body.removeChild(notification);
            }
        }, 300);
    }, duration);
}

// Initialize page
function initSearchResults() {
    // Add animation styles if not already present
    if (!document.getElementById('custom-animations')) {
        const style = document.createElement('style');
        style.id = 'custom-animations';
        style.textContent = `
            @keyframes slideInRight {
                from {
                    opacity: 0;
                    transform: translateX(100px);
                }
                to {
                    opacity: 1;
                    transform: translateX(0);
                }
            }
            @keyframes slideOutRight {
                from {
                    opacity: 1;
                    transform: translateX(0);
                }
                to {
                    opacity: 0;
                    transform: translateX(100px);
                }
            }
        `;
        document.head.appendChild(style);
    }

    // Restore view preference
    try {
        const savedView = localStorage.getItem('tripViewPreference');
        if (savedView === 'grid') {
            const gridButton = document.querySelector('.view-options .btn:last-child');
            if (gridButton) {
                gridButton.click();
            }
        }
    } catch (e) {
        console.log('Could not restore view preference');
    }

    // Add smooth scroll behavior to all internal links
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

    // Set minimum date for date inputs to today
    const dateInputs = document.querySelectorAll('input[type="date"]');
    const today = new Date().toISOString().split('T')[0];
    dateInputs.forEach(input => {
        if (!input.value) {
            input.min = today;
        }
    });

    console.log('Search Results page initialized');
}

// Run initialization when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initSearchResults);
} else {
    initSearchResults();
}

// Export functions for potential module usage
if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        toggleTripDetails,
        showSearchForm,
        sortTrips,
        setView,
        showNotification
    };
}