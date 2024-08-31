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
                        postClone.querySelector('.post-sportType').textContent = element.sportType;
                        postClone.querySelector('.post-dateTime').textContent = element.dateTime;
                        postClone.querySelector('.post-hourTime').textContent = element.hourTime;
                        postClone.querySelector('.post-skillLevel').textContent = element.skillLevel;
                        postClone.querySelector('.post-peopleStatus').textContent = element.peopleStatus;
                        postClone.querySelector('.post-additionalInfo').textContent = element.additionalInfo || 'Brak dodatkowych informacji';
                        postsContainer.appendChild(postClone);
                    });
                })
                .catch(error => console.error('Error', error));
        })
        .catch(error => {
            console.error('Could not load the template: ', error);
        })
});
