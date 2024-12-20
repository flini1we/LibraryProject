function handleDeleteAccount(doUserHaveAnyReservedBooks) {
    if (doUserHaveAnyReservedBooks) {
        alert("Нельзя удалить аккаунт, имея забронированные книги.");
    } else {
        const form = document.getElementById("deleteAccountForm");
        const userConfirmed = confirm("Вы уверены, что хотите удалить аккаунт?");
        if (userConfirmed) {
            form.submit();
        }
    }
}