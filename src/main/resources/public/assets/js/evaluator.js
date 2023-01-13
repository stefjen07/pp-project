function downloadURI(uri, name) {
    let link = document.createElement("a");
    link.download = name;
    link.href = uri;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

function submitForm() {
    const inputOpt = document.getElementById("inputopt");
    const outputOpt = document.getElementById("outputopt");

    const inputEnc = document.getElementById("input-enc").checked;
    const inputArc = document.getElementById("input-arc").checked;
    const inputPass = document.getElementById("input-pass").value;
    const outputEnc = document.getElementById("output-enc").checked;
    const outputArc = document.getElementById("output-arc").checked;
    const outputPass = document.getElementById("output-pass").value;

    const formData = new FormData();

    const inputOptions = inputOpt.getElementsByTagName('option');
    const outputOptions = outputOpt.getElementsByTagName('option');

    let inputType = '', outputType = '';

    for (let j = 0; j < inputOptions.length; j++) {
        if(inputOptions[j].selected) {
            inputType = inputOptions[j].value;
        }
    }

    for (let j = 0; j < outputOptions.length; j++) {
        if(outputOptions[j].selected) {
            outputType = outputOptions[j].value;
        }
    }

    const parameters = new Blob([JSON.stringify({
        inputType: inputType,
        inputEncryption: inputEnc,
        inputArchivation: inputArc,
        inputPassword: inputPass,
        outputType: outputType,
        outputEncryption: outputEnc,
        outputArchivation: outputArc,
        outputPassword: outputPass
    })], { type: "application/json"});

    formData.append("request", filePicker.files[0]);
    formData.append("parameters", parameters);

    const request = new XMLHttpRequest();
    request.open("POST", "/arithmetic/calculate");
    request.onreadystatechange = function() {
        if (request.readyState !== 4) return;
        if (request.status !== 200 && request.status !== 304) {
            alert('Error. Check your input and try again (' + request.status + ')');
            return;
        }

        let responseType = 'text/plain';
        let extension = 'txt';
        if(outputType === 'json' || outputType === 'xml') {
            responseType = 'application/' + outputType;
            extension = outputType;
        }

        if(outputArc) {
            responseType = 'application/zip';
            extension = 'zip';
        }

        downloadURI('data:' + responseType + ';base64,' + btoa(request.responseText), 'results.' + extension);
    }
    request.send(formData);
}

const filePicker = document.getElementById('file-picker');
filePicker.onchange = function() {
    const inputOpt = document.getElementById("inputopt");
    const inputOptions = inputOpt.getElementsByTagName('option');

    switch (filePicker.files[0].type) {
        case "application/json":
            for (let j = 0; j < inputOptions.length; j++) {
                if(inputOptions[j].value === "json") {
                    inputOptions[j].selected = true;
                }
            }
            break;
        case "application/xml":
        case "text/xml":
            for (let j = 0; j < inputOptions.length; j++) {
                if(inputOptions[j].value === "xml") {
                    inputOptions[j].selected = true;
                }
            }
            break;
        case "text/plain":
            for (let j = 0; j < inputOptions.length; j++) {
                if(inputOptions[j].value === "txt") {
                    inputOptions[j].selected = true;
                }
            }
            break;
    }

    document.getElementById("input-arc").checked = /.+\.(tar|ar|arj|jar|cpio|zip|7z)/.test(filePicker.files[0].name);
};