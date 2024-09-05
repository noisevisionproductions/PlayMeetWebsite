import { initializeApp } from 'https://www.gstatic.com/firebasejs/10.13.1/firebase-app.js'

// Add Firebase products that you want to use
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
        // Initialize Firebase with config fetched from the backend
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

        $('#message').text('Logged in successfully').css('color', 'green');
    } catch (error) {
        console.error('Error during login:', error);
        let errorMessage = 'Unknown error occurred.';

        if (error.code) {
            switch (error.code) {
                case 'auth/user-not-found':
                    errorMessage = 'User not found for the given email.';
                    break;
                case 'auth/wrong-password':
                    errorMessage = 'Incorrect password.';
                    break;
                case 'auth/invalid-email':
                    errorMessage = 'Invalid email format.';
                    break;
                case 'auth/user-disabled':
                    errorMessage = 'The user account is disabled.';
                    break;
                case 'auth/too-many-requests':
                    errorMessage = 'Too many failed login attempts. Try again later.';
                    break;
                case 'auth/invalid-credential':
                    errorMessage = 'Invalid email or password.';
                    break;
                default:
                    errorMessage = `Login error: ${error.message}`;
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