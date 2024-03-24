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
                    `
                <div class="post-header">
                    <h2 class="post-title">${post.title}</h2>
                    <span class="post-date">${post.date}</span>
                    <span class="post-like"><i class="fas fa-heart"></i></span>
                </div>
                <div class="post-content">
                    <p>${post.content}</p>
                </div>
                <div class="post-footer">
                    <span class="post-author">${post.author}</span>
                    <span class="post-comments"><i class="fas fa-comment"></i> ${post.commentsCount} komentarze</span>
                </div>
            `;
                postsContainer.appendChild(postElement);
            });
        })
        .catch(error => console.error('Error', error));
})