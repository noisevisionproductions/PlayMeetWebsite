const checkUserSession = async () => {
    try {
        const response = await fetch('/auth/user/session');
        if (!response.ok){
            throw new Error('Failed to fetch user session');
        }

        const sessionData = await response.json();
        const accountLink = document.getElementById('accountLink');

        if (sessionData.status == 'logged_in' && sessionData.user) {
            accountLink.innerText = "Twoje konto";
            accountLink.href = `/user_account/${sessionData.user}`;
        } else if (sessionData.status === 'logged_out') {
            accountLink.innerText = "Zaloguj się";
            accountLink.href = "/login";
        }
    } catch (error) {
        console.error('Error checking user session: ', error);
    }
};

window.addEventListener('DOMContentLoaded', event => {
    checkUserSession();
});