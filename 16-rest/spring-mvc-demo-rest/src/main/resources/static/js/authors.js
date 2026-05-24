document.addEventListener('DOMContentLoaded', function() {
    fetch('/api/authors')
        .then(response => response.json())
        .then(authors => {
            const tbody = document.getElementById('authors-body');
            authors.forEach(author => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${author.id}</td>
                    <td>${author.name}</td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Error fetching authors:', error);
            const tbody = document.getElementById('authors-body');
            const row = document.createElement('tr');
            row.innerHTML = '<td colspan="2">Error loading authors</td>';
            tbody.appendChild(row);
        });
});
