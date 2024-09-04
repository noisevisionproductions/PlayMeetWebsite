import { initializeApp } from 'https://www.gstatic.com/firebasejs/10.13.1/firebase-app.js'

// Add Firebase products that you want to use
import { getAuth } from 'https://www.gstatic.com/firebasejs/10.13.1/firebase-auth.js'
import { signInWithEmailAndPassword } from 'https://www.gstatic.com/firebasejs/10.13.1/firebase-auth.js';

// Konfiguracja Firebase
const firebaseConfig = {
    apiKey: "AIzaSyA2o_c5Z2qWyyl04Tymo0pKe_Ue0ZqlhB0",
    authDomain: "zagrajmy-b418d.firebaseapp.com",
    projectId: "zagrajmy-b418d",
    storageBucket: "zagrajmy-b418d.appspot.com",
    messagingSenderId: "114161405415850164738",
    appId: "AIzaSyA2o_c5Z2qWyyl04Tymo0pKe_Ue0ZqlhB0"
};

// Inicjalizacja Firebase
const app = initializeApp(firebaseConfig);

// Uzyskanie referencji do usługi auth
const auth = getAuth(app);

// Funkcja logowania z użyciem emaila i hasła
const handleLogin = async () => {
    // Pobierz dane z formularza (przykład)
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        if (!email || !password){
            throw new Error('Proszę wypełnić wszystkie pola.');
        }

        const userCredential = await signInWithEmailAndPassword(auth, email, password);
        const token = await userCredential.user.getIdToken();

        // Wysyłanie tokena do backendu
        const response = await fetch('http://localhost:8080/verifyToken', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ token })
        });

        if (!response.ok) {
            throw new Error('Błąd weryfikacji tokena na serwerze.');
        }

        const data = await response.text();
        $('#message').text('Zalogowano').css('color', 'green');
    } catch (error) {
        console.error('Błąd podczas logowania:', error);
        let errorMessage = 'Wystąpił nieznany błąd.';

        if (error.code) {
            switch (error.code) {
                case 'auth/user-not-found':
                    errorMessage = 'Nie znaleziono użytkownika o podanym adresie email.';
                    break;
                case 'auth/wrong-password':
                    errorMessage = 'Nieprawidłowe hasło.';
                    break;
                case 'auth/invalid-email':
                    errorMessage = 'Nieprawidłowy format adresu email.';
                    break;
                case 'auth/user-disabled':
                    errorMessage = 'Konto użytkownika zostało wyłączone.';
                    break;
                case 'auth/too-many-requests':
                    errorMessage = 'Zbyt wiele nieudanych prób logowania. Spróbuj ponownie później.';
                    break;
                case 'auth/invalid-credential':
                    errorMessage = 'E-mail lub hasło są nieprawidłowe.';
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