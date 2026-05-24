document.addEventListener('DOMContentLoaded', function() {
    fetch('/api/articles')
        .then(response => response.json())
        .then(articles => {
            const tbody = document.getElementById('articles-body');
            articles.forEach(article => {
                // Формируем строку с именами авторов
                const authorsNames = article.authors.map(author => author.name).join(', ');
                
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${article.id}</td>
                    <td><a href="/articles/${article.id}">${article.title}</a></td>
                    <td>${authorsNames}</td>
                    <td>${article.journal ? article.journal.name : ''}</td>
                    <td>
                        <a href="/articles/edit/${article.id}">Edit</a> |
                        <button onclick="deleteArticle(${article.id})" class="link-button">Delete</button>
                    </td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Error fetching articles:', error);
            const tbody = document.getElementById('articles-body');
            const row = document.createElement('tr');
            row.innerHTML = '<td colspan="5">Error loading articles</td>';
            tbody.appendChild(row);
        });

    // Функция для удаления статьи
    window.deleteArticle = function(id) {
        if (confirm('Are you sure you want to delete this article?')) {
            fetch(`/api/articles/${id}`, { method: 'DELETE' })
                .then(() => location.reload())
                .catch(error => console.error('Error deleting article:', error));
        }
    };
});
