function deleteUser(userId) {
    if (!confirm("Вы уверены, что хотите удалить пользователя?")) {
        return;
    }

    fetch('deleteAccount', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'userId=' + encodeURIComponent(userId)
    })
        .then(response => {
            if (response.ok) {
                alert('Пользователь успешно удалён!');
                const userRow = document.getElementById('user-row-' + userId);
                if (userRow) {
                    userRow.remove();
                } else {
                    location.reload();
                }
            } else {
                alert('Неизвестная ошибка. HTTP статус: ' + response.status);
            }
        })
        .catch(error => {
            console.error('Ошибка при выполнении запроса:', error);
            alert('Ошибка при удалении пользователя.');
        });
}

function changeRole(userId, currentRole) {
    if (!confirm("Вы уверены, что хотите изменить роль пользователя?")) {
        return;
    }

    const newRole = currentRole === 'READER' ? 'ADMIN' : 'READER';
    fetch('promoteOrDemote', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `userId=${encodeURIComponent(userId)}`
    })
        .then(response => {
            if (response.ok) {
                alert('Роль пользователя успешно изменена!');
                const roleElement = document.getElementById(`user-role-${userId}`);
                if (roleElement) {
                    roleElement.textContent = newRole;
                }
            } else if (response.status === 400) {
                alert('Ошибка: Некорректный запрос.');
            } else if (response.status === 500) {
                alert('Ошибка сервера при изменении роли.');
            } else {
                alert('Неизвестная ошибка. HTTP статус: ' + response.status);
            }
            // location.reload();
        })
        .catch(error => {
            console.error('Ошибка при выполнении запроса:', error);
            alert('Ошибка при изменении роли пользователя.');
        });
}

function updateCopies(bookId, action, availableCopiesElementId) {
    fetch('addBooks', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            'bookId': bookId,
            'action': action
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const availableCopiesElement = document.getElementById(availableCopiesElementId);
                if (availableCopiesElement) {
                    availableCopiesElement.innerText = data.availableCopies;
                }
            } else {
                alert(`Ошибка при обновлении копий книги: ${data.error}`);
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
            alert(`Произошла ошибка при выполнении запроса: ${error.message}`);
        });
}