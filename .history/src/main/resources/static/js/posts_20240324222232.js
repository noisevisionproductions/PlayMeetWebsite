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
                const postClone = document.importNode(template, true);

                postClone.querySelector('.post-avatar').src = element.avatarUrl;
                postClone.querySelector('post-title').textContext = element.title;

                postsContainer.appendChild(postClone);
            });
        })
        .catch(error => console.error('Error', error));
})