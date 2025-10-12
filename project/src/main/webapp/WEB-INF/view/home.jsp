<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" %> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>BusBooking Admin</title>
        <link
            rel="stylesheet"
            href="${pageContext.request.contextPath}/styles/reset.css"
        />
        <link
            rel="stylesheet"
            href="${pageContext.request.contextPath}/styles/style.css"
        />
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>

    <body>
        <jsp:include page="/WEB-INF/view/components/header.jsp" />
        <div class="container">
            <jsp:include page="/WEB-INF/view/components/sidebar.jsp" />
            <main class="main">
                <div class="main-content" id="mainContent">
                    <c:choose>
                        <c:when test="${not empty contentPage}">
                            <!-- Include trang con được chỉ định -->
                            <jsp:include page="${contentPage}" />
                        </c:when>
                        <c:otherwise>
                            <!-- Hiển thị dashboard mặc định -->
                            <div class="dashboard">
                                <div class="grid">
                                    <div class="infor-card">
                                        <div class="infor-card__left">
                                            <h2 class="infor-card__title">
                                                Tổng số lượng xe
                                            </h2>
                                            <p class="infor-card__value">
                                                ${totalVehicles}
                                            </p>
                                        </div>
                                        <svg
                                            xmlns="http://www.w3.org/2000/svg"
                                            viewBox="0 0 640 640"
                                            class="infor-card__icon"
                                        >
                                            <path
                                                d="M480 64C568.4 64 640 135.6 640 224L640 448C640 483.3 611.3 512 576 512L570.4 512C557.2 549.3 521.8 576 480 576C438.2 576 402.7 549.3 389.6 512L250.5 512C237.3 549.3 201.8 576 160.1 576C118.4 576 82.9 549.3 69.7 512L64 512C28.7 512 0 483.3 0 448L0 160C0 107 43 64 96 64L480 64zM160 432C133.5 432 112 453.5 112 480C112 506.5 133.5 528 160 528C186.5 528 208 506.5 208 480C208 453.5 186.5 432 160 432zM480 432C453.5 432 432 453.5 432 480C432 506.5 453.5 528 480 528C506.5 528 528 506.5 528 480C528 453.5 506.5 432 480 432zM480 128C462.3 128 448 142.3 448 160L448 352C448 369.7 462.3 384 480 384L544 384C561.7 384 576 369.7 576 352L576 224C576 171 533 128 480 128zM248 288L352 288C369.7 288 384 273.7 384 256L384 160C384 142.3 369.7 128 352 128L248 128L248 288zM96 128C78.3 128 64 142.3 64 160L64 256C64 273.7 78.3 288 96 288L200 288L200 128L96 128z"
                                            />
                                        </svg>
                                    </div>
                                    <div class="infor-card">
                                        <div class="infor-card__left">
                                            <h2 class="infor-card__title">
                                                Tổng số lượng tài xế
                                            </h2>
                                            <p class="infor-card__value">
                                                ${totalDrivers}
                                            </p>
                                        </div>
                                        <svg
                                            xmlns="http://www.w3.org/2000/svg"
                                            viewBox="0 0 640 640"
                                            class="infor-card__icon"
                                        >
                                            <path
                                                d="M376 88C376 57.1 350.9 32 320 32C289.1 32 264 57.1 264 88C264 118.9 289.1 144 320 144C350.9 144 376 118.9 376 88zM400 300.7L446.3 363.1C456.8 377.3 476.9 380.3 491.1 369.7C505.3 359.1 508.3 339.1 497.7 324.9L427.2 229.9C402 196 362.3 176 320 176C277.7 176 238 196 212.8 229.9L142.3 324.9C131.8 339.1 134.7 359.1 148.9 369.7C163.1 380.3 183.1 377.3 193.7 363.1L240 300.7L240 576C240 593.7 254.3 608 272 608C289.7 608 304 593.7 304 576L304 416C304 407.2 311.2 400 320 400C328.8 400 336 407.2 336 416L336 576C336 593.7 350.3 608 368 608C385.7 608 400 593.7 400 576L400 300.7z"
                                            />
                                        </svg>
                                    </div>
                                    <div class="infor-card">
                                        <div class="infor-card__left">
                                            <h2 class="infor-card__title">
                                                Tổng số tài khoản
                                            </h2>
                                            <p class="infor-card__value">
                                                ${totalUsers}
                                            </p>
                                        </div>
                                        <svg
                                            xmlns="http://www.w3.org/2000/svg"
                                            viewBox="0 0 640 640"
                                            class="infor-card__icon"
                                        >
                                            <path
                                                d="M320 80C377.4 80 424 126.6 424 184C424 241.4 377.4 288 320 288C262.6 288 216 241.4 216 184C216 126.6 262.6 80 320 80zM96 152C135.8 152 168 184.2 168 224C168 263.8 135.8 296 96 296C56.2 296 24 263.8 24 224C24 184.2 56.2 152 96 152zM0 480C0 409.3 57.3 352 128 352C140.8 352 153.2 353.9 164.9 357.4C132 394.2 112 442.8 112 496L112 512C112 523.4 114.4 534.2 118.7 544L32 544C14.3 544 0 529.7 0 512L0 480zM521.3 544C525.6 534.2 528 523.4 528 512L528 496C528 442.8 508 394.2 475.1 357.4C486.8 353.9 499.2 352 512 352C582.7 352 640 409.3 640 480L640 512C640 529.7 625.7 544 608 544L521.3 544zM472 224C472 184.2 504.2 152 544 152C583.8 152 616 184.2 616 224C616 263.8 583.8 296 544 296C504.2 296 472 263.8 472 224zM160 496C160 407.6 231.6 336 320 336C408.4 336 480 407.6 480 496L480 512C480 529.7 465.7 544 448 544L192 544C174.3 544 160 529.7 160 512L160 496z"
                                            />
                                        </svg>
                                    </div>
                                    <div class="infor-card">
                                        <div class="infor-card__left">
                                            <h2 class="infor-card__title">
                                                Tổng doanh số tháng
                                            </h2>
                                            <p class="infor-card__value">
                                                <c:choose>
                                                    <c:when
                                                        test="${monthlyRevenue != null && monthlyRevenue != '0'}"
                                                    >
                                                        ${monthlyRevenue} VNĐ
                                                    </c:when>
                                                    <c:otherwise>
                                                        0 VNĐ
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                        </div>
                                        <svg
                                            xmlns="http://www.w3.org/2000/svg"
                                            viewBox="0 0 640 640"
                                            class="infor-card__icon"
                                        >
                                            <path
                                                d="M296 88C296 74.7 306.7 64 320 64C333.3 64 344 74.7 344 88L344 128L400 128C417.7 128 432 142.3 432 160C432 177.7 417.7 192 400 192L285.1 192C260.2 192 240 212.2 240 237.1C240 259.6 256.5 278.6 278.7 281.8L370.3 294.9C424.1 302.6 464 348.6 464 402.9C464 463.2 415.1 512 354.9 512L344 512L344 552C344 565.3 333.3 576 320 576C306.7 576 296 565.3 296 552L296 512L224 512C206.3 512 192 497.7 192 480C192 462.3 206.3 448 224 448L354.9 448C379.8 448 400 427.8 400 402.9C400 380.4 383.5 361.4 361.3 358.2L269.7 345.1C215.9 337.5 176 291.4 176 237.1C176 176.9 224.9 128 285.1 128L296 128L296 88z"
                                            />
                                        </svg>
                                    </div>

                                    <div class="big-chart">
                                        <h3 class="chart-title">
                                            Doanh thu và số chuyến 7 ngày gần
                                            nhất
                                        </h3>
                                        <canvas id="revenueChart"></canvas>
                                    </div>
                                    <div class="premium-chart">
                                        <h3 class="chart-title">
                                            Thống kê tuyến đường phổ biến
                                        </h3>
                                        <canvas id="routeChart"></canvas>
                                    </div>
                                </div>
                            </div>

                            <!-- Hidden inputs to pass data to JavaScript -->
                            <script type="application/json" id="chartDataInput">
                                <c:out value="${chartData}" escapeXml="false" />
                            </script>
                            <script type="application/json" id="routeDataInput">
                                <c:out value="${routeData}" escapeXml="false" />
                            </script>
                        </c:otherwise>
                    </c:choose>
                </div>
            </main>
        </div>

        <script>
            // Khởi tạo tất cả biểu đồ khi DOM sẵn sàng
            document.addEventListener("DOMContentLoaded", function () {
                // Thêm timeout nhỏ để đảm bảo Chart.js đã load xong
                setTimeout(function () {
                    console.log("DOM loaded - bắt đầu khởi tạo biểu đồ");

                    // Kiểm tra Chart.js có được load không
                    if (typeof Chart === "undefined") {
                        console.error("Chart.js chưa được load!");
                        return;
                    }
                    console.log("Chart.js đã được load thành công");

                    // Kiểm tra canvas elements
                    const revenueCanvas =
                        document.getElementById("revenueChart");
                    const routeCanvas = document.getElementById("routeChart");

                    if (!revenueCanvas) {
                        console.error("Không tìm thấy canvas revenueChart!");
                        return;
                    }

                    if (!routeCanvas) {
                        console.error("Không tìm thấy canvas routeChart!");
                        return;
                    }

                    console.log("Tất cả canvas elements đã sẵn sàng");

                    // Biểu đồ doanh thu và số chuyến
                    try {
                        // Lấy dữ liệu từ script tag
                        const chartDataInput =
                            document.getElementById("chartDataInput");
                        const chartDataString = chartDataInput
                            ? chartDataInput.textContent ||
                              chartDataInput.innerText
                            : "";
                        console.log("Chart data raw:", chartDataString);
                        console.log(
                            "Chart data length:",
                            chartDataString.length
                        );
                        console.log("Chart data type:", typeof chartDataString);

                        if (
                            chartDataString &&
                            chartDataString.trim() !== "" &&
                            chartDataString !== "null" &&
                            chartDataString !== "undefined"
                        ) {
                            let chartData;
                            try {
                                chartData = JSON.parse(chartDataString);
                            } catch (parseError) {
                                console.error("JSON parse error:", parseError);
                                console.log("Trying to fix JSON string...");
                                // Thử fix JSON string nếu bị escape
                                const fixedString = chartDataString
                                    .replace(/\\"/g, '"')
                                    .replace(/\\n/g, "")
                                    .replace(/\\r/g, "");
                                chartData = JSON.parse(fixedString);
                            }
                            console.log("Chart data parsed:", chartData);

                            console.log("Checking chartData structure:", {
                                hasChartData: !!chartData,
                                hasDates: !!(chartData && chartData.dates),
                                datesLength:
                                    chartData && chartData.dates
                                        ? chartData.dates.length
                                        : 0,
                                dates: chartData && chartData.dates,
                                revenue: chartData && chartData.revenue,
                                count: chartData && chartData.count,
                            });

                            if (
                                chartData &&
                                chartData.dates &&
                                chartData.dates.length > 0
                            ) {
                                console.log("Using REAL data from server!");
                                const ctx = document
                                    .getElementById("revenueChart")
                                    .getContext("2d");

                                new Chart(ctx, {
                                    type: "bar",
                                    data: {
                                        labels: chartData.dates,
                                        datasets: [
                                            {
                                                type: "bar",
                                                label: "Số chuyến",
                                                data: chartData.count,
                                                backgroundColor:
                                                    "rgba(39, 115, 142, 0.7)",
                                                borderColor:
                                                    "rgba(39, 115, 142, 1)",
                                                borderWidth: 1,
                                                yAxisID: "y",
                                            },
                                            {
                                                type: "line",
                                                label: "Doanh thu (VNĐ)",
                                                data: chartData.revenue,
                                                borderColor:
                                                    "rgba(192, 84, 133, 1)",
                                                backgroundColor:
                                                    "rgba(192, 84, 133, 0.1)",
                                                borderWidth: 3,
                                                fill: false,
                                                tension: 0.4,
                                                pointBackgroundColor:
                                                    "rgba(192, 84, 133, 1)",
                                                pointBorderColor: "#fff",
                                                pointBorderWidth: 2,
                                                pointRadius: 6,
                                                yAxisID: "y1",
                                            },
                                        ],
                                    },
                                    options: {
                                        responsive: true,
                                        maintainAspectRatio: false,
                                        plugins: {
                                            title: {
                                                display: false,
                                            },
                                            legend: {
                                                display: true,
                                                position: "top",
                                            },
                                        },
                                        scales: {
                                            x: {
                                                display: true,
                                                title: {
                                                    display: true,
                                                    text: "Ngày",
                                                },
                                            },
                                            y: {
                                                type: "linear",
                                                display: true,
                                                position: "left",
                                                title: {
                                                    display: true,
                                                    text: "Số chuyến",
                                                },
                                                ticks: {
                                                    beginAtZero: true,
                                                    stepSize: 1,
                                                },
                                            },
                                            y1: {
                                                type: "linear",
                                                display: true,
                                                position: "right",
                                                title: {
                                                    display: true,
                                                    text: "Doanh thu (VNĐ)",
                                                },
                                                ticks: {
                                                    beginAtZero: true,
                                                    callback: function (value) {
                                                        return value.toLocaleString(
                                                            "vi-VN"
                                                        );
                                                    },
                                                },
                                                grid: {
                                                    drawOnChartArea: false,
                                                },
                                            },
                                        },
                                        interaction: {
                                            mode: "index",
                                            intersect: false,
                                        },
                                    },
                                });
                                console.log(
                                    "Biểu đồ doanh thu đã được tạo thành công"
                                );
                            } else {
                                console.log(
                                    "Không có dữ liệu biểu đồ doanh thu"
                                );
                            }
                        }
                    } catch (error) {
                        console.error("Lỗi khi tạo biểu đồ doanh thu:", error);
                    }

                    // Biểu đồ tròn thống kê tuyến đường
                    try {
                        // Lấy dữ liệu từ script tag
                        const routeDataInput =
                            document.getElementById("routeDataInput");
                        const routeDataString = routeDataInput
                            ? routeDataInput.textContent ||
                              routeDataInput.innerText
                            : "";
                        console.log("Route data raw:", routeDataString);
                        console.log(
                            "Route data length:",
                            routeDataString.length
                        );
                        console.log("Route data type:", typeof routeDataString);

                        if (
                            routeDataString &&
                            routeDataString.trim() !== "" &&
                            routeDataString !== "null" &&
                            routeDataString !== "undefined"
                        ) {
                            let routeData;
                            try {
                                routeData = JSON.parse(routeDataString);
                            } catch (parseError) {
                                console.error(
                                    "Route JSON parse error:",
                                    parseError
                                );
                                console.log(
                                    "Trying to fix route JSON string..."
                                );
                                // Thử fix JSON string nếu bị escape
                                const fixedString = routeDataString
                                    .replace(/\\"/g, '"')
                                    .replace(/\\n/g, "")
                                    .replace(/\\r/g, "");
                                routeData = JSON.parse(fixedString);
                            }
                            console.log("Route data parsed:", routeData);

                            console.log("Checking routeData structure:", {
                                hasRouteData: !!routeData,
                                hasLabels: !!(routeData && routeData.labels),
                                hasData: !!(routeData && routeData.data),
                                labelsLength:
                                    routeData && routeData.labels
                                        ? routeData.labels.length
                                        : 0,
                                labels: routeData && routeData.labels,
                                data: routeData && routeData.data,
                            });

                            if (
                                routeData &&
                                routeData.labels &&
                                routeData.data
                            ) {
                                console.log(
                                    "Using REAL route data from server!"
                                );
                                const routeCtx = document
                                    .getElementById("routeChart")
                                    .getContext("2d");

                                // Màu sắc cho biểu đồ tròn
                                const colors = [
                                    "#FF6384", // Hồng
                                    "#36A2EB", // Xanh dương
                                    "#FFCE56", // Vàng
                                    "#4BC0C0", // Xanh lá
                                    "#9966FF", // Tím (cho "Khác")
                                ];

                                new Chart(routeCtx, {
                                    type: "pie",
                                    data: {
                                        labels: routeData.labels,
                                        datasets: [
                                            {
                                                data: routeData.data,
                                                backgroundColor: colors.slice(
                                                    0,
                                                    routeData.labels.length
                                                ),
                                                borderColor: colors
                                                    .slice(
                                                        0,
                                                        routeData.labels.length
                                                    )
                                                    .map(
                                                        (color) => color + "CC"
                                                    ),
                                                borderWidth: 2,
                                                hoverOffset: 4,
                                            },
                                        ],
                                    },
                                    options: {
                                        responsive: true,
                                        maintainAspectRatio: false,
                                        plugins: {
                                            title: {
                                                display: false,
                                            },
                                            legend: {
                                                display: true,
                                                position: "bottom",
                                                labels: {
                                                    padding: 20,
                                                    usePointStyle: true,
                                                    font: {
                                                        size: 12,
                                                    },
                                                },
                                            },
                                            tooltip: {
                                                callbacks: {
                                                    label: function (context) {
                                                        const label =
                                                            context.label || "";
                                                        const value =
                                                            context.parsed;
                                                        const total =
                                                            context.dataset.data.reduce(
                                                                (a, b) => a + b,
                                                                0
                                                            );
                                                        const percentage = (
                                                            (value / total) *
                                                            100
                                                        ).toFixed(1);
                                                        return `${label}: ${value} chuyến (${percentage}%)`;
                                                    },
                                                },
                                            },
                                        },
                                    },
                                });
                                console.log(
                                    "Biểu đồ tròn đã được tạo thành công"
                                );
                            } else {
                                console.log("Dữ liệu tuyến đường không hợp lệ");
                            }
                        }
                    } catch (error) {
                        console.error("Lỗi khi tạo biểu đồ tròn:", error);
                    }
                }, 100); // Timeout 100ms
            });

            let isInternalNavigation = false;

            // Bắt tất cả link → đánh dấu là điều hướng nội bộ
            document.querySelectorAll("a").forEach((a) => {
                a.addEventListener("click", function () {
                    isInternalNavigation = true;
                });
            });

            // ======================== PHẦN ĐƯỢC THÊM VÀO ========================
            // Bắt tất cả sự kiện gửi form → đánh dấu là điều hướng nội bộ
            document.querySelectorAll("form").forEach((form) => {
                form.addEventListener("submit", function () {
                    isInternalNavigation = true;
                });
            });
            // ====================== KẾT THÚC PHẦN THÊM VÀO ======================

            // Trước khi rời trang
            window.addEventListener("beforeunload", function (e) {
                // Chỉ hiển thị cảnh báo nếu không phải là điều hướng nội bộ
                if (!isInternalNavigation) {
                    var confirmationMessage =
                        "Bạn có chắc chắn muốn thoát? Nếu thoát, bạn sẽ bị đăng xuất.";
                    (e || window.event).returnValue = confirmationMessage;
                    return confirmationMessage;
                }
            });

            // Khi unload (thật sự rời trang / đóng tab)
            window.addEventListener("unload", function () {
                // Chỉ logout nếu không phải là điều hướng nội bộ
                if (!isInternalNavigation) {
                    const formData = new FormData();
                    formData.append(
                        "<%= com.busbooking.filter.CsrfTokenFilter.CSRF_TOKEN_SESSION_ATTR %>",
                        "${sessionScope.csrfToken}"
                    );
                    navigator.sendBeacon(
                        "${pageContext.request.contextPath}/logout",
                        formData
                    );
                }
            });
        </script>
    </body>
</html>
