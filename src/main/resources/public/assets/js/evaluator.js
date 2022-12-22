function submitForm() {
    const inputOpt = document.getElementById("inputopt");
    const outputOpt = document.getElementById("outputopt");
    const inputEnc = document.getElementById("input-enc");
    const inputArc = document.getElementById("input-arc");
    const outputEnc = document.getElementById("output-enc");
    const outputArc = document.getElementById("output-arc");
    const filePicker = document.getElementById("file-picker");

    const formData = new FormData();

    const parameters = new Blob([JSON.stringify({
        inputType: inputOpt.getAttribute('value'),
        inputEncryption: inputEnc.value,
        inputArchivation: inputArc.value,
        outputType: outputOpt.getAttribute('value'),
        outputEncryption: outputEnc.value,
        outputArchivation: outputArc.value
    })], { type: "application/json"});

    formData.append("request", filePicker.files[0]);
    formData.append("parameters", parameters);

    const request = new XMLHttpRequest();
    request.open("POST", "/arithmetic/calculate");
    request.send(formData);
}