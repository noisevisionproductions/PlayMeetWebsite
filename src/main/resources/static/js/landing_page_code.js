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
        // Szuka elementu paska nawigacyjnego po ID
        const navbarCollapsible = document.body.querySelector('#mainNav');
        // Jeśli element nie istnieje, funkcja kończy działanie
        if (!navbarCollapsible) {
            return;
        }
        // Jeśli przewinięcie strony jest na samej górze, usuwa klasę zmniejszającą navbar,
        // w przeciwnym razie dodaje tę klasę
        if (window.scrollY === 0) {
            navbarCollapsible.classList.remove('navbar-shrink')
        } else {
            navbarCollapsible.classList.add('navbar-shrink')
        }
    };

    // Wywołuje funkcję zmniejszania paska nawigacyjnego
    navbarShrink();

    // Dodaje nasłuchiwacz zdarzeń na przewijanie strony, który wywołuje funkcję zmniejszającą navbar
    document.addEventListener('scroll', navbarShrink);

    // Aktywacja Bootstrapowego "scrollspy" na głównym elemencie nawigacyjnym
    // Pozwala to na zmianę aktywnego linku w pasku nawigacyjnym w zależności od przewinięcia strony
    const mainNav = document.body.querySelector('#mainNav');
    if (mainNav) {
        new bootstrap.ScrollSpy(document.body, {
            target: '#mainNav',
            rootMargin: '0px 0px -40%',
        });
    };

    // Zwiniecie responsywnego paska nawigacyjnego, kiedy przełącznik jest widoczny
    const navbarToggler = document.body.querySelector('.navbar-toggler');
    const responsiveNavItems = [].slice.call(
        document.querySelectorAll('#navbarResponsive .nav-link')
    );
    // Dla każdego elementu nawigacyjnego dodaje nasłuchiwacz zdarzeń kliknięcia,
    // który zwija pasek nawigacyjny, gdy przełącznik jest widoczny
    responsiveNavItems.map(function (responsiveNavItem) {
        responsiveNavItem.addEventListener('click', () => {
            if (window.getComputedStyle(navbarToggler).display !== 'none') {
                navbarToggler.click();
            }
        });
    });

});
