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

                const userAvatar = document.createElement('img');
                userAvatar.src = element.avatar || 'assets/post/sample_avatar.png'; // Fallback to a default avatar if none provided
                userAvatar.alt = 'Avatar';
                userAvatar.className = 'post-avatar';

                const postTitle = document.createElement('h2');
                postTitle.className = 'post-sportType';
                postTitle.textContent = element.sportType; // Assuming `title` is part of your JSON

                const postDate = document.createElement('span');
                postDate.className = 'post-date';
                postDate.textContent = element.dateTime; // Format as needed

                postHeader.appendChild(userAvatar);
                postHeader.appendChild(postTitle);
                postHeader.appendChild(postDate);

                // Creating post content
                const postContent = document.createElement('div');
                postContent.className = 'post-content';
                postContent.textContent = element.cityName; // Assuming `content` is part of your JSON

                // Creating footer
                const postFooter = document.createElement('div');
                postFooter.className = 'post-footer';

                const postTags = document.createElement('div');
                postTags.className = 'post-skillLevel';
                postTags.textContent = element.signedUpCount + "/" + element.howManyPeopleNeeded;

                const postCommentsCount = document.createElement('div');
                postCommentsCount.className = 'post-comments-count';
                postTags.textContent = element.signedUpCount + "/" + element.howManyPeopleNeeded;

                postFooter.appendChild(postTags);
                postFooter.appendChild(postCommentsCount);

                // Adding everything to the post
                postElement.appendChild(postHeader);
                postElement.appendChild(postContent);
                postElement.appendChild(postFooter);

                postsContainer.appendChild(postElement);
            });
        })
        .catch(error => console.error('Error', error));
})