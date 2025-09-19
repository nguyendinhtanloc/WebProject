<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c"
uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <link rel="stylesheet" href="../styles/reset.css" />
        <link rel="stylesheet" href="../styles/style.css" />
        <title>admin</title>
    </head>
    <body>
        <jsp:include page="/components/header.jsp" />
        <div class="container">
            <jsp:include page="/components/sidebar.jsp" />
            <main class="main">
                <div class="buses">
                    <h1 class="buses-title title">Xe</h1>
                    <div class="buses-top top">
                        <label
                            for="addform-checkbox"
                            class="buses-top__addbutton addbutton"
                        >
                            ThÃªm
                        </label>
                        <input
                            type="checkbox"
                            name="addform"
                            id="addform-checkbox"
                            class="addform-checkbox"
                        />
                        <div class="addform">
                            <div class="main-content">
                                <h2 class="addform__title">ThÃªm xe</h2>
                                <form
                                    class="addform__form"
                                    action="category_insert"
                                    method="POST"
                                >
                                    <!-- Äáº£m báº£o method="POST" -->
                                    <label>company_id (UUID format)</label>
                                    <input
                                        type="text"
                                        name="company_id"
                                        required
                                        pattern="[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
                                        placeholder="vÃ­ dá»¥: 123e4567-e89b-12d3-a456-426614174000"
                                    />
                                    <br />

                                    <label>name</label>
                                    <input type="text" name="name" required />
                                    <br />

                                    <label>phone</label>
                                    <input type="text" name="phone" required />
                                    <br />

                                    <label>license_no</label>
                                    <input
                                        type="text"
                                        name="license_no"
                                        required
                                    />
                                    <br />

                                    <label>experience_years</label>
                                    <input
                                        type="number"
                                        name="experience_years"
                                        required
                                    />
                                    <br />

                                    <label>status</label>
                                    <input type="text" name="status" required />
                                    <br />

                                    <input type="submit" value="submit" />
                                </form>
                            </div>
                        </div>
                        <!-- overlay -->
                        <label
                            for="addform-checkbox"
                            class="addform-overlay overlay"
                        ></label>
                    </div>

                    <table class="buses-table table">
                        <thead class="buses-table__head table-head">
                            <tr class="buses-table__row table-row">
                                <th>ID</th>
                                <th>ID CÃ´ng ty</th>
                                <th>TÃªn</th>
                                <th>Sá» Äiá»n thoáº¡i</th>
                                <th>CÄn cÆ°á»c</th>
                                <th>Kinh nghiá»m(NÄm)</th>
                                <th>Tráº¡ng thÃ¡i</th>
                                <th>HÃ nh Äá»ng</th>
                            </tr>
                        </thead>
                        <tbody class="buses-table__body table-body">
                            <tr class="buses-table__row table-row">
                                <td>1</td>
                                <td>1</td>
                                <td>Nguyá»n VÄn A</td>
                                <td>0123456789</td>
                                <td>123456789</td>
                                <td>5</td>
                                <td>Hoáº¡t Äá»ng</td>
                                <td>
                                    <button
                                        class="buses-table__editbutton editbutton"
                                    >
                                        Sửa
                                    </button>
                                    <button
                                        class="buses-table__deletebutton deletebutton"
                                    >
                                        Xóa
                                    </button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    </body>
</html>
