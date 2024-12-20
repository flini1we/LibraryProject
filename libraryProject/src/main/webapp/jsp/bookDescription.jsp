<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Описание книги</title>
    <link rel="stylesheet" href="css/bookDescription.css" defer>
</head>
<body>

<div class="container">
    <h1>Описание книги</h1>
    <div class="book-container">
        <div class="book-cover">
            <img src="${bookDTO.imagePath != null ? bookDTO.imagePath : ''}"
                 alt="${bookDTO.title}">
        </div>

        <div class="book-details">
            <h2>${bookDTO.title}</h2>
            <p><strong>Автор:</strong> ${bookDTO.authorName != null ? bookDTO.authorName : 'Неизвестен'}</p>
            <p><strong>Год издания:</strong> ${bookDTO.publicationYear}</p>
            <p><strong>ISBN:</strong> ${bookDTO.isbn != null ? bookDTO.isbn : 'Не указан'}</p>
            <p><strong>Доступные копии:</strong> ${bookDTO.availableCopies}</p>
            <p><strong>Описание:</strong></p>
            <p>${bookDTO.description != null ? bookDTO.description : 'Описание отсутствует.'}</p>
        </div>
    </div>

    <a href="main" class="back-link">Назад к списку книг</a>
</div>

</body>
</html>