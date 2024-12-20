<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
  <title>User's Reserved Books</title>
  <link rel="stylesheet" href="css/userBooked.css">
</head>
<body>
<a href="main" class="back-button">Назад</a>
<h1>Ваши зарезервированные книги</h1>
<c:choose>
  <c:when test="${not empty reservedBooks}">
    <table>
      <thead>
      <tr>
        <th>Обложка</th>
        <th>Название</th>
        <th>Автор</th>
        <th>Год издания</th>
        <th>Описание</th>
      <tr>
      </thead>
      <tbody>
      <c:forEach var="book" items="${reservedBooks}">
        <tr>
          <td><img src="${book.imagePath}" alt="${book.title}" height="200"></td>
          <td>${book.title}</td>
          <td>${book.authorName}</td>
          <td>${book.publicationYear}</td>
          <td>${book.description}</td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:when>
  <c:otherwise>
    <p>У вас нет ни одной зарезервированной книги</p>
  </c:otherwise>
</c:choose>
</body>
</html>
