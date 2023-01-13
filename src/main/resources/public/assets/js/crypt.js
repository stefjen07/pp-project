function downloadURI(uri, name) {
    let link = document.createElement("a");
    link.download = name;
    link.href = uri;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

function crypt(mode) {
    const filePicker = document.getElementById("enc-file-picker");
    const password = document.getElementById("enc-pass").value;
    const file = filePicker.files[0];

    const formData = new FormData();
    formData.append("request", file);
    formData.append("password", password);

    const request = new XMLHttpRequest();
    request.open("POST", "/crypt/" + mode);
    request.onreadystatechange = function() {
        if (request.readyState !== 4) return;
        if (request.status !== 200 && request.status !== 304) {
            alert('Error. Check your input and try again (' + request.status + ')');
            return;
        }

        downloadURI('data:' + file.type + ';base64,' + btoa(request.responseText), file.name);
    }
    request.send(formData);
}