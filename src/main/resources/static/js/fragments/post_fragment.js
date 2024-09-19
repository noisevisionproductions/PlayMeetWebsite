document.addEventListener("DOMContentLoaded", function() {
    function checkUserSession() {
        fetch('/auth/user/session', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
            document.querySelectorAll('.post-more-info').forEach(details => {
                if (data.status === 'logged_out') {
                    details.addEventListener('click', function(event) {
                        event.preventDefault();
                    });
                    details.querySelector('.post-more-info-summary').textContent = 'Więcej informacji (tylko dla zalogowanych)';
                } else if (data.status === 'logged_in') {
                    details.querySelector('.post-more-info-summary').textContent = 'Więcej informacji';
                }
            });
        })
            .catch(error => console.error('Error checking session:', error));
    }
    checkUserSession();
})
function registerForPost(button) {
    const form = button.closest(".registerForm");
    const postId = form.querySelector("input[name='postId']").value;
    const messageDiv = form.querySelector(".registerMessage");

    fetch('/api/posts/register-for-post', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ postId: postId })
    })
        .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
        .then(data => {
        if (data.success) {
            messageDiv.style.color = "green";
        } else {
            messageDiv.style.color = "red";
        }
        messageDiv.textContent = data.message;
    })
        .catch(error => {
        messageDiv.style.color = "red";
        messageDiv.textContent = "Błąd. Spróbuj ponownie.";
        console.error('Error:', error);
    });
}
function unregisterFromPost(button) {
    const form = button.closest(".registerForm");
    const postId = form.querySelector("input[name='postId']").value;
    const messageDiv = form.querySelector(".registerMessage");

    fetch('/api/posts/unregister-from-post', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ postId: postId })
    })
        .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
        .then(data => {
        if (data.success) {
            messageDiv.style.color = "green";
            messageDiv.textContent = data.message;
            location.reload();
        } else {
            messageDiv.style.color = "red";
            messageDiv.textContent = data.message;
        }
    })
        .catch(error => {
        messageDiv.style.color = "red";
        messageDiv.textContent = "Błąd. Spróbuj ponownie.";
        console.error('Error', error);
    });
}
