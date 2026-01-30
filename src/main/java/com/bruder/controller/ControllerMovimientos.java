package com.bruder.controller;

import com.bruder.model.Despachos;
import com.bruder.model.InventarioInicial;
import com.bruder.model.Movimiento;
import com.bruder.model.Produccion;
import com.bruder.service.IDespachosService;
import com.bruder.service.IInventarioInicialService;
import com.bruder.service.IProduccionService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import java.awt.Color;

@Controller
@RequestMapping("/movimientos")
public class ControllerMovimientos {

	@Autowired
	private IProduccionService produccionService;

	@Autowired
	private IDespachosService despachosService;

	@Autowired
	private IInventarioInicialService inventarioInicialService;

	private List<Movimiento> obtenerMovimientos() {

		List<Movimiento> movimientos = new ArrayList<>();

		List<InventarioInicial> inventarios = inventarioInicialService.findAll();
		List<Produccion> producciones = produccionService.listarTodos();
		List<Despachos> despachos = despachosService.listarTodos();

		for (InventarioInicial inv : inventarios) {
			Movimiento m = new Movimiento();
			m.setFechaMovimiento(inv.getFechaRegistro());
			m.setTipoMovimiento("INVENTARIO INICIAL");
			m.setCantidad(inv.getCantidad());
			m.setCerveza(inv.getCerveza());
			movimientos.add(m);
		}

		for (Produccion p : producciones) {
			Movimiento m = new Movimiento();
			m.setFechaMovimiento(p.getFechaProduccion());
			m.setTipoMovimiento("INGRESO");
			m.setCantidad(p.getCantidad());
			m.setCerveza(p.getCerveza());
			movimientos.add(m);
		}

		for (Despachos d : despachos) {
			Movimiento m = new Movimiento();
			m.setFechaMovimiento(d.getFechaDespacho());
			m.setTipoMovimiento("SALIDA");
			m.setCantidad(d.getCantidad());
			m.setCerveza(d.getCerveza());
			m.setPuntoVenta(d.getPuntoVenta());
			movimientos.add(m);
		}

		return movimientos;
	}

//	@GetMapping
//	public String listarMovimientos(Model model) {
//
//		List<Movimiento> movimientos = new ArrayList<>();
//
//		int totalInventarioInicial = 0;
//		int totalIngresos = 0;
//		int totalSalidas = 0;
//
//		// Obtener todos los registros
//		List<InventarioInicial> inventarios = inventarioInicialService.findAll();
//		List<Produccion> producciones = produccionService.listarTodos();
//		List<Despachos> despachos = despachosService.listarTodos();
//
//		// INVENTARIO INICIAL — sumar por nombre y tipo de presentación
//		Map<String, Integer> inventarioUnicoPorCerveza = new HashMap<>();
//
//		for (InventarioInicial inv : inventarios) {
//			if (inv.getCerveza() == null)
//				continue;
//
//			String nombre = inv.getCerveza().getNombre() != null ? inv.getCerveza().getNombre() : "Sin nombre";
//			String tipo = inv.getCerveza().getTipo() != null ? inv.getCerveza().getTipo() : "Sin tipo";
//			String clave = nombre.trim().toUpperCase() + " - " + tipo.trim().toUpperCase();
//
//			int cantidad = 0;
//			try {
//				if (inv.getCantidad() != null && !inv.getCantidad().isEmpty()) {
//					cantidad = Integer.parseInt(inv.getCantidad().replaceAll("[^0-9]", "").trim());
//				}
//			} catch (NumberFormatException e) {
//				System.out.println("⚠ Error al convertir cantidad en Inventario Inicial: " + inv.getCantidad());
//			}
//
//			inventarioUnicoPorCerveza.put(clave, inventarioUnicoPorCerveza.getOrDefault(clave, 0) + cantidad);
//
//			// Registrar el movimiento individual del inventario
//			Movimiento mov = new Movimiento();
//			mov.setFechaMovimiento(inv.getFechaRegistro());
//			mov.setTipoMovimiento("INVENTARIO INICIAL");
//			mov.setCantidad(String.valueOf(cantidad));
//			mov.setCerveza(inv.getCerveza());
//			movimientos.add(mov);
//		}
//
//		// 🔸 Calcular la suma total del inventario inicial (una única vez)
//		totalInventarioInicial = inventarioUnicoPorCerveza.values().stream().mapToInt(Integer::intValue).sum();
//
//		// 🔹 PRODUCCIÓN → tipo INGRESO
//		for (Produccion p : producciones) {
//			int cantidad = 0;
//			try {
//				if (p.getCantidad() != null && !p.getCantidad().isEmpty()) {
//					cantidad = Integer.parseInt(p.getCantidad().replaceAll("[^0-9]", "").trim());
//				}
//			} catch (NumberFormatException e) {
//				System.out.println("⚠ Error al convertir cantidad en Producción: " + p.getCantidad());
//			}
//
//			Movimiento mov = new Movimiento();
//			mov.setFechaMovimiento(p.getFechaProduccion());
//			mov.setTipoMovimiento("INGRESO");
//			mov.setCantidad(String.valueOf(cantidad));
//			mov.setCerveza(p.getCerveza());
//			movimientos.add(mov);
//
//			totalIngresos += cantidad;
//		}
//
//		// 🔹 DESPACHOS → tipo SALIDA
//		for (Despachos d : despachos) {
//			int cantidad = 0;
//			try {
//				if (d.getCantidad() != null && !d.getCantidad().isEmpty()) {
//					cantidad = Integer.parseInt(d.getCantidad().replaceAll("[^0-9]", "").trim());
//				}
//			} catch (NumberFormatException e) {
//				System.out.println("⚠ Error al convertir cantidad en Despacho: " + d.getCantidad());
//			}
//
//			Movimiento mov = new Movimiento();
//			mov.setFechaMovimiento(d.getFechaDespacho());
//			mov.setTipoMovimiento("SALIDA");
//			mov.setCantidad(String.valueOf(cantidad));
//			mov.setCerveza(d.getCerveza());
//			mov.setPuntoVenta(d.getPuntoVenta());
//			movimientos.add(mov);
//
//			totalSalidas += cantidad;
//		}
//
//		// ✅ Calcular saldo actual
//		// (el inventario inicial se suma solo una vez al total)
//		int saldoActual = totalInventarioInicial + totalIngresos - totalSalidas;
//
//		// ✅ Ordenar movimientos por tipo y luego por fecha
//		Map<String, Integer> prioridadTipo = Map.of("INVENTARIO INICIAL", 1, "INGRESO", 2, "SALIDA", 3);
//
//		movimientos = movimientos.stream()
//				.sorted(Comparator.comparing((Movimiento m) -> prioridadTipo.getOrDefault(m.getTipoMovimiento(), 99))
//						.thenComparing(m -> m.getCerveza() != null ? m.getCerveza().getNombre() : "")
//						.thenComparing(m -> m.getFechaMovimiento() != null ? m.getFechaMovimiento() : ""))
//				.collect(Collectors.toList());
//
//		// 🔹 Mostrar resultados en consola (para verificación)
//		System.out.println("🟢 Total Inventario Inicial: " + totalInventarioInicial);
//		System.out.println("🟢 Total Producción (Ingresos): " + totalIngresos);
//		System.out.println("🟢 Total Despachos (Salidas): " + totalSalidas);
//		System.out.println("🟢 Saldo Actual: " + saldoActual);
//
//		// 🔹 Enviar datos a la vista
//		model.addAttribute("movimientos", movimientos);
//		model.addAttribute("totalInventarioInicial", totalInventarioInicial);
//		model.addAttribute("totalIngresos", totalIngresos);
//		model.addAttribute("totalSalidas", totalSalidas);
//		model.addAttribute("saldoActual", saldoActual);
//
//		return "Movimientos/movimientos";
//	}

