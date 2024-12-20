<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>
<head>
    <title>Профиль пользователя</title>
    <link rel="stylesheet" href="css/profile.css" defer>
    <script src="js/userActions.js" defer></script>
</head>
<body>

<button class="delete-button ${doUserHaveAnyReservedBooks ? 'disabled' : ''}"
        onclick="handleDeleteAccount(${doUserHaveAnyReservedBooks})">Удалить аккаунт</button>

<form id="deleteAccountForm" action="deleteAccount" method="POST" style="display: none;">
    <input type="hidden" name="userId" value="${user.id}">
</form>

<div class="profile-container">
    <img src="${pageContext.request.contextPath}/assets/reader/reader.png" alt="Читатель">

    <h1>Профиль пользователя</h1>

    <div class="profile-item">
        <span>ID:</span> ${user.id}
    </div>
    <div class="profile-item">
        <span>Имя пользователя:</span> ${user.username}
    </div>
    <div class="profile-item">
        <span>Email:</span> ${user.email}
    </div>
    <div class="profile-item">
        <span>Роль:</span>
        <c:choose>
            <c:when test="${user.role == 'READER'}">Читатель</c:when>
            <c:otherwise>Администратор</c:otherwise>
        </c:choose>
    </div>
    <div class="profile-item">
        <span>Дата регистрации:</span> ${formattedDate}
    </div>

    <div class="button-container">
        <a class="button" href="main">Назад</a>
        <a class="button" href="changeUserData">Изменить данные</a>
        <a class="delete-button" href="logout">Выйти</a>
    </div>
</div>

</body>
</html>