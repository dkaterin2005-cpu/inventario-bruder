package com.bruder.controller;

import com.bruder.model.Despachos;
import com.bruder.model.InventarioFinal;
import com.bruder.service.ICervezaService;
import com.bruder.service.IDespachosService;
import com.bruder.service.IInventarioFinalService;
import com.bruder.service.IPuntosVentaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.bind.annotation.GetMapping;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

@Controller
@RequestMapping("/despachos")
public class ControllerDespachos {

	@Autowired
	private IDespachosService despachosService;

	@Autowired
	private IInventarioFinalService inventarioFinalService;

	@Autowired
	private IPuntosVentaService puntosVentaService;

	@Autowired
	private ICervezaService cervezaService;

	private void cargarFiltros(Model model) {

		// 🍺 TODAS las cervezas (NO desde despachos)
		model.addAttribute("cervezas", inventarioFinalService.obtenerCervezas());

		// 🍻 TODOS los tipos
		model.addAttribute("tipos", cervezaService.obtenerTipos());

		// 🏷 TODOS los lotes / barriles
		model.addAttribute("lotesBarriles", inventarioFinalService.obtenerLotesBarriles());

		// 🏪 TODOS los puntos de venta
		model.addAttribute("puntosVenta", puntosVentaService.listarNombres());
	}

	// FORMULARIO NUEVO DESPACHO
	@GetMapping("/nuevo")
	public String nuevoDespacho(Model model) {

		Despachos despacho = new Despachos();

		List<InventarioFinal> inventario = inventarioFinalService.findAll().stream().filter(i -> {
			try {
				return Double.parseDouble(i.getCantidad()) > 0;
			} catch (Exception e) {
				return false;
			}
		}).toList();

		model.addAttribute("inventarioDisponible", inventario);
		model.addAttribute("puntosVenta", puntosVentaService.findByActivoTrue());
		model.addAttribute("despacho", despacho);

		return "Despachos/formulario";
	}

	// ============================================================
	// GUARDAR NUEVO DESPACHO
	// ============================================================
	@PostMapping("/guardar")
	public String guardarDespacho(@ModelAttribute Despachos despacho) {

		Optional<InventarioFinal> optInv = inventarioFinalService.findById(despacho.getInventarioFinal().getId());

		if (!optInv.isPresent()) {
			throw new RuntimeException("InventarioFinal no encontrado");
		}

		InventarioFinal inv = optInv.get();

		// Asignar datos del inventario al despacho
		despacho.setCerveza(inv.getCerveza());
		despacho.setLote(inv.getLote());
		despacho.setNumBarril(inv.getNumBarril());

		// Descontar stock
		double cantDespachada = Double.parseDouble(despacho.getCantidad());
		double cantidadActual = Double.parseDouble(inv.getCantidad());

		double nuevaCantidad = cantidadActual - cantDespachada;
		if (nuevaCantidad < 0)
			nuevaCantidad = 0;

		inv.setCantidad(String.valueOf(nuevaCantidad));

		inventarioFinalService.save(inv);
		despachosService.save(despacho);

		return "redirect:/despachos/listar";
	}

	// ============================================================
	// LISTAR
	// ============================================================
//	@GetMapping("/listar")
//	public String listarDespachos(Model model) {
//
//		List<Despachos> lista = despachosService.findAllOrderByFechaRegistroDesc();
//		model.addAttribute("despachos", lista);
//
//		int total = lista.stream().mapToInt(d -> {
//			try {
//				return Integer.parseInt(d.getCantidad());
//			} catch (Exception e) {
//				return 0;
//			}
//		}).sum();
//
//		model.addAttribute("totalCantidad", total);
//
//		return "Despachos/listar";
//	}

	@GetMapping("/listar")
	public String listar(Model model) {

		// 📦 Tabla completa
		model.addAttribute("despachos", despachosService.listarTodos());

		// 🔥 FILTROS DESDE DATOS BASE (NO DESPACHOS)
		cargarFiltros(model);

		// limpiar estados de filtros
		model.addAttribute("filtroCerveza", null);
		model.addAttribute("filtroTipo", null);
		model.addAttribute("filtroLoteBarril", null);
		model.addAttribute("filtroPuntoVenta", null);
		model.addAttribute("filtroFechaInicio", null);
		model.addAttribute("filtroFechaFin", null);

		return "despachos/listar";
	}

	// EDITAR DESPACHO (FORM)
	@GetMapping("/editar/{id}")
	public String editarDespacho(@PathVariable Integer id, Model model) {

		Optional<Despachos> opt = despachosService.findById(id);
		if (!opt.isPresent()) {
			return "redirect:/despachos/listar";
		}

		Despachos despacho = opt.get();

		List<InventarioFinal> inventario = inventarioFinalService.findAll();

		model.addAttribute("despacho", despacho);
		model.addAttribute("cervezas", inventario.stream().map(i -> i.getCerveza()).distinct().toList());
		model.addAttribute("lotes", inventario);
		model.addAttribute("puntosVenta", puntosVentaService.findAll());

		return "Despachos/editar";
	}

