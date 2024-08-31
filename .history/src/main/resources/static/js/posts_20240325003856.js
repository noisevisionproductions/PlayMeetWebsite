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
                const postClone = document.importNode(templateContent, true);
                // postClone
                // postsContainer.appendChild(postClone);

                // Creating user avatar
                const userAvatar = document.createElement('img');
                userAvatar.src = 'assets/post/sample_avatar.png';
                userAvatar.alt = 'Avatar użytkownika';
                userAvatar.className = 'post-avatar';

                // Creating header
                const postHeader = document.createElement('div');
                postHeader.className = 'post-header';

                const postTitle = document.createElement('h2');
                postTitle..querySelector('.post-skillLevel').textContent = element.skillLevel;
                postTitle.textContent = 'Tytuł postu';

                // Adding elements to the header
                postHeader.appendChild(userAvatar);
                postHeader.appendChild(postTitle);

                // Adding everything to the post
                postElement.appendChild(postHeader);

                //postElement.className = 'post';
                //     postElement.innerHTML =
                //         `
                //     <div class="post-header">
                //         <h2 class="post-title">${userAvatar}</h2>
                //         <span class="post-date">${element.dateTime}</span>
                //         <span class="post-like"><i class="fas fa-heart"></i></span>
                //     </div>
                //     <div class="post-content">
                //         <p>${element.skillLevel}</p>
                //     </div>
                //     <div class="post-footer">
                //         <span class="post-author">${element.peopleStatus}</span>
                //         <span class="post-comments"><i class="fas fa-comment"></i> ${element.cityName} komentarze</span>
                //     </div>
                // `;
                postsContainer.appendChild(postElement);
            });
        })
        .catch(error => console.error('Error', error));
})