// ALERTA CONFIRMAR GUARDAR
function confirmarGuardar(form) {
	Swal.fire({
		title: '¿Guardar cambios?',
		text: 'El punto de venta será guardado',
		icon: 'question',
		showCancelButton: true,
		confirmButtonColor: '#415a77',
		cancelButtonColor: '#6b7280',
		confirmButtonText: 'Sí, guardar',
		cancelButtonText: 'Cancelar'
	}).then((result) => {
		if (result.isConfirmed) {
			form.submit();
		}
	});
}

// ALERTA CANCELAR
function confirmarCancelar(url) {
	Swal.fire({
		title: '¿Cancelar?',
		text: 'Los cambios no se guardarán',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#ef4444',
		cancelButtonColor: '#6b7280',
		confirmButtonText: 'Sí, salir',
		cancelButtonText: 'Seguir editando '
	}).then((result) => {
		if (result.isConfirmed) {
			window.location.href = url;
		}
	});
}



