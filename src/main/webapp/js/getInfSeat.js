document.addEventListener('DOMContentLoaded', function() {

    // --- Phần code xử lý chọn ghế ---
    const seatCheckboxes = document.querySelectorAll('.seat-cell input[type="checkbox"]:not(:disabled)');
    const seatNumbersSpan = document.getElementById('seat-numbers');
    const seatCountSpan = document.getElementById('seat-count');
    const totalPriceSpan = document.getElementById('total-price');
    // Dòng mới: Lấy tham chiếu đến input ẩn cho ghế đã chọn
    const hiddenSelectedSeats = document.getElementById('hidden_selected_seats');

    function updateSelection() {
        const selectedSeats = Array.from(seatCheckboxes)
                                       .filter(checkbox => checkbox.checked);

        const selectedSeatIds = selectedSeats.map(checkbox => checkbox.value);

        if (selectedSeatIds.length > 0) {
           seatNumbersSpan.textContent = selectedSeatIds.join(', ');
        } else {
           seatNumbersSpan.textContent = 'Chưa chọn';
        }

        seatCountSpan.textContent = selectedSeatIds.length;

        const totalPrice = selectedSeatIds.length * ticketPrice;
        totalPriceSpan.textContent = totalPrice.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
        document.getElementById('final-total-price').textContent = totalPrice.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });

        // Dòng mới: Cập nhật giá trị cho input ẩn bằng chuỗi các ghế đã chọn (ví dụ: "A1,B5,C2")
        hiddenSelectedSeats.value = selectedSeatIds.join(',');
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