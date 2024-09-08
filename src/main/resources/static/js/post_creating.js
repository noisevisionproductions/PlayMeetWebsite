const today = new Date().toISOString().split('T')[0];
const dateInput = document.getElementById('date');
dateInput.setAttribute('min', today);

function toggleDate() {
    const checkbox = document.getElementById('noDate');
    if (checkbox.checked) {
        dateInput.setAttribute('disabled', 'true');
        dateInput.removeAttribute('required');
    } else {
        dateInput.removeAttribute('disabled');
        dateInput.setAttribute('required', 'true');
    }
}

function toggleHour() {
    const hourInput = document.getElementById('time');
    const checkbox = document.getElementById('noHour');
    if (checkbox.checked) {
        hourInput.setAttribute('disabled', 'true');
        hourInput.removeAttribute('required');
    } else {
        hourInput.removeAttribute('disabled');
        hourInput.setAttribute('required', 'true');
    }
}
