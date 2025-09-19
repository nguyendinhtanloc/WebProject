<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <link rel="stylesheet" href="../css/reset.css" />
        <link rel="stylesheet" href="../css/style.css" />
        <title>admin</title>
    </head>
    <body>
        <jsp:include page="/components/header.jsp" />
        <div class="container">
            <jsp:include page="/components/sidebar.jsp" />
            <main class="main">
                <div class="main-content" id="mainContent">
                    <div class="dashboard">
                        <div class="grid">
                            <div class="infor-card">
                                <h2 class="title"></h2>
                                <p class="value"></p>
                            </div>
                            <div class="infor-card">
                                <h2 class="title"></h2>
                                <p class="value"></p>
                            </div>
                            <div class="infor-card">
                                <h2 class="title"></h2>
                                <p class="value"></p>
                            </div>
                            <div class="infor-card">
                                <h2 class="title"></h2>
                                <p class="value"></p>
                            </div>

                            <div class="big-chart"></div>
                            <div class="premium-chart"></div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </body>
</html>
