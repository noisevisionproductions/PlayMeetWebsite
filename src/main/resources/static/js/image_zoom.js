window.onload = function() {
    var images = document.querySelectorAll('.img-fluid');
    images.forEach(function(img) {
        img.addEventListener('click', function() {
            this.classList.toggle('zoomed');
        });
    });
};
