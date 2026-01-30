// ALERTA REGISTRAR
function confirmarRegistrar(event) {
	event.preventDefault();

	Swal.fire({
		title: '¿Registrar producción?',
		text: 'La producción será guardada en el sistema',
		icon: 'question',
		showCancelButton: true,
		confirmButtonColor: '#16a34a',
		cancelButtonColor: '#6b7280',
		confirmButtonText: 'Sí, registrar',
		cancelButtonText: 'Cancelar'
	}).then((result) => {
		if (result.isConfirmed) {
			document.getElementById('formProduccion').submit();
		}
	});
}

// ALERTA CANCELAR
function confirmarCancelar(url) {
	Swal.fire({
		title: '¿Cancelar?',
		text: 'Los datos ingresados se perderán',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#ef4444',
		cancelButtonColor: '#6b7280',
		confirmButtonText: 'Sí, salir',
		cancelButtonText: 'Seguir editando'
	}).then((result) => {
		if (result.isConfirmed) {
			window.location.href = url;
		}
	});
}

function confirmarEliminacion(elemento) {

	const id = elemento.getAttribute("data-id");

	Swal.fire({
		title: '¿Eliminar producción?',
		text: 'Esta acción no se puede deshacer',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#dc2626', // rojo
		cancelButtonColor: '#415a77',
		confirmButtonText: 'Sí, eliminar',
		cancelButtonText: 'Cancelar',
		reverseButtons: true
	}).then((result) => {
		if (result.isConfirmed) {
			window.location.href = `/produccion/eliminar/${id}`;
		}
	});
}

function confirmarCancelar() {
	Swal.fire({
		title: '¿Cancelar cambios?',
		text: 'Los cambios no guardados se perderán',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#6b7280', // gris
		cancelButtonColor: '#415a77',
		confirmButtonText: 'Sí, salir',
		cancelButtonText: 'Seguir editando',
		reverseButtons: true
	}).then((result) => {
		if (result.isConfirmed) {
			window.location.href = '/produccion/listar';
		}
	});
}

function confirmarGuardar(boton) {

	const form = boton.closest('form');

	Swal.fire({
		title: '¿Guardar cambios?',
		text: 'Se actualizará la información de la producción',
		icon: 'question',
		showCancelButton: true,
		confirmButtonColor: '#16a34a', // verde
		cancelButtonColor: '#6b7280',
		confirmButtonText: 'Sí, guardar',
		cancelButtonText: 'Cancelar',
		reverseButtons: true
	}).then((result) => {
		if (result.isConfirmed) {
			form.submit();
		}
	});
}

