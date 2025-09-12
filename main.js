const sidebarLinks = document.querySelectorAll(".sidebar-item a");
const mainContent = document.getElementById("mainContent");

// Hàm load trang bằng fetch
async function loadPage(page) {
  try {
    const res = await fetch(`pages/${page}.html`);
    const html = await res.text();
    mainContent.innerHTML = html;
  } catch (err) {
    mainContent.innerHTML = "<p style='color:red'>Không tải được nội dung!</p>";
  }
}

// Gán sự kiện click
sidebarLinks.forEach((link) => {
  link.addEventListener("click", (e) => {
    e.preventDefault();
    const page = link.closest(".sidebar-item").dataset.page;
    loadPage(page);
  });
});

// Mặc định load Dashboard khi mở web
loadPage("dashboard");
