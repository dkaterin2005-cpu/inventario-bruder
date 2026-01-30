package com.bruder.controller;

import com.bruder.model.Bajas;
import com.bruder.model.Despachos;
import com.bruder.model.InventarioInicial;
import com.bruder.model.Produccion;
import com.bruder.service.IBajasService;
import com.bruder.service.IDespachosService;
import com.bruder.service.IInventarioInicialService;
import com.bruder.service.IProduccionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.IndexedColors;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import java.util.LinkedHashMap;

@Controller
@RequestMapping("/inventarioFinal")
public class ControllerInventarioFinal {

	@Autowired
	private IInventarioInicialService inventarioInicialService;

	@Autowired
	private IProduccionService produccionService;

	@Autowired
	private IDespachosService despachosService;

	@Autowired
	private IBajasService bajasService;

	// LISTAR INVENTARIO FINAL
	@GetMapping("/listar")
	public String listarInventarioFinal(Model model, HttpSession session) {

		// ==============================
		// MAPA PRINCIPAL DE SALDOS
		// ==============================
		Map<String, Integer> saldos = new HashMap<>();

		// INVENTARIO INICIAL
		List<InventarioInicial> iniciales = inventarioInicialService.findAll();

		for (InventarioInicial ini : iniciales) {

			if (ini.getCerveza() == null)
				continue;

			String clave = buildKey(ini.getCerveza().getNombre(), ini.getCerveza().getTipo(), ini.getLote(),
					ini.getNumBarril());

			int cantidad = parseCantidad(ini.getCantidad());
			saldos.put(clave, saldos.getOrDefault(clave, 0) + cantidad);
		}

		// PRODUCCIÓN
		List<Produccion> producciones = produccionService.findAll();

		for (Produccion p : producciones) {

			if (p.getCerveza() == null)
				continue;

			String clave = buildKey(p.getCerveza().getNombre(), p.getCerveza().getTipo(), p.getLote(),
					p.getNumBarril());

			int cantidad = parseCantidad(p.getCantidad());
			saldos.put(clave, saldos.getOrDefault(clave, 0) + cantidad);
		}

		// DESPACHOS
		List<Despachos> despachos = despachosService.findAll();

		for (Despachos d : despachos) {

			if (d.getCerveza() == null) {
				System.out.println("⚠️ Despacho sin cerveza. ID: " + d.getId());
				continue;
			}

			String clave = buildKey(d.getCerveza().getNombre(), d.getCerveza().getTipo(), d.getLote(),
					d.getNumBarril());

			int cantidad = parseCantidad(d.getCantidad());
			saldos.put(clave, saldos.getOrDefault(clave, 0) - cantidad);
		}

		// BAJAS
		List<Bajas> bajas = bajasService.findAll();

		for (Bajas b : bajas) {

			if (b.getCerveza() == null || b.getInventarioFinal() == null) {
				System.out.println("⚠️ Baja incompleta. ID: " + b.getId());
				continue;
			}

			String clave = buildKey(b.getCerveza().getNombre(), b.getCerveza().getTipo(),
					b.getInventarioFinal().getLote(), b.getInventarioFinal().getNumBarril());

			int cantidad = parseCantidad(b.getCantidad());
			saldos.put(clave, saldos.getOrDefault(clave, 0) - cantidad);
		}

		// FILTRAR SOLO POSITIVOS
		Map<String, Integer> inventarioFiltrado = saldos.entrySet().stream().filter(e -> e.getValue() > 0)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		// INVENTARIO FINAL
		model.addAttribute("inventario", inventarioFiltrado);

		// GUARDAR INVENTARIO FINAL EN SESIÓN
		session.setAttribute("inventarioFinal", inventarioFiltrado);

		// TOTALES
		int totalInicial = iniciales.stream().mapToInt(i -> parseCantidad(i.getCantidad())).sum();

		int totalProduccion = producciones.stream().mapToInt(p -> parseCantidad(p.getCantidad())).sum();

		int totalDespachos = despachos.stream().mapToInt(d -> parseCantidad(d.getCantidad())).sum();

		int totalBajas = bajas.stream().mapToInt(b -> parseCantidad(b.getCantidad())).sum();

		int totalFinal = inventarioFiltrado.values().stream().mapToInt(Integer::intValue).sum();

		model.addAttribute("totalInicial", totalInicial);
		model.addAttribute("totalProduccion", totalProduccion);
		model.addAttribute("totalDespachos", totalDespachos);
		model.addAttribute("totalBajas", totalBajas);
		model.addAttribute("totalFinal", totalFinal);

		return "InventarioFinal/listar";
	}

	// HELPERS
	private String buildKey(String nombre, String tipo, String lote, String barril) {
		return nombre + "-" + tipo + "-" + (lote != null ? lote : "") + "-" + (barril != null ? barril : "");
	}

	private int parseCantidad(String cantidad) {
		try {
			return Integer.parseInt(cantidad == null ? "0" : cantidad.trim());
		} catch (Exception e) {
			return 0;
		}
	}

