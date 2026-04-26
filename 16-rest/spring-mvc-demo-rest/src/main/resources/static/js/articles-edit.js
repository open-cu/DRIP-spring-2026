document.addEventListener('DOMContentLoaded', function() {
    // Загружаем список авторов и журналов
    Promise.all([
        fetch('/api/authors').then(res => res.json()),
        fetch('/api/journals').then(res => res.json())
    ]).then(([authors, journals]) => {
        const authorsSelect = document.getElementById('authors');
        const journalsSelect = document.getElementById('journal');

        // Заполняем список авторов
        authors.forEach(author => {
            const option = document.createElement('option');
            option.value = author.id;
            option.text = author.name;
            authorsSelect.appendChild(option);
        });

        // Заполняем список журналов
        journals.forEach(journal => {
            const option = document.createElement('option');
            option.value = journal.id;
            option.text = journal.name;
            journalsSelect.appendChild(option);
        });

        // Если редактируем существующую статью, загружаем её данные
        if (action === 'edit' && articleId) {
            fetch(`/api/articles/${articleId}`)
                .then(res => res.json())
                .then(article => {
                    document.getElementById('id').value = article.id;
                    document.getElementById('title').value = article.title;

                    // Выбираем авторов
                    const authorIds = article.authors.map(a => a.id);
                    Array.from(authorsSelect.options).forEach(option => {
                        option.selected = authorIds.includes(parseInt(option.value));
                    });

                    // Выбираем журнал
                    if (article.journal) {
                        journalsSelect.value = article.journal.id;
                    }
                });
        }
    });

    // Обработка отправки формы
    document.getElementById('article-form').addEventListener('submit', function(e) {
        e.preventDefault();

        const articleId = document.getElementById('id').value;
        const title = document.getElementById('title').value;
        const authors = Array.from(document.getElementById('authors').selectedOptions)
            .map(option => parseInt(option.value));
        const journal = parseInt(document.getElementById('journal').value);

        const articleData = {
            id: articleId ? parseInt(articleId) : null,
            title: title,
            authors: authors,
            journal: journal
        };

        const url = articleId ? `/api/articles/${articleId}` : '/api/articles';
        const method = articleId ? 'PUT' : 'POST';

        fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(articleData)
        })
        .then(res => {
            if (res.ok) {
                window.location.href = '/articles';
            } else {
                alert('Error saving article');
            }
        })
        .catch(error => console.error('Error:', error));
    });
});
