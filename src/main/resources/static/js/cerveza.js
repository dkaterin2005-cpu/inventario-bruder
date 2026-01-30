document.addEventListener("DOMContentLoaded", () => {

	const form = document.getElementById("formProducto");

	if (!form) return;

	form.addEventListener("submit", function(e) {
		e.preventDefault();

		// detectar si es crear o editar
		const id = form.querySelector("input[name='id']").value;
		const esEdicion = id && id.trim() !== "";

		Swal.fire({
			title: esEdicion ? "¿Actualizar producto?" : "¿Guardar producto?",
			text: esEdicion
				? "Los cambios se guardarán permanentemente."
				: "El nuevo producto será registrado en el sistema.",

			icon: "warning",

			showCancelButton: true,
			background: "#ffffff",
			color: "#374151",
			iconColor: "#f59e0b",

			confirmButtonText: esEdicion ? "Sí, actualizar" : "Sí, guardar",
			cancelButtonText: "Cancelar",

			confirmButtonColor: esEdicion ? "#2563eb" : "#16a34a", // azul editar / verde guardar
			cancelButtonColor: "#6b7280"
		}).then((result) => {
			if (result.isConfirmed) {
				form.submit();
			}
		});
	});

});
