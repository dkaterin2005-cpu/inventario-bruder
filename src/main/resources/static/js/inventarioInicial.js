function confirmarActualizar() {
	Swal.fire({
		title: '¿Actualizar inventario?',
		text: 'Los cambios se guardarán definitivamente',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#415a77',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Sí, actualizar',
		cancelButtonText: 'Cancelar'
	}).then((result) => {
		if (result.isConfirmed) {
			document.getElementById("formActualizarInventario").submit();
		}
	});
}

function confirmarCancelar() {
	Swal.fire({
		title: '¿Cancelar cambios?',
		text: 'Los datos no se guardarán',
		icon: 'info',
		showCancelButton: true,
		confirmButtonColor: '#d33',
		cancelButtonColor: '#415a77',
		confirmButtonText: 'Sí, cancelar',
		cancelButtonText: 'Volver'
	}).then((result) => {
		if (result.isConfirmed) {
			window.location.href = '/inventarioInicial';
		}
	});
}

function confirmarGuardar() {
	Swal.fire({
		title: '¿Guardar inventario?',
		text: 'Se registrará el inventario inicial',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#415a77',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Sí, guardar',
		cancelButtonText: 'Cancelar'
	}).then((result) => {
		if (result.isConfirmed) {
			Swal.fire({
				title: 'Guardando...',
				text: 'Espere un momento',
				allowOutsideClick: false,
				showConfirmButton: false,
				didOpen: () => {
					Swal.showLoading();
				}
			});

			setTimeout(() => {
				document.getElementById("formInventarioInicial").submit();
			}, 500);
		}
	});
}

function confirmarCancelar() {
	Swal.fire({
		title: '¿Cancelar registro?',
		text: 'Se perderán los datos ingresados',
		icon: 'info',
		showCancelButton: true,
		confirmButtonColor: '#d33',
		cancelButtonColor: '#415a77',
		confirmButtonText: 'Sí, salir',
		cancelButtonText: 'Seguir editando'
	}).then((result) => {
		if (result.isConfirmed) {
			window.location.href = '/inventarioInicial';
		}
	});
}