	@GetMapping("/exportarExcel")
	public void exportarExcel(HttpServletResponse response) throws IOException {

		// Content-Type correcto para Excel
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader("Content-Disposition", "attachment; filename=inventario_final_bruder.xlsx");

		Map<String, Integer> inventario = new HashMap<>();

		// CARGAR DATOS
		List<InventarioInicial> iniciales = inventarioInicialService.findAll();
		List<Produccion> producciones = produccionService.findAll();
		List<Despachos> despachos = despachosService.findAll();
		List<Bajas> bajas = bajasService.findAll();

		// INVENTARIO INICIAL
		for (InventarioInicial ini : iniciales) {
			if (ini.getCerveza() == null)
				continue;

			String key = buildKey(ini.getCerveza().getNombre(), ini.getCerveza().getTipo(), ini.getLote(),
					ini.getNumBarril());

			inventario.put(key, inventario.getOrDefault(key, 0) + parseCantidad(ini.getCantidad()));
		}

		// PRODUCCIÓN
		for (Produccion p : producciones) {
			if (p.getCerveza() == null)
				continue;

			String key = buildKey(p.getCerveza().getNombre(), p.getCerveza().getTipo(), p.getLote(), p.getNumBarril());

			inventario.put(key, inventario.getOrDefault(key, 0) + parseCantidad(p.getCantidad()));
		}

		// DESPACHOS
		for (Despachos d : despachos) {
			if (d.getCerveza() == null)
				continue;

			String key = buildKey(d.getCerveza().getNombre(), d.getCerveza().getTipo(), d.getLote(), d.getNumBarril());

			inventario.put(key, inventario.getOrDefault(key, 0) - parseCantidad(d.getCantidad()));
		}

		// BAJAS
		for (Bajas b : bajas) {
			if (b.getCerveza() == null || b.getInventarioFinal() == null)
				continue;

			String key = buildKey(b.getCerveza().getNombre(), b.getCerveza().getTipo(),
					b.getInventarioFinal().getLote(), b.getInventarioFinal().getNumBarril());

			inventario.put(key, inventario.getOrDefault(key, 0) - parseCantidad(b.getCantidad()));
		}

		// SOLO SALDOS POSITIVOS
		Map<String, Integer> filtrado = inventario.entrySet().stream().filter(e -> e.getValue() > 0)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		// CREAR EXCEL
		try (Workbook workbook = new XSSFWorkbook()) {

			Sheet sheet = workbook.createSheet("Inventario Final Bruder");

			// COLORES
			XSSFColor azulOscuro = new XSSFColor(new java.awt.Color(30, 60, 120), new DefaultIndexedColorMap());
			XSSFColor dorado = new XSSFColor(new java.awt.Color(255, 215, 100), new DefaultIndexedColorMap());
			XSSFColor grisFila = new XSSFColor(new java.awt.Color(245, 245, 245), new DefaultIndexedColorMap());

			// TÍTULO (SIN EMOJIS)
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
			titleCell.setCellValue("Reporte de Inventario Final - Cervecería Bruder");
			titleCell.setCellStyle(titleStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

			// ENCABEZADOS
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

			String[] columnas = { "Cerveza", "Tipo", "Lote", "Barril", "Cantidad Disponible" };

			Row headerRow = sheet.createRow(2);
			for (int i = 0; i < columnas.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columnas[i]);
				cell.setCellStyle(headerStyle);
			}

			// FILAS
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

			for (Map.Entry<String, Integer> e : filtrado.entrySet()) {

				String[] partes = e.getKey().split("-", -1);

				String cerveza = partes.length > 0 ? partes[0] : "";
				String tipo = partes.length > 1 ? partes[1] : "";
				String lote = partes.length > 2 ? partes[2] : "";
				String barril = partes.length > 3 ? partes[3] : "";

				Row row = sheet.createRow(rowNum);
				CellStyle estilo = (rowNum % 2 == 0) ? bodyAlt : bodyStyle;
				int c = 0;

				row.createCell(c).setCellValue(cerveza);
				row.getCell(c++).setCellStyle(estilo);

				row.createCell(c).setCellValue(tipo);
				row.getCell(c++).setCellStyle(estilo);

				row.createCell(c).setCellValue(lote.isEmpty() ? "N/A" : lote);
				row.getCell(c++).setCellStyle(estilo);

				row.createCell(c).setCellValue(barril.isEmpty() ? "N/A" : barril);
				row.getCell(c++).setCellStyle(estilo);

				row.createCell(c).setCellValue(e.getValue());
				row.getCell(c).setCellStyle(estilo);

				rowNum++;
			}

			// AUTOAJUSTAR COLUMNAS
			for (int i = 0; i < columnas.length; i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(response.getOutputStream());
		}

		// MUY IMPORTANTE
		response.getOutputStream().flush();
	}

	@GetMapping("/resumen")
	public String resumenInventario(Model model, HttpSession session) {

		// INVENTARIO FINAL DESDE SESIÓN
		Map<String, Integer> inventarioFinal = (Map<String, Integer>) session.getAttribute("inventarioFinal");

		if (inventarioFinal == null) {
			return "redirect:/inventarioFinal/listar";
		}

		// INVENTARIO POR PRESENTACIÓN
		// (Chocolate - Barril / Botella)
		Map<String, Integer> inventarioPorPresentacion = new HashMap<>();

		for (Map.Entry<String, Integer> entry : inventarioFinal.entrySet()) {

			String[] partes = entry.getKey().split("-");
			String cerveza = partes[0];
			String tipo = partes[1];

			String clave = cerveza + " - " + tipo;

			inventarioPorPresentacion.put(clave, inventarioPorPresentacion.getOrDefault(clave, 0) + entry.getValue());
		}

		model.addAttribute("inventarioPorPresentacion", inventarioPorPresentacion);

		// TOTALES GENERALES
		int totalFinal = inventarioFinal.values().stream().mapToInt(Integer::intValue).sum();

		model.addAttribute("totalFinal", totalFinal);

		// TOTALES POR PRESENTACIÓN
		int totalBarril = inventarioPorPresentacion.entrySet().stream().filter(e -> e.getKey().contains("Barril"))
				.mapToInt(Map.Entry::getValue).sum();

		int totalBotella = inventarioPorPresentacion.entrySet().stream().filter(e -> e.getKey().contains("Botella"))
				.mapToInt(Map.Entry::getValue).sum();

		model.addAttribute("totalBarril", totalBarril);
		model.addAttribute("totalBotella", totalBotella);

		// ALERTAS REALES DE STOCK BAJO
		Map<String, Integer> alertasStockBajo = new HashMap<>();

		for (Map.Entry<String, Integer> entry : inventarioPorPresentacion.entrySet()) {

			String clave = entry.getKey();
			int cantidad = entry.getValue();

			// Barril crítico
			if (clave.contains("Barril") && cantidad <= 2) {
				alertasStockBajo.put(clave, cantidad);
			}

			// Botella crítica
			if (clave.contains("Botella") && cantidad <= 10) {
				alertasStockBajo.put(clave, cantidad);
			}
		}

		model.addAttribute("alertasStockBajo", alertasStockBajo);

		// GRÁFICA POR SABOR
		Map<String, Integer> graficaPorSabor = new HashMap<>();

		for (Map.Entry<String, Integer> entry : inventarioFinal.entrySet()) {

			String[] partes = entry.getKey().split("-");
			String sabor = partes[0];

			graficaPorSabor.put(sabor, graficaPorSabor.getOrDefault(sabor, 0) + entry.getValue());
		}

		model.addAttribute("graficaPorSabor", graficaPorSabor);

		return "InventarioFinal/resumen";
	}

	@GetMapping("/resumen/excel")
	public void exportarResumenExcel(HttpServletResponse response, HttpSession session) throws IOException {

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=resumen_inventario_bruder.xlsx");

		Map<String, Integer> inventarioFinal = (Map<String, Integer>) session.getAttribute("inventarioFinal");

		if (inventarioFinal == null)
			return;

		// AGRUPAR POR CERVEZA + PRESENTACIÓN
		Map<String, Integer> resumen = new LinkedHashMap<>();

		for (Map.Entry<String, Integer> e : inventarioFinal.entrySet()) {
			String[] partes = e.getKey().split("-");
			String clave = partes[0] + " - " + partes[1];
			resumen.merge(clave, e.getValue(), Integer::sum);
		}

		try (Workbook workbook = new XSSFWorkbook()) {

			Sheet sheet = workbook.createSheet("Resumen Inventario");

			// COLORES
			XSSFColor azulOscuro = new XSSFColor(new java.awt.Color(30, 60, 120), new DefaultIndexedColorMap());

			XSSFColor dorado = new XSSFColor(new java.awt.Color(255, 215, 100), new DefaultIndexedColorMap());

			// TÍTULO
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
			titleCell.setCellValue("📊 Resumen Inventario Final - Cervecería Bruder");
			titleCell.setCellStyle(titleStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

			// ENCABEZADOS
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

			Row headerRow = sheet.createRow(2);
			String[] columnas = { "Cerveza", "Presentación", "Cantidad Total" };

			for (int i = 0; i < columnas.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columnas[i]);
				cell.setCellStyle(headerStyle);
			}

			// CONTENIDO
			int rowNum = 3;

			for (Map.Entry<String, Integer> e : resumen.entrySet()) {
				String[] p = e.getKey().split(" - ");

				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(p[0]);
				row.createCell(1).setCellValue(p[1]);
				row.createCell(2).setCellValue(e.getValue());
			}

			// AJUSTAR COLUMNAS
			for (int i = 0; i < columnas.length; i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(response.getOutputStream());
		}
	}

}