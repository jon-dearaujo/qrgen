window.addEventListener('load', function () {
    const form = document.querySelector('form');
    form.addEventListener("submit", onFormSubmit);
})

const onFormSubmit = async (event) => {
    event.preventDefault();
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
        const body = document.querySelector("body");
        const imageData = "data:image/png;base64," + responseData.content;
        const existingImgs = body.querySelectorAll("img");
        if (existingImgs) {
            for (const existingImg of existingImgs) {
                body.removeChild(existingImg);
            }
        }
        const img = document.createElement("img");
        img.setAttribute("src", imageData);
        body.appendChild(img);
        const existingLinks = body.querySelectorAll("a");
        if (existingLinks) {
            for (const existingLink of existingLinks) {
                body.removeChild(existingLink);
            }
        }
        const downloadLink = document.createElement("a");
        downloadLink.setAttribute("href", imageData);
        downloadLink.setAttribute("download", "qrcode.png");
        downloadLink.innerText = "Download QR Code";
        body.appendChild(downloadLink);
    } else {
        console.log('ERROR', response.status, await response.json())
    }
}