function deleteAvatar(element) {
    const userId = element.getAttribute('data-userid');
    if (confirm("Czy na pewno chcesz usunąć avatar?")) {
        fetch(`/user_account/${userId}/delete-avatar`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
            .then(response => {
            if (response.ok) {
                alert("Avatar został usunięty.");
                window.location.reload();
            } else {
                alert("Wystąpił błąd podczas usuwania avatara.");
            }
        })
            .catch(error => {
            console.error('Error:', error);
            alert("Wystąpił błąd podczas usuwania avatara.");
        });
    }
}
