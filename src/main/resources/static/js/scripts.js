// static/js/scripts.js
function uploadPDF() {
    const fileInput = document.getElementById('pdfFile');
    const selectedFile = fileInput.files[0];

    // Check if a file is selected
    if (!selectedFile) {
        alert('Please select a PDF file before uploading.');
        return;
    }

    event.preventDefault(); // prevent default form submission

    const form = document.getElementById('pdfUploadForm');
    const formData = new FormData(form);

    fetch('/scrum/api/invoices/process', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error uploading PDF');
        }
        return response.json();
    })
    .then(data => {
        // Display an alert with the response
        alert('Invoice processed successfully: ' + JSON.stringify(data));
        // Clear the file input after successful upload
        fileInput.value = '';
    })
    .catch(error => {
        // Display an alert with the error message
        alert('Error uploading PDF: ' + error.message);
    });
}

