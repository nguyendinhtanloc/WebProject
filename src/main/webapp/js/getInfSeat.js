document.addEventListener('DOMContentLoaded', function() {
   const seatCheckboxes = document.querySelectorAll('.seat-cell input[type="checkbox"]:not(:disabled)');
   const seatNumbersSpan = document.getElementById('seat-numbers');
   const seatCountSpan = document.getElementById('seat-count');
   const totalPriceSpan = document.getElementById('total-price');

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
   }

   seatCheckboxes.forEach(checkbox => {
      checkbox.addEventListener('change', updateSelection);
   });
});