document.addEventListener('DOMContentLoaded', function() {

    // --- Phần code xử lý chọn ghế ---
    const seatCheckboxes = document.querySelectorAll('.seat-cell input[type="checkbox"]:not(:disabled)');
    const seatNumbersSpan = document.getElementById('seat-numbers');
    const seatCountSpan = document.getElementById('seat-count');
    const totalPriceSpan = document.getElementById('total-price');
    const finalTotalPriceSpan = document.getElementById('final-total-price');

    const hiddenSelectedSeats = document.getElementById('hidden_selected_seats');
    const hiddenTotalPrice = document.getElementById('hidden-total-price-value');

    function updateSelection() {
        // Lấy ra các element checkbox đang được chọn
        const selectedCheckboxes = Array.from(seatCheckboxes)
                                       .filter(checkbox => checkbox.checked);

        // 1. Danh sách SỐ GHẾ (A1, B5) để hiển thị cho người dùng
        const selectedSeatNumbers = selectedCheckboxes.map(checkbox => checkbox.dataset.seatNumber);

        // 2. Danh sách ID GHẾ (101, 102) để gửi lên server
        const selectedSeatIds = selectedCheckboxes.map(checkbox => checkbox.value);

        // --- Cập nhật giao diện ---
        seatNumbersSpan.textContent = selectedSeatNumbers.length > 0 ? selectedSeatNumbers.join(', ') : 'Chưa chọn';
        seatCountSpan.textContent = selectedCheckboxes.length;

        const totalPrice = selectedCheckboxes.length * ticketPrice;
        const formattedPrice = totalPrice.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });

        totalPriceSpan.textContent = formattedPrice;
        finalTotalPriceSpan.textContent = formattedPrice;

        // --- Cập nhật giá trị cho các input ẩn để gửi đi ---
        hiddenSelectedSeats.value = selectedSeatIds.join(','); // Gửi chuỗi các ID
        hiddenTotalPrice.value = totalPrice; // Gửi giá trị số
    }

    seatCheckboxes.forEach(checkbox => {
       checkbox.addEventListener('change', updateSelection);
    });

    // Chạy hàm cập nhật một lần lúc tải trang để khởi tạo giá trị
    updateSelection();
});