/*!
* Script for displaying posts that users created. Posts will be downloaded from database.
*/
document.addEventListener('DOMContentLoaded', function () {
    fetch('/api/posts')
        .then(response => response.json())
        .then(posts => {
            const postsContainer = document.getElementById('posts-container');
            posts.forEach(element => {
                const postElement = document.createElement('div');
                postElement.className = 'post';
                postElement.innerHTML = `<h2>${element.title}</h2><p>${element.content}</p>`;
                postsContainer.appendChild(postElement);
            });
        })
        .catch(error => console.error('Error', error));
})
