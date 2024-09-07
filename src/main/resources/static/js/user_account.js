function performLogout() {
    fetch('/auth/perform_logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
        if (response.ok) {
            localStorage.clear();
            sessionStorage.clear();
            window.location.href = '/';
        }
    })
        .catch(error => {
        console.error('Error logging out:', error);
    });
}
