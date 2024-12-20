<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Отзывы пользователей</title>
    <link rel="stylesheet" href="css/bookDetail.css" defer>
</head>
<body>

<h1>Отзывы пользователей</h1>

<c:choose>
    <c:when test="${not empty reviews}">
        <table>
            <thead>
                <tr>
                    <th>Имя Автора</th>
                    <th>Название Книги</th>
                    <th>Отзыв</th>
                    <th>Оценка</th>
                    <th>Дата отзыва</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="review" items="${reviews}">
                <tr>
                    <td>${review.username}</td>
                    <td>${review.bookTitle}</td>
                    <td>${review.reviewText}</td>
                    <td>${review.rating}</td>
                    <td>
                        <fmt:formatDate value="${review.reviewDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <p>Нет отзывов для этой книги.</p>
    </c:otherwise>
</c:choose>

<div class="button-container">
    <a href="createReview?bookId=${param.bookId}&userId=${param.userId}">
        <button>Создать отзыв</button>
    </a>
    <a href="main">
        <button>Назад на главный экран</button>
    </a>
</div>

</body>
</html>