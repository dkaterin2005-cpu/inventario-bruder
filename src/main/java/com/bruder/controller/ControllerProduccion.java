package com.bruder.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bruder.model.Cerveza;
import com.bruder.model.InventarioFinal;
import com.bruder.model.Produccion;
import com.bruder.service.IProduccionService;
import com.bruder.service.ICervezaService;
import com.bruder.service.IInventarioFinalService;

@Controller
@RequestMapping("/produccion")
public class ControllerProduccion {

	@Autowired
	private IProduccionService produccionService;

	@Autowired
	private ICervezaService cervezaService;

	@Autowired
	private IInventarioFinalService inventarioFinalService;

	// LISTAR PRODUCCIÓN
//	@GetMapping("/listar")
//	public String listar(Model model) {
//
//		model.addAttribute("producciones", produccionService.findAll());
//
//		return "Produccion/listarProduccion";
//	}
	@GetMapping("/listar")
	public String listar(Model model) {

		List<Produccion> producciones = produccionService.findAll();

		List<String> cervezas = producciones.stream().map(p -> p.getCerveza().getNombre()).distinct()
				.collect(Collectors.toList());

		model.addAttribute("producciones", producciones);
		model.addAttribute("cervezas", cervezas);

		return "Produccion/listarProduccion";
	}

	// MOSTRAR FORMULARIO NUEVO
	@GetMapping("/nuevo")
	public String mostrarFormulario(Model model) {

		List<Cerveza> cervezasActivas = cervezaService.findAll().stream().filter(Cerveza::isActivo)
				.collect(Collectors.toList());

		model.addAttribute("cervezas", cervezasActivas);
		model.addAttribute("produccion", new Produccion());

		return "Produccion/nuevaProduccion";
	}

	// GUARDAR
	@PostMapping("/guardar")
	public String guardar(@ModelAttribute Produccion produccion) {

		Cerveza cerveza = cervezaService.findById(produccion.getCerveza().getId()).orElse(null);
		produccion.setCerveza(cerveza);

		produccionService.save(produccion);

		// ACTUALIZAR INVENTARIO FINAL
		InventarioFinal inv = new InventarioFinal();
		inv.setCerveza(cerveza);
		inv.setCantidad(produccion.getCantidad());
		inv.setFechaRegistro(produccion.getFechaProduccion().toString());
		inv.setLote(produccion.getLote());
		inv.setNumBarril(produccion.getNumBarril());
		inv.setTipo(cerveza.getTipo());

		inventarioFinalService.save(inv);

		return "redirect:/produccion/listar";
	}

	// 🚀 EDITAR
	@GetMapping("/editar/{id}")
	public String editarProduccion(@PathVariable Integer id, Model model) {

		Produccion produccion = produccionService.findById(id).orElse(null);
		if (produccion == null)
			return "redirect:/produccion/listar";

		List<Cerveza> cervezasActivas = cervezaService.findAll().stream().filter(Cerveza::isActivo)
				.collect(Collectors.toList());

		model.addAttribute("cervezas", cervezasActivas);
		model.addAttribute("produccion", produccion);

		return "Produccion/editarProduccion";
	}

	//ELIMINAR
	@GetMapping("/eliminar/{id}")
	public String eliminarProduccion(@PathVariable Integer id) {
		produccionService.delete(id);
		return "redirect:/produccion/listar";
	}

	//EXPORTAR EXCEL
	@GetMapping("/exportarExcel")
	public void exportarExcel(@RequestParam(required = false) List<String> cerveza,
			@RequestParam(required = false) List<String> lote, @RequestParam(required = false) List<String> barril,
			@RequestParam(required = false) String fechaInicio, @RequestParam(required = false) String fechaFin,
			HttpServletResponse response) throws IOException {

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=produccion_filtrada.xlsx");

		// ✅ USAR FILTROS (NO findAll)
		List<Produccion> listaProduccion = produccionService.filtrar(cerveza, lote, barril, fechaInicio, fechaFin);

		try (Workbook workbook = new XSSFWorkbook()) {

			Sheet sheet = workbook.createSheet("Producción Bruder");

			XSSFColor azulOscuro = new XSSFColor(new java.awt.Color(30, 60, 120), new DefaultIndexedColorMap());
			XSSFColor dorado = new XSSFColor(new java.awt.Color(255, 215, 100), new DefaultIndexedColorMap());
			XSSFColor grisFila = new XSSFColor(new java.awt.Color(245, 245, 245), new DefaultIndexedColorMap());

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
			titleCell.setCellValue("🍺 Reporte de Producción - Cervecería Bruder");
			titleCell.setCellStyle(titleStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

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

			String[] columnas = { "ID", "Fecha Producción", "Cerveza", "Tipo", "Cantidad", "Lote", "N° Barril",
					"Vencimiento", "Observación" };

			Row headerRow = sheet.createRow(2);
			for (int i = 0; i < columnas.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columnas[i]);
				cell.setCellStyle(headerStyle);
			}

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

			for (Produccion p : listaProduccion) {

				Row row = sheet.createRow(rowNum);
				CellStyle estilo = rowNum % 2 == 0 ? bodyAlt : bodyStyle;
				int c = 0;

				Cell cel;

				cel = row.createCell(c++);
				cel.setCellValue(p.getId());
				cel.setCellStyle(estilo);

				cel = row.createCell(c++);
				cel.setCellValue(p.getFechaProduccion() != null ? p.getFechaProduccion().toString() : "");
				cel.setCellStyle(estilo);

				cel = row.createCell(c++);
				cel.setCellValue(p.getCerveza().getNombre());
				cel.setCellStyle(estilo);

				cel = row.createCell(c++);
				cel.setCellValue(p.getCerveza().getTipo());
				cel.setCellStyle(estilo);

				cel = row.createCell(c++);
				cel.setCellValue(p.getCantidad());
				cel.setCellStyle(estilo);

				cel = row.createCell(c++);
				cel.setCellValue(p.getLote());
				cel.setCellStyle(estilo);

				cel = row.createCell(c++);
				cel.setCellValue(p.getNumBarril());
				cel.setCellStyle(estilo);

				cel = row.createCell(c++);
				cel.setCellValue(p.getFechaVencimiento() != null ? p.getFechaVencimiento().toString() : "");
				cel.setCellStyle(estilo);

				cel = row.createCell(c++);
				cel.setCellValue(p.getObservacion());
				cel.setCellStyle(estilo);

				rowNum++;
			}

			for (int i = 0; i < columnas.length; i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(response.getOutputStream());
		}
	}

	@GetMapping("/filtrar")
	public String filtrarProduccion(@RequestParam(required = false) List<String> cerveza,
			@RequestParam(required = false) List<String> lote, @RequestParam(required = false) List<String> barril,

			// ✅ STRING, NO LocalDate
			@RequestParam(required = false) String fechaInicio, @RequestParam(required = false) String fechaFin,

			Model model) {

		List<Produccion> producciones = produccionService.filtrar(cerveza, lote, barril, fechaInicio, fechaFin);

		List<String> cervezas = producciones.stream().map(p -> p.getCerveza().getNombre()).distinct()
				.collect(Collectors.toList());

		model.addAttribute("producciones", producciones);
		model.addAttribute("cervezas", cervezas);

		return "Produccion/listarProduccion";
	}

}
