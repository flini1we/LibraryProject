<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Вход в систему</title>
  <link rel="stylesheet" href="css/login.css" defer>
</head>
<body>
<div class="login-container">
  <h2>Вход в систему</h2>

  <c:if test="${not empty errorMessage}">
    <h3 style="color: red;">${errorMessage}</h3>
  </c:if>

  <form action="login" method="post">
    <div class="form-group">
      <label for="username">Имя пользователя:</label>
      <input type="text" id="username" name="username" required>
    </div>
    <div class="form-group">
      <label for="password">Пароль:</label>
      <input type="password" id="password" name="password" required>
    </div>
    <div class="form-group">
      <input type="submit" value="Войти">
    </div>
  </form>
  <p>Нет аккаунта? <a href="register">Зарегистрироваться</a></p>

</div>
</body>
</html>