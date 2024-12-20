function toggleMenu() {
    const sidebar = document.getElementById('sidebar');
    sidebar.classList.toggle('open');
}

function handleReserve(button, bookId, action, availableCopiesElementId) {
    button.disabled = true;
    fetch(action, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        },
        body: new URLSearchParams({
            'bookId': bookId
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                button.innerText = data.newButtonText;
                button.style.backgroundColor = data.newButtonColor;
                const availableCopiesElement = document.getElementById(availableCopiesElementId);
                if (availableCopiesElement) {
                    availableCopiesElement.innerText = data.availableCopies;
                }
                location.reload()
            } else {
                alert('Произошла ошибка: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Ошибка запроса:', error);
            alert('Произошла ошибка при обработке запроса.');
        }).finally(() => {
            button.disabled = false;
        });
}