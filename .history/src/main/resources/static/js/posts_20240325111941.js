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

                // Creating header
                const postHeader = document.createElement('div');
                postHeader.className = 'post-header';

                // Creating user avatar
                const userAvatar = document.createElement('img');
                userAvatar.src = 'assets/post/sample_avatar.png';
                userAvatar.alt = 'Avatar użytkownika';
                userAvatar.className = 'post-avatar';

                const sportType = document.createElement('h2');
                sportType.className = 'post-sport';
                sportType.textContent = element.sportType;

                // Adding elements to the header
                postHeader.appendChild(userAvatar);
                postHeader.appendChild(sportType);

                // Adding everything to the post
                postElement.appendChild(postHeader);


                postsContainer.appendChild(postElement);
            });
        })
        .catch(error => console.error('Error', error));
})