	// ============================================================
	// ELIMINAR DESPACHO
	// ============================================================
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Integer id) {

		Optional<Despachos> opt = despachosService.findById(id);
		if (opt.isPresent()) {

			Despachos d = opt.get();
			InventarioFinal inv = d.getInventarioFinal();

			double cant = Double.parseDouble(d.getCantidad());
			double actual = Double.parseDouble(inv.getCantidad());

			// REVERTIR STOCK
			inv.setCantidad(String.valueOf(actual + cant));

			inventarioFinalService.save(inv);
			despachosService.deleteById(id);
		}

		return "redirect:/despachos/listar";
	}

	// ============================================================
	// ACTUALIZAR DESPACHO (🔥 FIX DE STOCK)
	// ============================================================
	@PostMapping("/actualizar")
	public String actualizar(@ModelAttribute Despachos despacho) {

		Optional<Despachos> opt = despachosService.findById(despacho.getId());

		if (!opt.isPresent()) {
			return "redirect:/despachos/listar";
		}

		Despachos original = opt.get();

		InventarioFinal inv = original.getInventarioFinal();

		// ============================
		// 1️⃣ DEVOLVER STOCK ANTERIOR
		// ============================
		double cantOriginal = Double.parseDouble(original.getCantidad());
		double stockActual = Double.parseDouble(inv.getCantidad());

		inv.setCantidad(String.valueOf(stockActual + cantOriginal));

		// ============================
		// 2️⃣ RESTAR NUEVA CANTIDAD
		// ============================
		double cantNueva = Double.parseDouble(despacho.getCantidad());
		double stockDespuesDeRevertir = Double.parseDouble(inv.getCantidad());

		double nuevoStock = stockDespuesDeRevertir - cantNueva;
		if (nuevoStock < 0)
			nuevoStock = 0;

		inv.setCantidad(String.valueOf(nuevoStock));
		inventarioFinalService.save(inv);

		// ============================
		// 3️⃣ ACTUALIZAR CAMPOS
		// ============================
		original.setFechaDespacho(despacho.getFechaDespacho());
		original.setCantidad(despacho.getCantidad());
		original.setObservacion(despacho.getObservacion());
		original.setPuntoVenta(despacho.getPuntoVenta());

		despachosService.save(original);

		return "redirect:/despachos/listar";
	}

	@GetMapping("/exportarExcel")
	public void exportarExcel(@RequestParam(required = false) List<String> cerveza,
			@RequestParam(required = false) List<String> tipo, @RequestParam(required = false) List<String> loteBarril,
			@RequestParam(required = false) List<String> puntoVenta, @RequestParam(required = false) String fechaInicio,
			@RequestParam(required = false) String fechaFin, HttpServletResponse response) throws IOException {

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=despachos_filtrados.xlsx");

		// 🔥 APLICAR LOS MISMOS FILTROS
		List<Despachos> lista = despachosService.findAllOrderByFechaRegistroDesc().stream()

				// 🍺 CERVEZA
				.filter(d -> cerveza == null || cerveza.isEmpty()
						|| cerveza.contains(d.getInventarioFinal().getCerveza().getNombre()))

				// 🍻 TIPO
				.filter(d -> tipo == null || tipo.isEmpty()
						|| tipo.contains(d.getInventarioFinal().getCerveza().getTipo()))

				// 🏷 LOTE / BARRIL
				.filter(d -> loteBarril == null || loteBarril.isEmpty()
						|| loteBarril.contains(d.getInventarioFinal().getCerveza().getTipo().equals("Barril")
								? d.getInventarioFinal().getNumBarril()
								: d.getInventarioFinal().getLote()))

				// 🏪 PUNTO DE VENTA
				.filter(d -> puntoVenta == null || puntoVenta.isEmpty()
						|| (d.getPuntoVenta() != null && puntoVenta.contains(d.getPuntoVenta().getNombre())))

				// 📅 FECHAS
				.filter(d -> fechaInicio == null || fechaInicio.isBlank()
						|| d.getFechaDespacho().compareTo(fechaInicio) >= 0)

				.filter(d -> fechaFin == null || fechaFin.isBlank() || d.getFechaDespacho().compareTo(fechaFin) <= 0)

				.toList();

		try (Workbook workbook = new XSSFWorkbook()) {

			Sheet sheet = workbook.createSheet("Despachos Filtrados");

			XSSFColor azulOscuro = new XSSFColor(new java.awt.Color(30, 60, 120), new DefaultIndexedColorMap());
			XSSFColor dorado = new XSSFColor(new java.awt.Color(255, 215, 100), new DefaultIndexedColorMap());
			XSSFColor grisFila = new XSSFColor(new java.awt.Color(245, 245, 245), new DefaultIndexedColorMap());

			// ================= TÍTULO =================
			CellStyle titleStyle = workbook.createCellStyle();
			XSSFFont titleFont = ((XSSFWorkbook) workbook).createFont();
			titleFont.setBold(true);
			titleFont.setFontHeight(16);
			titleFont.setColor(IndexedColors.DARK_BLUE.getIndex());
			titleStyle.setFont(titleFont);
			titleStyle.setAlignment(HorizontalAlignment.CENTER);
			((XSSFCellStyle) titleStyle).setFillForegroundColor(dorado);
			titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Row titleRow = sheet.createRow(0);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellValue("📦 Reporte de Despachos Filtrados - Bruder");
			titleCell.setCellStyle(titleStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

			// ================= ENCABEZADOS =================
			CellStyle headerStyle = workbook.createCellStyle();
			XSSFFont headerFont = ((XSSFWorkbook) workbook).createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.WHITE.getIndex());
			headerStyle.setFont(headerFont);
			((XSSFCellStyle) headerStyle).setFillForegroundColor(azulOscuro);
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);

			headerStyle.setBorderBottom(BorderStyle.THIN);
			headerStyle.setBorderTop(BorderStyle.THIN);
			headerStyle.setBorderLeft(BorderStyle.THIN);
			headerStyle.setBorderRight(BorderStyle.THIN);

			String[] columnas = { "Fecha", "Cerveza", "Tipo", "Lote / Barril", "Cantidad", "Punto de Venta",
					"Observación" };

			Row headerRow = sheet.createRow(2);
			for (int i = 0; i < columnas.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columnas[i]);
				cell.setCellStyle(headerStyle);
			}

			// ================= FILAS =================
			CellStyle bodyStyle = workbook.createCellStyle();
			bodyStyle.setAlignment(HorizontalAlignment.CENTER);
			bodyStyle.setBorderBottom(BorderStyle.THIN);
			bodyStyle.setBorderTop(BorderStyle.THIN);
			bodyStyle.setBorderLeft(BorderStyle.THIN);
			bodyStyle.setBorderRight(BorderStyle.THIN);

			CellStyle bodyAlt = workbook.createCellStyle();
			bodyAlt.cloneStyleFrom(bodyStyle);
			((XSSFCellStyle) bodyAlt).setFillForegroundColor(grisFila);
			bodyAlt.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			int rowNum = 3;

			for (Despachos d : lista) {
				Row row = sheet.createRow(rowNum);
				CellStyle estilo = rowNum % 2 == 0 ? bodyAlt : bodyStyle;

				int c = 0;

				row.createCell(c).setCellValue(d.getFechaDespacho());
				row.getCell(c++).setCellStyle(estilo);

				row.createCell(c).setCellValue(d.getInventarioFinal().getCerveza().getNombre());
				row.getCell(c++).setCellStyle(estilo);

				row.createCell(c).setCellValue(d.getInventarioFinal().getCerveza().getTipo());
				row.getCell(c++).setCellStyle(estilo);

				row.createCell(c)
						.setCellValue(d.getInventarioFinal().getCerveza().getTipo().equals("Barril")
								? d.getInventarioFinal().getNumBarril()
								: d.getInventarioFinal().getLote());
				row.getCell(c++).setCellStyle(estilo);

				row.createCell(c).setCellValue(d.getCantidad());
				row.getCell(c++).setCellStyle(estilo);

				row.createCell(c).setCellValue(d.getPuntoVenta() != null ? d.getPuntoVenta().getNombre() : "—");
				row.getCell(c++).setCellStyle(estilo);

				row.createCell(c).setCellValue(d.getObservacion() != null ? d.getObservacion() : "");
				row.getCell(c).setCellStyle(estilo);

				rowNum++;
			}

			for (int i = 0; i < columnas.length; i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(response.getOutputStream());
		}
	}

	@GetMapping("/filtrar")
	public String filtrarDespachos(@RequestParam(required = false) List<String> cerveza,
			@RequestParam(required = false) List<String> tipo, @RequestParam(required = false) List<String> loteBarril,
			@RequestParam(required = false) List<String> puntoVenta, @RequestParam(required = false) String fechaInicio,
			@RequestParam(required = false) String fechaFin, Model model) {

		// 📦 Tabla filtrada
		model.addAttribute("despachos",
				despachosService.filtrar(cerveza, tipo, loteBarril, puntoVenta, fechaInicio, fechaFin));

		// 🔥 LOS FILTROS SIEMPRE COMPLETOS
		cargarFiltros(model);

		// 🔒 Mantener seleccionados
		model.addAttribute("filtroCerveza", cerveza);
		model.addAttribute("filtroTipo", tipo);
		model.addAttribute("filtroLoteBarril", loteBarril);
		model.addAttribute("filtroPuntoVenta", puntoVenta);
		model.addAttribute("filtroFechaInicio", fechaInicio);
		model.addAttribute("filtroFechaFin", fechaFin);

		return "despachos/listar";
	}

}
