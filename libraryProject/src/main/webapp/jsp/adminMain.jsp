<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Админ панель</title>
    <script src="js/adminAction.js" defer></script>
    <link rel="stylesheet" href="css/adminMain.css" defer>
</head>
<body>

<div class="menu">
    <a href="adminMain">Список пользователей</a>
    <a href="addBooks">Добавить книги</a>
</div>

<h1>Список пользователей</h1>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Имя</th>
        <th>Дата регистрации</th>
        <th>Роль</th>
        <th>Действия</th>
    </tr>
    </thead>
    <tbody>
    <c:choose>
        <c:when test="${not empty users}">
            <c:forEach var="user" items="${users}">
                <tr id="user-row-${user.id}">
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.getRegistrationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))}</td>
                    <td id="user-role-${user.id}">${user.role}</td>
                    <td>
                        <button class="delete-btn" onclick="deleteUser(${user.id})">Удалить</button>
                        <button class="admin-btn" onclick="changeRole(${user.id}, '${user.role}')">
                            <c:choose>
                                <c:when test="${user.role == 'ADMIN'}">
                                    Сделать читателем
                                </c:when>
                                <c:otherwise>
                                    Сделать админом
                                </c:otherwise>
                            </c:choose>
                        </button>
                    </td>
                </tr>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="5">Не найдено ни одной актуальной пользовательской сессии.</td>
            </tr>
        </c:otherwise>
    </c:choose>
    </tbody>
</table>

<div style="margin-top: 20px; text-align: center">
    <form action="logout" method="post">
        <button type="submit">Выйти</button>
    </form>
</div>

</body>
</html>