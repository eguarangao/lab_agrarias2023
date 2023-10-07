// miScript.js
function validar() {
    var selectElement = document.getElementById("formS:selectHoras"); // Reemplaza "selectHoras" con el ID de tu elemento select
    var selectedOptions = selectElement.selectedOptions;

    if (selectedOptions.length < 1) {
        alert("Debe seleccionar al menos 1 hora.");
        return;
    }

    if (selectedOptions.length > 3) {
        alert("No puede seleccionar más de 3 horas.");
        return;
    }

    // // Verificar que las opciones seleccionadas sean consecutivas
    // var values = Array.from(selectedOptions).map(function(option) {
    //     return parseInt(option.value); // Asegúrate de que los valores sean números
    // });
    //
    // for (var i = 0; i < values.length - 1; i++) {
    //     if (values[i] + 1 !== values[i + 1]) {
    //         alert("Debe seleccionar horas consecutivas.");
    //         return;
    //     }
    // }
}

function mostrarMensaje() {
    alert('¡Hola, mundo! 😃');
}


// miScript.js
function validarSeleccion() {
    var selectElement = document.getElementById("formS:selectHoras"); // Reemplaza "selectHoras" con el ID de tu select
    var selectedOptions = selectElement.selectedOptions;

    if (selectedOptions.length < 1) {
        alert("Debe seleccionar al menos 1 hora.");
        return;
    }

    if (selectedOptions.length > 3) {
        alert("No puede seleccionar más de 3 horas.");
        return;
    }

    // Verificar que las opciones seleccionadas sean consecutivas
    var values = Array.from(selectedOptions).map(function(option) {
        return parseInt(option.value); // Asegúrate de que los valores sean números
    });

    for (var i = 0; i < values.length - 1; i++) {
        if (values[i] + 1 !== values[i + 1]) {
            alert("Debe seleccionar horas consecutivas.");
            return;
        }
    }
}
