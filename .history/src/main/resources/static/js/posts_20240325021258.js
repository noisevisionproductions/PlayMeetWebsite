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

                // Creating user avatar
                const userAvatar = document.createElement('img');
                userAvatar.src = 'assets/post/sample_avatar.png';
                userAvatar.alt = 'Avatar użytkownika';
                userAvatar.className = 'post-avatar';

                // Creating header
                const postHeader = document.createElement('div');
                postHeader.className = 'post-header';

                const postTitle = document.createElement('h2');
                postTitle.className = 'postTitle';
                postTitle.textContent = element.sportName;

                // Adding elements to the header
                postHeader.appendChild(userAvatar);
                postHeader.appendChild(postTitle);

                // Adding everything to the post
                postElement.appendChild(postHeader);


                postsContainer.appendChild(postElement);
            });
        })
        .catch(error => console.error('Error', error));
})