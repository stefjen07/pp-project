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
    const outputEnc = document.getElementById("output-enc").checked;
    const outputArc = document.getElementById("output-arc").checked;
    const filePicker = document.getElementById("file-picker");

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
        outputType: outputType,
        outputEncryption: outputEnc,
        outputArchivation: outputArc
    })], { type: "application/json"});

    formData.append("request", filePicker.files[0]);
    formData.append("parameters", parameters);

    const request = new XMLHttpRequest();
    request.open("POST", "/arithmetic/calculate");
    request.onreadystatechange = function(){
        if (request.readyState !== 4) return;
        if (request.status !== 200 && request.status !== 304) {
            alert('HTTP error ' + request.status);
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