document.getElementById('loginForm').addEventListener('submit', function (e) {
    e.preventDefault();

    const id = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    fetch('/rest/login/', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ id, password })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Invalid login credentials.");
            }
            return response.json();
        })
        .then(token => {
            // Store token if needed for later use
            // localStorage.setItem("token", token.tokenID);
            window.location.href = 'userPage.html';
        })
        .catch(err => {
            document.getElementById('loginError').textContent = err.message;
        });
});
