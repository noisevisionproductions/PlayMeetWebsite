/*!
* Start Bootstrap - Grayscale v7.0.6 (https://startbootstrap.com/theme/grayscale)
* Copyright 2013-2023 Start Bootstrap
* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-grayscale/blob/master/LICENSE)
*/
//
// Scripts
// 
// Dodaje nasłuchiwacz zdarzeń, który wykonuje funkcję po załadowaniu całego drzewa DOM strony


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

    // Polityka cookies
    var consent = localStorage.getItem('userConsent');
    if (!consent) {
        document.getElementById('cookieConsentContainer').style.display = 'block';
    }

    document.getElementById('acceptCookieConsent').onclick = function () {
        localStorage.setItem('userConsent', 'accepted');
        document.getElementById('cookieConsentContainer').style.display = 'none';
    };
});

const checkUserSession = async () => {
    try {
        const response = await fetch('http://localhost:8080/user/session');
        if (!response.ok){
            throw new Error('Failed to fetch user session');
        }

        const sessionData = await response.json();
        const accountLink = document.getElementById('accountLink');

        if (sessionData.status == 'logged_in') {
            accountLink.innerText = "Twoje konto";
            accountLink.href = "/account";
        } else {
            accountLink.innerText = "Zaloguj się";
            accountLink.href = "/login"
        }
    } catch (error) {
        console.error('Error checking user session: ', error);
    }
};

window.addEventListener('DOMContentLoaded', event => {
    checkUserSession();
});