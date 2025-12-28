/*!
* Start Bootstrap - Grayscale v7.0.6 (https://startbootstrap.com/theme/grayscale)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-grayscale/blob/master/LICENSE)
*/
//
// Scripts
//

window.onload = function() {
    // Check for the postId query parameter and show the popup
    const urlParams = new URLSearchParams(window.location.search);
    const postId = urlParams.get('postId');

    if (postId) {
        showPopup("Post utworzony! Jego ID to: " + postId);
    }

    // Zoom functionality for images
    var images = document.querySelectorAll('.img-fluid');
    images.forEach(function(img) {
        img.addEventListener('click', function() {
            this.classList.toggle('zoomed');
        });
    });
};

window.addEventListener('DOMContentLoaded', event => {
    // Funkcja zmniejszająca pasek nawigacyjny
    var navbarShrink = function () {
        const navbarCollapsible = document.body.querySelector('#mainNav');
        if (!navbarCollapsible) {
            return;
        }
        if (window.scrollY === 0) {
            navbarCollapsible.classList.remove('navbar-shrink');
        } else {
            navbarCollapsible.classList.add('navbar-shrink');
        }
    };

    // Wywołuje funkcję zmniejszania paska nawigacyjnego i dodaje nasłuchiwacz na przewijanie
    navbarShrink();
    document.addEventListener('scroll', navbarShrink);

    // Aktywacja Bootstrapowego "scrollspy"
    const mainNav = document.body.querySelector('#mainNav');
    if (mainNav) {
        new bootstrap.ScrollSpy(document.body, {
            target: '#mainNav',
            rootMargin: '0px 0px -40%',
        });
    }

    // Zwiniecie responsywnego paska nawigacyjnego
    const navbarToggler = document.body.querySelector('.navbar-toggler');
    const responsiveNavItems = [].slice.call(document.querySelectorAll('#navbarResponsive .nav-link'));
    responsiveNavItems.forEach(responsiveNavItem => {
        responsiveNavItem.addEventListener('click', () => {
            if (window.getComputedStyle(navbarToggler).display !== 'none') {
                navbarToggler.click();
            }
        });
    });
});

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

fetch('/cookies/cookies-status')
    .then(response => response.json())
    .then(data => {
    if (data.cookiePolicyAccepted){
        document.getElementById('cookieConsentContainer').style.display = 'none';
    }
})
    .catch(error => {
    console.error('Error fetching cookie policy status:',  error);
});

document.getElementById("acceptCookieConsent").addEventListener("click", function(){
    fetch('cookies/accept-cookies', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(response => {
        if (response.ok) {
            document.getElementById('cookieConsentContainer').style.display = 'none';
        } else {
            console.error('Error accepting cookie policy:', response.status);
        }
    })
        .catch(error => {
        console.error('Error:', error);
    });
});

window.addEventListener('DOMContentLoaded', event => {
    checkUserSession();
});