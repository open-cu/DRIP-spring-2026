document.addEventListener('DOMContentLoaded', function() {
    if (!articleId) {
        console.error('Error fetching article:', error);
        document.getElementById('article-details').innerHTML = '<p>Error loading article</p>';
        return;
    }

    fetch(`/api/articles/${articleId}`)
        .then(res => res.json())
        .then(article => {
            document.getElementById('article-id').textContent = article.id;
            document.getElementById('article-title-display').textContent = article.title;
            document.getElementById('article-title').textContent = article.title;

            // Заполняем список авторов
            const authorsList = document.getElementById('article-authors');
            authorsList.innerHTML = '';
            article.authors.forEach(author => {
                const li = document.createElement('li');
                li.textContent = author.name;
                authorsList.appendChild(li);
            });

            // Заполняем журнал
            if (article.journal) {
                document.getElementById('article-journal').textContent = article.journal.name;
            } else {
                document.getElementById('article-journal').textContent = 'N/A';
            }

            // Устанавливаем ссылку для редактирования
            const editLink = document.getElementById('edit-link');
            if (editLink) {
                editLink.href = `/articles/edit/${article.id}`;
            }
        })
        .catch(error => {
            console.error('Error fetching article:', error);
            document.getElementById('article-details').innerHTML = '<p>Error loading article</p>';
        });
});