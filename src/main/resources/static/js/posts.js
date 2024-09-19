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