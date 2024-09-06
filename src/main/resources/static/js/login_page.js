import { initializeApp } from 'https://www.gstatic.com/firebasejs/10.13.1/firebase-app.js'
import { getAuth } from 'https://www.gstatic.com/firebasejs/10.13.1/firebase-auth.js'
import { signInWithEmailAndPassword } from 'https://www.gstatic.com/firebasejs/10.13.1/firebase-auth.js';

let app;
let auth;

const fetchFirebaseConfig = async () => {
    try {
        const response = await fetch('http://localhost:8080/api/config/firebase');
        if (!response.ok) {
            throw new Error('Failed to load Firebase config');
        }
        const firebaseConfig = await response.json();
        app = initializeApp(firebaseConfig);
        auth = getAuth(app);
    } catch (error) {
        console.error('Error fetching Firebase config:', error);
    }
};

const handleLogin = async () => {
    if (!auth) {
        console.error('Firebase not initialized');
        return;
    }

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        if (!email || !password) {
            throw new Error('Please fill out all fields.');
        }

        const userCredential = await signInWithEmailAndPassword(auth, email, password);
        const token = await userCredential.user.getIdToken();

        const response = await fetch('http://localhost:8080/verifyToken', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ token })
        });

        if (!response.ok) {
            throw new Error('Token verification failed on the server.');
        }
        window.location.href = "/";

    } catch (error) {
        console.error('Error during login:', error);
        let errorMessage = 'Unknown error occurred.';

        if (error.code) {
            switch (error.code) {
                case 'auth/user-not-found':
                    errorMessage = 'Użytkownik o podanym e-mail nie istnieje.';
                    break;
                case 'auth/wrong-password':
                    errorMessage = 'Nieprawidłowe hasło.';
                    break;
                case 'auth/invalid-email':
                    errorMessage = 'Nieprawidłowy format e-mail';
                    break;
                case 'auth/user-disabled':
                    errorMessage = 'Konto użytkownika zostało zablokowane.';
                    break;
                case 'auth/too-many-requests':
                    errorMessage = 'Za dużo prób logownaia. Spróbuj ponownie później.';
                    break;
                case 'auth/invalid-credential':
                    errorMessage = 'Nieprawidłowy e-mail lub hasło.';
                    break;
                default:
                    errorMessage = `Błąd logowania: ${error.message}`;
            }
        } else if (error.message) {
            errorMessage = error.message;
        }
        $('#message').text(errorMessage).css('color', 'red');
    }
};

document.getElementById('loginButton').addEventListener('click', handleLogin);

// Fetch the Firebase config and initialize Firebase on page load
fetchFirebaseConfig();
