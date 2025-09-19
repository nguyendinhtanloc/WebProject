function loadComponent(id, path) {
    return fetch(path)
        .then((res) => res.text())
        .then((html) => {
            document.getElementById(id).innerHTML = html;
        })
        .catch((err) => console.error(`Lỗi khi tải ${path}:`, err));
}

function loadHeaderFooter() {
    loadComponent("header", "components/header.html");
    loadComponent("footer", "components/footer.html");
}
