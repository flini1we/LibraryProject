<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="css/addBooks.css" defer>
    <script src="js/adminAction.js" defer></script>
</head>
<body>

<div class="menu">
    <a href="adminMain">Список пользователей</a>
    <a href="addBooks">Добавить книги</a>
</div>

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
        <th>Изменить количество</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="book" items="${books}">
        <tr>
            <td><img src="${book.imagePath}" alt="${book.title}" height="200"></td>
            <td>${book.title}</td>
            <td>${book.authorName}</td>
            <td>${book.publicationYear}</td>
            <td>${book.description}</td>
            <td>${book.isbn}</td>
            <td id="availableCopies_${book.id}">${book.availableCopies}</td>
            <td>
                <button onclick="updateCopies(${book.id}, 'increase', 'availableCopies_${book.id}')">+</button>
                <button onclick="updateCopies(${book.id}, 'decrease', 'availableCopies_${book.id}')">-</button>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
