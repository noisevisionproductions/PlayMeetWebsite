/*!
* Script for displaying posts that users created. Posts will be downloaded from database.
*/
document.addEventListener('DOMContentLoaded', function () {
    fetch('/api/posts', {
        headers: {
            'Accept': 'application/json'
        }
    })
        .then(response => response.json())
        .then(posts => {
            const postsContainer = document.getElementById('posts-container');
            posts.forEach(element => {
                const postElement = document.createElement('div');
                postElement.className = 'post';
                postElement.innerHTML =
                    `<h2>${element.sportType}
                    </h2><p>${element.cityName}</p>
                    </h2 > <p>${element.userId}</p>`
                    ;
                postsContainer.appendChild(postElement);
            });
        })
        .catch(error => console.error('Error', error));
})