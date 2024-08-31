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
                const userAvatar = document.createElement('img');
                postElement.className = 'post';
                postElement.innerHTML =
                    `
                <div class="post-header">
                    <h2 class="post-title">${userAvatar}</h2>
                    <span class="post-date">${element.dateTime}</span>
                    <span class="post-like"><i class="fas fa-heart"></i></span>
                </div>
                <div class="post-content">
                    <p>${element.skillLevel}</p>
                </div>
                <div class="post-footer">
                    <span class="post-author">${element.peopleStatus}</span>
                    <span class="post-comments"><i class="fas fa-comment"></i> ${element.cityName} komentarze</span>
                </div>
            `;
                postsContainer.appendChild(postElement);
            });
        })
        .catch(error => console.error('Error', error));
})