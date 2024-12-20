<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Создать отзыв</title>
    <link rel="stylesheet" href="css/createReview.css" defer>
</head>
<body>
<form action="createReview" method="post">
    <h1>Опишите ваши впечатления</h1>

    <input type="hidden" name="bookId" value="${param.bookId}">
    <input type="hidden" name="userId" value="${param.userId}">

    <label for="rating">Оценка (1-5):</label>
    <input type="number" id="rating" name="rating" min="1" max="5" required>

    <label for="reviewText">Ваш отзыв:</label>
    <textarea id="reviewText" name="reviewText" rows="5" cols="30" required></textarea>

    <button type="submit">Отправить отзыв</button>
    <a class="delete-button" href="bookDetail?bookId=${param.bookId}">Отмена</a>
</form>
</body>
</html>