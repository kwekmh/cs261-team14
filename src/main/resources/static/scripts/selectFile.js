$('#selectFile').bind('change', function() {
    var fileName = $(this).val().split('\\').pop();
    $('#fileSelected').html(fileName);
});

$('#uploadButton').bind('click', function() {
    if ($('#selectFile').val() != '') {
        $('#fileUpload').submit();
    }
});