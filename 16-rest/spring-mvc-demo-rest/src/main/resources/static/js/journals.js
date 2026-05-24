document.addEventListener('DOMContentLoaded', function() {
    fetch('/api/journals')
        .then(response => response.json())
        .then(journals => {
            const tbody = document.getElementById('journals-body');
            journals.forEach(journal => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${journal.id}</td>
                    <td>${journal.name}</td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Error fetching journals:', error);
            const tbody = document.getElementById('journals-body');
            const row = document.createElement('tr');
            row.innerHTML = '<td colspan="2">Error loading journals</td>';
            tbody.appendChild(row);
        });
});