	@GetMapping
	public String listarMovimientos(Model model) {

		List<Movimiento> movimientos = obtenerMovimientos();

		List<String> cervezas = movimientos.stream().filter(m -> m.getCerveza() != null)
				.map(m -> m.getCerveza().getNombre()).distinct().sorted().collect(Collectors.toList());

		List<String> puntosVenta = movimientos.stream().filter(m -> m.getPuntoVenta() != null)
				.map(m -> m.getPuntoVenta().getNombre()).distinct().sorted().collect(Collectors.toList());

		model.addAttribute("movimientos", movimientos);
		model.addAttribute("cervezas", cervezas);
		model.addAttribute("puntosVenta", puntosVenta);

		return "Movimientos/movimientos";
	}

	// Exportar Excel
	@GetMapping("/exportarExcel")
	public void exportarMovimientosExcel(@RequestParam(required = false) List<String> cerveza,
			@RequestParam(required = false) List<String> puntoVenta, @RequestParam(required = false) String tipoCerveza,
			@RequestParam(required = false) String fechaInicio, @RequestParam(required = false) String fechaFin,
			HttpServletResponse response) throws Exception {

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=movimientos_filtrados_bruder.xlsx");

		// 🔹 1. Obtener TODOS los movimientos
		List<Movimiento> movimientos = obtenerMovimientos();

		// 🔹 2. Aplicar LOS MISMOS FILTROS que la tabla
		movimientos = movimientos.stream()
				.filter(m -> cerveza == null || cerveza.isEmpty()
						|| (m.getCerveza() != null && cerveza.contains(m.getCerveza().getNombre())))
				.filter(m -> puntoVenta == null || puntoVenta.isEmpty()
						|| (m.getPuntoVenta() != null && puntoVenta.contains(m.getPuntoVenta().getNombre())))
				.filter(m -> tipoCerveza == null || tipoCerveza.isEmpty()
						|| (m.getCerveza() != null && m.getCerveza().getTipo().equalsIgnoreCase(tipoCerveza)))
				.filter(m -> fechaInicio == null || fechaInicio.isEmpty()
						|| m.getFechaMovimiento().compareTo(fechaInicio) >= 0)
				.filter(m -> fechaFin == null || fechaFin.isEmpty() || m.getFechaMovimiento().compareTo(fechaFin) <= 0)
				.collect(Collectors.toList());

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Movimientos Filtrados");

			// 🎨 Colores
			XSSFColor azul = new XSSFColor(new Color(65, 90, 119), new DefaultIndexedColorMap());
			XSSFColor gris = new XSSFColor(new Color(240, 240, 240), new DefaultIndexedColorMap());

			// 🏷️ Header
			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.WHITE.getIndex());
			headerStyle.setFont(headerFont);
			((XSSFCellStyle) headerStyle).setFillForegroundColor(azul);
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			headerStyle.setBorderBottom(BorderStyle.THIN);

