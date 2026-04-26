let stompClient = null;

function connect() {
    // SockJS - библиотека-полифил для подключения к WebSocket
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        
        // Subscribe to comments for this article
        if (typeof articleId !== 'undefined') {
            stompClient.subscribe(`/topic/comments/${articleId}`, function(evt) {
                displayComments(JSON.parse(evt.body));
            });
            
            // Load existing comments
            loadComments();
        }
    });
}

function disconnect() {
    if(stompClient !== null) {
        stompClient.disconnect();
    }
}

function loadComments() {
    stompClient.send(`/app/comments/${articleId}/load`, {}, {});
}

function displayComments(comments) {
    console.log(comments)
    const tbody = document.getElementById('comments-body');
    tbody.innerHTML = '';
    comments.forEach(comment => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${comment.id}</td>
            <td>${comment.content}</td>
            <td><button onclick="deleteComment(${comment.id})" class="link-button">Delete</button></td>
        `;
        tbody.appendChild(row);
    });
}

function addComment(event) {
    event.preventDefault();
    
    const content = document.getElementById('comment-content').value;
    
    if (!content.trim()) {
        alert('Please enter comment content');
        return;
    }
    
    const comment = {
        articleId: articleId,
        content: content
    };
    
    stompClient.send(`/app/comments/${articleId}/create`, {}, JSON.stringify(comment));
    
    document.getElementById('comment-content').value = '';
}

function deleteComment(commentId) {
    stompClient.send(`/app/comments/${articleId}/delete/${commentId}`, {}, {});
}

// Initialize WebSocket connection when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    connect();
    
    // Add event listener for comment form
    const commentForm = document.getElementById('comment-form');
    if (commentForm) {
        commentForm.addEventListener('submit', addComment);
    }
    
    // Cleanup on page unload
    window.addEventListener('beforeunload', disconnect);
});
