// Load live counts on home page
function loadCounts() {
    fetch('/api/items/counts')
        .then(response => response.json())
        .then(data => {
            document.getElementById('lostCount').textContent = data.lost;
            document.getElementById('foundCount').textContent = data.found;
        })
        .catch(error => {
            console.error('Error loading counts:', error);
        });
}

// Submit item function (used in report pages)
function submitItem(status) {
    const item = {
        itemName: document.getElementById('itemName').value,
        category: document.getElementById('category').value,
        description: document.getElementById('description').value,
        location: document.getElementById('location').value,
        contactName: document.getElementById('contactName').value,
        contactEmail: document.getElementById('contactEmail').value,
        contactPhone: document.getElementById('contactPhone').value,
        status: status
    };

    fetch('/api/items', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(item)
    })
    .then(response => response.json())
    .then(data => {
        showMessage(`Item reported successfully! ID: ${data.id}`, 'success');
        // Reset form
        document.getElementById(status === 'LOST' ? 'lostItemForm' : 'foundItemForm').reset();
    })
    .catch(error => {
        showMessage('Error submitting item. Please try again.', 'danger');
        console.error('Error:', error);
    });
}

// Show message function
function showMessage(message, type) {
    const messageDiv = document.getElementById('message');
    messageDiv.innerHTML = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
}

// Load counts when page loads (for home page)
if (document.getElementById('lostCount')) {
    loadCounts();
    // Refresh counts every 10 seconds
    setInterval(loadCounts, 10000);
}