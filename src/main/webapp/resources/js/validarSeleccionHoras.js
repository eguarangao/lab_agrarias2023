// // // function controlarSeleccion() {
// // //     // Obtenemos el elemento del selectManyCheckbox
// // //     var selectManyCheckbox = document.getElementById('formb:selectHoras');
// // //
// // //     // Contador para llevar un registro de los elementos seleccionados
// // //     var seleccionados = 0;
// // //
// // //     // Variable para controlar si la selección es seguida
// // //     var seleccionSeguida = true;
// // //
// // //     // Manejador de eventos para el cambio de selección
// // //     selectManyCheckbox.addEventListener('click', function() {
// // //         // Obtenemos todos los elementos de checkbox dentro del selectManyCheckbox
// // //         var checkboxes = selectManyCheckbox.querySelectorAll('input[type="checkbox"]');
// // //
// // //         // Recorremos los checkboxes y contamos cuántos están seleccionados
// // //         seleccionados = 0;
// // //         seleccionSeguida = true;
// // //
// // //         checkboxes.forEach(function(checkbox) {
// // //             if (checkbox.checked) {
// // //                 seleccionados++;
// // //
// // //                 // Verificamos que la selección sea seguida
// // //                 if (seleccionados > 1 && !checkbox.previousElementSibling.checked) {
// // //                     seleccionSeguida = false;
// // //                 }
// // //             }
// // //         });
// // //
// // //         // Si se seleccionaron más de 3 elementos o la selección no es seguida, mostrar un mensaje de error
// // //         if (seleccionados > 3 || !seleccionSeguida) {
// // //             PrimeFaces.addMessage({
// // //                 severity: 'error',
// // //                 summary: 'Error de selección',
// // //                 detail: 'No puedes seleccionar más de 3 elementos o la selección no es seguida.'
// // //             });
// // //
// // //             checkboxes.forEach(function(checkbox) {
// // //                 if (checkbox.checked && seleccionados > 3) {
// // //                     checkbox.checked = false;
// // //                     seleccionados--;
// // //                 }
// // //                 if (!seleccionSeguida && checkbox.checked && !checkbox.previousElementSibling.checked) {
// // //                     checkbox.checked = false;
// // //                     seleccionados--;
// // //                 }
// // //             });
// // //         }
// // //     });
// // // }
// // //
// // // // Llamar a la función cuando se cargue la página
// // // document.addEventListener('DOMContentLoaded', function() {
// // //     controlarSeleccion();
// // // });
// //
// //
// // document.addEventListener('DOMContentLoaded', function () {
// //     var selectManyCheckbox = document.getElementById('selectHoras');
// //     var checkboxes = selectManyCheckbox.querySelectorAll('input[type="checkbox"]');
// //
// //     // Llevar un seguimiento de las casillas seleccionadas
// //     var selectedCheckboxes = [];
// //
// //     for (var i = 0; i < checkboxes.length; i++) {
// //         checkboxes[i].addEventListener('change', function (event) {
// //             var checkbox = event.target;
// //             var value = parseInt(checkbox.value); // Suponiendo que los valores son números
// //
// //             if (checkbox.checked) {
// //                 // Agregar el valor a la lista de seleccionados
// //                 selectedCheckboxes.push(value);
// //             } else {
// //                 // Eliminar el valor de la lista de seleccionados
// //                 var index = selectedCheckboxes.indexOf(value);
// //                 if (index !== -1) {
// //                     selectedCheckboxes.splice(index, 1);
// //                 }
// //             }
// //
// //             // Realizar validación en tiempo real
// //             if (selectedCheckboxes.length === 0) {
// //                 alert("Debes seleccionar al menos 1 elemento.");
// //             } else if (selectedCheckboxes.length > 3) {
// //                 alert("No puedes seleccionar más de 3 elementos.");
// //                 checkbox.checked = false; // Desmarcar la casilla seleccionada
// //             } else {
// //                 // Verificar si los elementos seleccionados son consecutivos
// //                 selectedCheckboxes.sort(function (a, b) {
// //                     return a - b;
// //                 });
// //                 for (var j = 0; j < selectedCheckboxes.length - 1; j++) {
// //                     if (selectedCheckboxes[j + 1] - selectedCheckboxes[j] !== 1) {
// //                         alert("Debes seleccionar elementos consecutivos.");
// //                         checkbox.checked = false; // Desmarcar la casilla seleccionada
// //                         break;
// //                     }
// //                 }
// //             }
// //         });
// //     }
// // });
//
//
//
// // validacion.js
//
// // Función para realizar la validación en tiempo real
// function validarSeleccion(element) {
//     var checkboxes = document.querySelectorAll('#selectHoras input[type="checkbox"]:checked');
//     var selectedCount = checkboxes.length;
//
//     if (selectedCount === 0) {
//         alert("Debes seleccionar al menos 1 elemento.");
//         element.checked = false;
//     } else if (selectedCount > 3) {
//         alert("No puedes seleccionar más de 3 elementos.");
//         element.checked = false;
//     } else {
//         var selectedValues = Array.from(checkboxes).map(function (checkbox) {
//             return parseInt(checkbox.value); // Suponiendo que los valores son números
//         });
//
//         selectedValues.sort(function (a, b) {
//             return a - b;
//         });
//
//         for (var i = 0; i < selectedValues.length - 1; i++) {
//             if (selectedValues[i + 1] - selectedValues[i] !== 1) {
//                 alert("Debes seleccionar elementos consecutivos.");
//                 element.checked = false;
//                 break;
//             }
//         }
//     }
// }
//
// // Agregar el evento onclick a las casillas de verificación
// var checkboxes = document.querySelectorAll('#selectHoras input[type="checkbox"]');
// checkboxes.forEach(function (checkbox) {
//     checkbox.onclick = function () {
//         validarSeleccion(checkbox);
//     };
// });



// function validarSeleccion() {
//     var checkboxes = document.querySelectorAll("input[type=checkbox][name='selectHoras']");
//     var seleccionados = Array.from(checkboxes).filter(function(checkbox) {
//         return checkbox.checked;
//     });
//
//     if (seleccionados.length < 1) {
//         alert("Debe seleccionar al menos 1 hora.");
//         return;
//     }
//
//     if (seleccionados.length > 3) {
//         alert("No puede seleccionar más de 3 horas.");
//         return;
//     }
//
//     // Verificar que las horas seleccionadas sean consecutivas
//     var fechasSeleccionadas = seleccionados.map(function(checkbox) {
//         return checkbox.value; // Ajusta esto según el valor que represente la fecha/hora
//     });
//
//     for (var i = 0; i < fechasSeleccionadas.length - 1; i++) {
//         if (fechasSeleccionadas[i] + 1 !== fechasSeleccionadas[i + 1]) {
//             alert("Debe seleccionar horas consecutivas.");
//             return;
//         }
//     }
// }
