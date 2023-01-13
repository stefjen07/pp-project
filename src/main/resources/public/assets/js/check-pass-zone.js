function checkPassZone() {
    document.getElementById("input-pass-zone").hidden = !document.getElementById("input-enc").checked;
    document.getElementById("output-pass-zone").hidden = !document.getElementById("output-enc").checked;
}