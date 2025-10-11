document.addEventListener('DOMContentLoaded', function() {

    // --- Phần code xử lý chọn ghế ---
    const seatCheckboxes = document.querySelectorAll('.seat-cell input[type="checkbox"]:not(:disabled)');
    const seatNumbersSpan = document.getElementById('seat-numbers');
    const seatCountSpan = document.getElementById('seat-count');
    const totalPriceSpan = document.getElementById('total-price');
    const finalTotalPriceSpan = document.getElementById('final-total-price');

    // SỬA LỖI 1: Khai báo các biến tham chiếu đến input ẩn
    const hiddenSelectedSeats = document.getElementById('hidden_selected_seats');
    const hiddenTotalPrice = document.getElementById('hidden-total-price-value');

    function updateSelection() {
        // Lấy ra các element checkbox đang được chọn
        const selectedCheckboxes = Array.from(seatCheckboxes)
                                       .filter(checkbox => checkbox.checked);

        // SỬA LỖI 2: Tạo ra 2 danh sách riêng biệt
        // 1. Danh sách SỐ GHẾ (A1, B5) để hiển thị cho người dùng
        const selectedSeatNumbers = selectedCheckboxes.map(checkbox => checkbox.dataset.seatNumber);

        // 2. Danh sách ID GHẾ (101, 102) để gửi lên server
        const selectedSeatIds = selectedCheckboxes.map(checkbox => checkbox.value);

        // --- Cập nhật giao diện ---
        seatNumbersSpan.textContent = selectedSeatNumbers.length > 0 ? selectedSeatNumbers.join(', ') : 'Chưa chọn';
        seatCountSpan.textContent = selectedCheckboxes.length; // SỬA LỖI 3: Dùng độ dài của mảng đúng

        const totalPrice = selectedCheckboxes.length * ticketPrice; // SỬA LỖI 3: Dùng độ dài của mảng đúng
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

    // --- Phần code để lấy thông tin đón/trả (giữ nguyên) ---
    const pickupOptionRadios = document.querySelectorAll('input[name="pickup_option"]');
    const pickupLocationSelect = document.getElementById('pickup-location');
    const dropoffOptionRadios = document.querySelectorAll('input[name="dropoff_option"]');
    const dropoffLocationSelect = document.getElementById('dropoff-location');

    const hiddenPickupOption = document.getElementById('hidden_pickup_option');
    const hiddenPickupLocation = document.getElementById('hidden_pickup_location');
    const hiddenDropoffOption = document.getElementById('hidden_dropoff_option');
    const hiddenDropoffLocation = document.getElementById('hidden_dropoff_location');

    function updateHiddenFields() {
        document.querySelectorAll('input[name="pickup_option"]').forEach(radio => {
            if (radio.checked) {
                hiddenPickupOption.value = radio.value;
            }
        });
        hiddenPickupLocation.value = pickupLocationSelect.value;

        document.querySelectorAll('input[name="dropoff_option"]').forEach(radio => {
            if (radio.checked) {
                hiddenDropoffOption.value = radio.value;
            }
        });
        hiddenDropoffLocation.value = dropoffLocationSelect.value;
    }

    pickupOptionRadios.forEach(radio => radio.addEventListener('change', updateHiddenFields));
    pickupLocationSelect.addEventListener('change', updateHiddenFields);
    dropoffOptionRadios.forEach(radio => radio.addEventListener('change', updateHiddenFields));
    dropoffLocationSelect.addEventListener('change', updateHiddenFields);

    // Chạy cả hai hàm cập nhật một lần lúc tải trang để lấy giá trị mặc định
    updateSelection();
    updateHiddenFields();
});