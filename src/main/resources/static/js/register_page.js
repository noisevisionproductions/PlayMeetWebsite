document.getElementById("registrationForm").addEventListener("submit", async function (event) {
    event.preventDefault();

    const form = event.target;
    const formData = new FormData(form);

    try {
        const response = await fetch(form.action, {
            method: 'POST',
            body: formData
        });

        if (!response.ok){
            const errorMessage = await response.text();
            document.getElementById("errorMessage").innerText = errorMessage;
        } else {
            window.location.href = "/login";
        }
    } catch (error) {
        console.error("Registration failed", error);
        document.getElementById("errorMessage").innerText = "Błąd podczas rejestracji. Spróbuj ponownie później.";
    }
});

const ageSelect = document.getElementById('age');
for (let i = 18; i <= 100; i++){
    const option = document.createElement('option');
    option.value = i;
    option.text = i;
    ageSelect.add(option);
}