			// 📄 Body
			CellStyle bodyStyle = workbook.createCellStyle();
			bodyStyle.setBorderBottom(BorderStyle.THIN);
			bodyStyle.setAlignment(HorizontalAlignment.CENTER);

			CellStyle bodyAlt = workbook.createCellStyle();
			bodyAlt.cloneStyleFrom(bodyStyle);
			((XSSFCellStyle) bodyAlt).setFillForegroundColor(gris);
			bodyAlt.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			// 🏷️ Encabezados
			String[] columnas = { "Fecha", "Tipo Movimiento", "Cerveza", "Tipo Cerveza", "Cantidad", "Punto de Venta" };
			Row headerRow = sheet.createRow(0);

			for (int i = 0; i < columnas.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columnas[i]);
				cell.setCellStyle(headerStyle);
			}

			// 📊 Datos
			int rowNum = 1;
			for (Movimiento m : movimientos) {
				Row row = sheet.createRow(rowNum);
				CellStyle estilo = rowNum % 2 == 0 ? bodyAlt : bodyStyle;

				row.createCell(0).setCellValue(m.getFechaMovimiento());
				row.createCell(1).setCellValue(m.getTipoMovimiento());
				row.createCell(2).setCellValue(m.getCerveza() != null ? m.getCerveza().getNombre() : "—");
				row.createCell(3).setCellValue(m.getCerveza() != null ? m.getCerveza().getTipo() : "—");
				row.createCell(4).setCellValue(m.getCantidad());
				row.createCell(5).setCellValue(m.getPuntoVenta() != null ? m.getPuntoVenta().getNombre() : "—");

				for (int i = 0; i < 6; i++) {
					row.getCell(i).setCellStyle(estilo);
				}

				rowNum++;
			}

			for (int i = 0; i < columnas.length; i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(response.getOutputStream());
		}
	}

	@GetMapping("/filtrar")
	public String filtrarMovimientos(@RequestParam(required = false) List<String> cerveza,
			@RequestParam(required = false) List<String> puntoVenta, @RequestParam(required = false) String tipoCerveza,
			@RequestParam(required = false) String fechaInicio, @RequestParam(required = false) String fechaFin,
			Model model) {

		// 🔹 1. TODOS los movimientos (para filtros)
		List<Movimiento> todos = obtenerMovimientos();

		// 🔹 2. SOLO la tabla se filtra
		List<Movimiento> movimientos = todos.stream()
				.filter(m -> cerveza == null || cerveza.isEmpty()
						|| (m.getCerveza() != null && cerveza.contains(m.getCerveza().getNombre())))
				.filter(m -> puntoVenta == null || puntoVenta.isEmpty()
						|| (m.getPuntoVenta() != null && puntoVenta.contains(m.getPuntoVenta().getNombre())))
				.filter(m -> tipoCerveza == null || tipoCerveza.isEmpty()
						|| (m.getCerveza() != null && m.getCerveza().getTipo().equalsIgnoreCase(tipoCerveza)))
				.filter(m -> fechaInicio == null || fechaInicio.isEmpty()
						|| m.getFechaMovimiento().compareTo(fechaInicio) >= 0)
				.filter(m -> fechaFin == null || fechaFin.isEmpty() || m.getFechaMovimiento().compareTo(fechaFin) <= 0)
				.collect(Collectors.toList());

		// 🔹 3. Filtros SIEMPRE desde TODOS
		List<String> cervezas = todos.stream().filter(m -> m.getCerveza() != null).map(m -> m.getCerveza().getNombre())
				.distinct().sorted().collect(Collectors.toList());

		List<String> puntosVenta = todos.stream().filter(m -> m.getPuntoVenta() != null)
				.map(m -> m.getPuntoVenta().getNombre()).distinct().sorted().collect(Collectors.toList());

		// 🔹 4. Enviar a la vista
		model.addAttribute("movimientos", movimientos);
		model.addAttribute("cervezas", cervezas);
		model.addAttribute("puntosVenta", puntosVenta);

		// 🔹 5. Mantener filtros activos
		model.addAttribute("filtroCerveza", cerveza);
		model.addAttribute("filtroPuntoVenta", puntoVenta);
		model.addAttribute("filtroTipo", tipoCerveza);
		model.addAttribute("filtroFechaInicio", fechaInicio);
		model.addAttribute("filtroFechaFin", fechaFin);

		return "Movimientos/movimientos";
	}

}
