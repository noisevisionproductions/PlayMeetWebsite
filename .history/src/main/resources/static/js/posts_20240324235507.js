/*!
* Script for displaying posts that users created. Posts will be downloaded from database.
*/
document.addEventListener('DOMContentLoaded', function () {
    fetch('/post_template.html')
        .then(response => response.text())
        .then(html => {
            var template = document.createElement('template');
            template.innerHTML = html;
            return template.content;
        }).then(templateContent => {
            fetch('/api/posts', {
                headers: {
                    'Accept': 'application/json'
                }
            })
                .then(response => response.json())
                .then(posts => {
                    const postsContainer = document.getElementById('posts-container');
                    posts.forEach(element => {
                        const postClone = document.importNode(templateContent, true);

                        postClone.querySelector('.post-avatar').src = element.avatarUrl;
                        postClone.querySelector('.post-title').textContent = element.title;

                        postsContainer.appendChild(postClone);
                    });
                })
                .catch(error => console.error('Error', error));
        })
        .catch(error => {
            console.error('Could not load the template: ', error);
        })
});
