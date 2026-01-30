// ===============================
// ALERTA CONFIRMAR ELIMINACIÓN
// ===============================
function confirmarEliminacionBaja(id) {

	Swal.fire({
		title: '¿Eliminar esta baja?',
		text: 'Esta acción devolverá el stock al inventario y no se puede deshacer.',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#dc2626',
		cancelButtonColor: '#6b7280',
		confirmButtonText: 'Sí, eliminar',
		cancelButtonText: 'Cancelar'
	}).then((result) => {
		if (result.isConfirmed) {
			window.location.href = `/bajas/eliminar/${id}`;
		}
	});
}


// ===============================
// ALERTA POST-ELIMINACIÓN
// ===============================
document.addEventListener('DOMContentLoaded', () => {

	const params = new URLSearchParams(window.location.search);

	if (params.has('eliminado')) {
		Swal.fire({
			icon: 'success',
			title: 'Baja eliminada',
			text: 'El stock fue restaurado correctamente.',
			timer: 2000,
			showConfirmButton: false
		});
	}
});




// ===============================
// CONFIRMAR ACTUALIZAR
// ===============================
function confirmarActualizar() {
	Swal.fire({
		title: '¿Actualizar la baja?',
		text: 'Se guardarán los cambios realizados.',
		icon: 'question',
		showCancelButton: true,
		confirmButtonColor: '#16a34a', // verde
		cancelButtonColor: '#6b7280',
		confirmButtonText: 'Sí, actualizar',
		cancelButtonText: 'Cancelar'
	}).then((result) => {
		if (result.isConfirmed) {
			document.getElementById('form-editar-baja').submit();
		}
	});
}

// ===============================
// CONFIRMAR CANCELAR
// ===============================
function confirmarCancelar() {
	Swal.fire({
		title: '¿Cancelar edición?',
		text: 'Los cambios no guardados se perderán.',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#dc2626', // rojo
		cancelButtonColor: '#6b7280',
		confirmButtonText: 'Sí, salir',
		cancelButtonText: 'Seguir editando'
	}).then((result) => {
		if (result.isConfirmed) {
			window.location.href = '/bajas/listar';
		}
	});
}

document.addEventListener("DOMContentLoaded", () => {

	const form = document.getElementById("formBaja");
	const btnCancelar = document.getElementById("btnCancelar");

	/* ===============================
	   ALERTA GUARDAR (ESTILO IMAGEN)
	=============================== */
	if (form) {
		form.addEventListener("submit", function(e) {
			e.preventDefault();

			Swal.fire({
				title: "¿Guardar la baja?",
				text: "Esta acción descontará el stock del inventario.",
				icon: "warning",
				showCancelButton: true,

				// 👉 COLORES COMO LA IMAGEN
				background: "#ffffff",
				color: "#374151", // gris oscuro
				iconColor: "#f59e0b", // naranja warning

				confirmButtonText: "Sí, guardar",
				cancelButtonText: "Cancelar",

				confirmButtonColor: "#dc2626", // rojo
				cancelButtonColor: "#6b7280",  // gris

				reverseButtons: false
			}).then((result) => {
				if (result.isConfirmed) {
					form.submit();
				}
			});
		});
	}

	/* ===============================
	   ALERTA CANCELAR (MISMO ESTILO)
	=============================== */
	if (btnCancelar) {
		btnCancelar.addEventListener("click", function(e) {
			e.preventDefault();

			Swal.fire({
				title: "¿Cancelar registro?",
				text: "Se perderán los datos ingresados.",
				icon: "warning",

				showCancelButton: true,
				background: "#ffffff",
				color: "#374151",
				iconColor: "#f59e0b",

				confirmButtonText: "Sí, salir",
				cancelButtonText: "Cancelar",

				confirmButtonColor: "#dc2626",
				cancelButtonColor: "#6b7280"
			}).then((result) => {
				if (result.isConfirmed) {
					window.location.href = btnCancelar.getAttribute("href");
				}
			});
		});
	}

});



