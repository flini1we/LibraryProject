<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Библиотека</title>
    <script src="js/mainAction.js" defer></script>
    <link rel="stylesheet" href="css/main.css">
</head>
<body>
<div class="container">
    <div class="sidebar" id="sidebar">
        <button class="close-btn" onclick="toggleMenu()">×</button>
        <ul>
            <li><a href="profile">Профиль</a></li>
            <li><a href="userBooked">Ваша бронь</a></li>
            <li><a href="login">Выйти</a></li>
        </ul>
    </div>

    <div class="main-content">
        <button class="menu-btn" onclick="toggleMenu()">☰ Меню</button>

        <div class="search-bar">
            <form id="searchForm" action="searchBooks" method="get">
                <input type="text" name="query" placeholder="Поиск по названию или автору">
                <button type="submit">Поиск</button>
            </form>
        </div>

        <div class="book-list">
            <h1>Добро пожаловать в библиотеку!</h1>
            <table>
                <thead>
                <tr>
                    <th>Обложка</th>
                    <th>Название</th>
                    <th>Автор</th>
                    <th>Год издания</th>
                    <th>Описание</th>
                    <th>ISBN</th>
                    <th>Копий доступно</th>
                    <th>Посмотреть отзывы</th>
                    <th>Действия с книгами</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="book" items="${books}">
                    <tr>
                        <td><img src="${book.imagePath}" alt="${book.title}" height="200"></td>
                        <td><a href="bookDescription?bookId=${book.id}">${book.title}</a></td>
                        <td>${book.authorName}</td>
                        <td>${book.publicationYear}</td>
                        <td>${book.description}</td>
                        <td>${book.isbn}</td>
                        <td id="availableCopies_${book.id}">${book.availableCopies}</td>
                        <td>
                            <button onclick="location.href='bookDetail?bookId=${book.id}'" class="btn">Отзывы</button>
                        </td>
                        <td>
                            <button onclick="handleReserve(this, '${book.id}', '${book.reservedByUser ? 'returnBook' : 'reserveBook'}', 'availableCopies_${book.id}')"
                                    class="reserve-btn"
                                    style="background-color:
                                    <c:choose>
                                    <c:when test="${book.availableCopiesPresent}">
                                        ${book.reservedByUser ? 'red' : 'green'}
                                    </c:when>
                                    <c:otherwise>
                                        ${book.reservedByUser ? 'red' : 'gray'}
                                    </c:otherwise>
                                    </c:choose>;"
                                    <c:if test="${!book.availableCopiesPresent && !book.reservedByUser}">disabled</c:if>>
                                <c:choose>
                                    <c:when test="${book.availableCopiesPresent}">
                                        ${book.reservedByUser ? 'Сдать' : 'Забронировать'}
                                    </c:when>
                                    <c:otherwise>
                                        ${book.reservedByUser ? 'Сдать' : 'Нет в наличии'}
                                    </c:otherwise>
                                </c:choose>
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>