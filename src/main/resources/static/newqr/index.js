const INITIAL_BTN_LABEL = 'Generate QR Code';
const LOADING_BTN_LABEL = 'Generate QR Code';
window.addEventListener('load', function () {
    const form = document.querySelector('form');
    form.querySelector('input').value = '';
    form.addEventListener("submit", onFormSubmit);
})

const onFormSubmit = async (event) => {
    event.preventDefault();
    const body = document.querySelector("body");
    try {
        const existingErrorMessage = body.querySelectorAll('span#error-message');
        if (existingErrorMessage[0]) {
            body.removeChild(existingErrorMessage[0]);
        }
        const existingImgs = body.querySelectorAll("img");
        if (existingImgs) {
            for (const existingImg of existingImgs) {
                body.removeChild(existingImg);
            }
        }
        const existingLinks = body.querySelectorAll("a");
        if (existingLinks) {
            for (const existingLink of existingLinks) {
                body.removeChild(existingLink);
            }
        }
        document.querySelector('button[type="submit"]').innerHTML = LOADING_BTN_LABEL;
        const data = {
            content: new FormData(event.target).get("content")
        };
        const response = await fetch('/generate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'accept': 'application/json'
            },
            body: JSON.stringify(data)
        })
        if (response.ok) {
            const responseData = await response.json();
            const imageData = "data:image/png;base64," + responseData.content;

            const img = document.createElement("img");
            img.setAttribute("src", imageData);
            body.appendChild(img);
            const downloadLink = document.createElement("a");
            downloadLink.setAttribute("href", imageData);
            downloadLink.setAttribute("download", "qrcode.png");
            downloadLink.innerText = "Download QR Code";
            body.appendChild(downloadLink);
        } else {
            console.error(response.status, await response.json())
        }
    } catch (error) {
        const errorMessage = document.createElement('span');
        errorMessage.setAttribute('id', 'error-message');
        errorMessage.style.width = '100%';
        errorMessage.style.textAlign = 'center';
        errorMessage.style.color = 'red';
        errorMessage.innerText = 'Could not create QR Code, please try again.';
        body.appendChild(errorMessage);
        console.error(error);
    } finally {
        document.querySelector('button[type="submit"]').innerHTML = INITIAL_BTN_LABEL;
    }
}