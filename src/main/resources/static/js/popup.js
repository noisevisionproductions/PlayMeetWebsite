function showPopup(message, duration = 6000) {
    let popup = document.getElementById("popup");

    if (!popup) {
        popup = document.createElement("div");
        popup.id = "popup";
        popup.style.position = "fixed";
        popup.style.top = "100px";
        popup.style.left = "50%";
        popup.style.transform = "translateX(-50%)";
        popup.style.padding = "20px";
        popup.style.backgroundColor = "green";
        popup.style.color = "white";
        popup.style.zIndex = "1000";
        popup.style.display = "none";
        popup.style.borderRadius = "15px";
        document.body.appendChild(popup);
    }

    popup.textContent = message;

    popup.style.display = "block";

    setTimeout(() => {
        popup.style.display = "none";

        const url = new URL(window.location);
        url.searchParams.delete('postId');
        window.history.replaceState(null, '', url);
    }, duration);
}