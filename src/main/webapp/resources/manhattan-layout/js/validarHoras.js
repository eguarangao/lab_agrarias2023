$(document).ready(function() {
    var selectedIndexes = [];

    $('#formb\\:selectHoras').on('click', 'input[type=checkbox]', function() {
        var checkboxes = $('#formb\\:selectHoras input[type=checkbox]');
        selectedIndexes = [];

        checkboxes.each(function(index) {
            if ($(this).is(':checked')) {
                selectedIndexes.push(index);
            }
        });

        var isValid = true;
        for (var i = 1; i < selectedIndexes.length; i++) {
            if (selectedIndexes[i] !== selectedIndexes[i - 1] + 1) {
                isValid = false;
                break;
            }
        }

        if (!isValid) {
            alert('Selecciona elementos consecutivos.');
            $(this).prop('checked', false);
        }
    });
});
