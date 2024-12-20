<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <title>Изменить данные пользователя</title>
    <link rel="stylesheet" href="css/changeUserData.css" defer>
</head>
<body>
<div class="form-container">
    <h1>Изменить данные пользователя</h1>
    <form action="changeUserData" method="post">
        <div class="form-item">
            <label for="username">Имя пользователя:</label>
            <input type="text" id="username" name="username" required>
        </div>
        <div class="form-item">
            <label for="password">Пароль:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <div class="form-item">
            <button type="submit" class="button">Подтвердить</button>
        </div>
    </form>

    <a href="profile" class="cancel-button">Отмена</a>
</div>
</body>
</html>