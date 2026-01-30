package com.bruder.controller;

import com.bruder.model.Bajas;
import com.bruder.model.InventarioFinal;
import com.bruder.service.IBajasService;
import com.bruder.service.IInventarioFinalService;
import com.bruder.service.IUsuarioService;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/bajas")
public class ControllerBajas {

	@Autowired
	private IBajasService bajasService;

	@Autowired
	private IInventarioFinalService inventarioFinalService;

	@Autowired
	private IUsuarioService usuarioService;

	// Convertir numero string a numero emtero
	private int toInt(String valor) {
		if (valor == null || valor.isBlank()) {
			return 0;
		}
		return (int) Math.round(Double.parseDouble(valor));
	}

	// LISTAR BAJAS
//	@GetMapping("/listar")
//	public String listar(Model model) {
//
//		List<Bajas> bajas = bajasService.findAll();
//
//		int total = bajas.stream().mapToInt(b -> toInt(b.getCantidad())).sum();
//
//		model.addAttribute("bajas", bajas);
//		model.addAttribute("totalBajas", total);
//
//		return "bajas/listar";
//	}

	@GetMapping("/listar")
	public String listar(Model model) {

		List<Bajas> bajas = bajasService.findAll();

		List<String> cervezas = bajas.stream().map(b -> b.getInventarioFinal().getCerveza().getNombre()).distinct()
				.toList();

		model.addAttribute("bajas", bajas);
		model.addAttribute("cervezas", cervezas);

		return "bajas/listar";
	}

	// NUEVA BAJA
	@GetMapping("/nuevo")
	public String nuevaBaja(Model model) {

		Bajas baja = new Bajas();
		baja.setInventarioFinal(new InventarioFinal());

		model.addAttribute("baja", baja);
		cargarInventarioConStock(model);
		model.addAttribute("usuarios", usuarioService.findAll());

		return "bajas/nuevaBaja";
	}

	// GUARDAR
	@PostMapping("/guardar")
	public String guardar(@ModelAttribute("baja") Bajas baja, Model model) {

		Integer inventarioId = baja.getInventarioFinal().getId();
		Optional<InventarioFinal> optInv = inventarioFinalService.findById(inventarioId);

		if (optInv.isEmpty()) {
			model.addAttribute("errorStock", "Inventario Final no encontrado.");
			cargarInventarioConStock(model);
			return "bajas/nuevaBaja";
		}

		InventarioFinal inv = optInv.get();

		int actual = toInt(inv.getCantidad());
		int bajaCantidad = toInt(baja.getCantidad());

		if (bajaCantidad > actual) {
			model.addAttribute("errorStock", "La cantidad ingresada supera el stock disponible: " + actual);
			cargarInventarioConStock(model);
			return "bajas/nuevaBaja";
		}

		inv.setCantidad(String.valueOf(actual - bajaCantidad));

		baja.setInventarioFinal(inv);
		baja.setCerveza(inv.getCerveza());

		inventarioFinalService.save(inv);
		bajasService.save(baja);

		return "redirect:/bajas/listar?creado";
	}

	// =====================================================
	// EDITAR
	// =====================================================
	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Integer id, Model model) {

		Optional<Bajas> opt = bajasService.findById(id);
		if (opt.isEmpty()) {
			return "redirect:/bajas/listar";
		}

		model.addAttribute("baja", opt.get());
		cargarInventarioConStock(model);
		model.addAttribute("usuarios", usuarioService.findAll());

		return "bajas/editarBaja";
	}

	// =====================================================
	// ACTUALIZAR
	// =====================================================
	@PostMapping("/actualizar")
	public String actualizar(@ModelAttribute("baja") Bajas baja, Model model) {

		Optional<Bajas> optOriginal = bajasService.findById(baja.getId());
		if (optOriginal.isEmpty()) {
			return "redirect:/bajas/listar";
		}

		Bajas original = optOriginal.get();
		InventarioFinal inv = original.getInventarioFinal();

		int stockAnterior = toInt(inv.getCantidad());
		int cantidadOriginal = toInt(original.getCantidad());
		int cantidadNueva = toInt(baja.getCantidad());

		int recuperado = stockAnterior + cantidadOriginal;

		if (cantidadNueva > recuperado) {
			model.addAttribute("errorStock", "La cantidad nueva supera el stock disponible: " + recuperado);
			cargarInventarioConStock(model);
			return "bajas/editarBaja";
		}

		inv.setCantidad(String.valueOf(recuperado - cantidadNueva));

		baja.setCerveza(original.getCerveza());
		baja.setInventarioFinal(inv);

		inventarioFinalService.save(inv);
		bajasService.update(baja);

		return "redirect:/bajas/listar?editado";
	}

	// =====================================================
	// ELIMINAR
	// =====================================================
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Integer id) {

		Optional<Bajas> opt = bajasService.findById(id);

		if (opt.isPresent()) {
			Bajas baja = opt.get();
			InventarioFinal inv = baja.getInventarioFinal();

			int stock = toInt(inv.getCantidad());
			int cantidad = toInt(baja.getCantidad());

			inv.setCantidad(String.valueOf(stock + cantidad));
			inventarioFinalService.save(inv);
		}

		bajasService.deleteById(id);

		return "redirect:/bajas/listar?eliminado";
	}

	// FILTRAR
	@GetMapping("/filtrar")
	public String filtrar(@RequestParam(required = false) List<String> producto,
			@RequestParam(required = false) String fechaInicio, @RequestParam(required = false) String fechaFin,
			Model model) {

		List<Bajas> filtradas = bajasService.findAll().stream()

				// 🍺 FILTRO CERVEZA
				.filter(b -> producto == null || producto.isEmpty()
						|| producto.stream()
								.anyMatch(p -> b.getInventarioFinal().getCerveza().getNombre().equalsIgnoreCase(p)))

				// 📅 FECHA INICIO
				.filter(b -> fechaInicio == null || fechaInicio.isBlank()
						|| b.getFecha_registro().compareTo(fechaInicio) >= 0)

				// 📅 FECHA FIN
				.filter(b -> fechaFin == null || fechaFin.isBlank() || b.getFecha_registro().compareTo(fechaFin) <= 0)

				.collect(Collectors.toList());

		int total = filtradas.stream().mapToInt(b -> toInt(b.getCantidad())).sum();

		List<String> cervezas = bajasService.findAll().stream()
				.map(b -> b.getInventarioFinal().getCerveza().getNombre()).distinct().toList();

		model.addAttribute("bajas", filtradas);
		model.addAttribute("cervezas", cervezas);
		model.addAttribute("totalBajas", total);

		return "bajas/listar";
	}

	// INVENTARIO CON STOCK
	private void cargarInventarioConStock(Model model) {

		List<InventarioFinal> inventario = inventarioFinalService.findAll().stream().filter(i -> i.getCerveza() != null)
				.filter(i -> toInt(i.getCantidad()) > 0).collect(Collectors.toList());

		model.addAttribute("inventarioDisponible", inventario);
	}

	@GetMapping("/exportarExcel")
	public void exportarExcel(@RequestParam(required = false) List<String> producto,
			@RequestParam(required = false) String fechaInicio, @RequestParam(required = false) String fechaFin,
			HttpServletResponse response) throws IOException {

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=bajas_bruder.xlsx");

		List<Bajas> lista = bajasService.findAll().stream()

				.filter(b -> producto == null || producto.isEmpty()
						|| producto.stream()
								.anyMatch(p -> b.getInventarioFinal().getCerveza().getNombre().equalsIgnoreCase(p)))

				.filter(b -> fechaInicio == null || fechaInicio.isBlank()
						|| b.getFecha_registro().compareTo(fechaInicio) >= 0)

				.filter(b -> fechaFin == null || fechaFin.isBlank() || b.getFecha_registro().compareTo(fechaFin) <= 0)

				.collect(Collectors.toList());

		// EXCEL
		try (Workbook workbook = new XSSFWorkbook()) {

			Sheet sheet = workbook.createSheet("Bajas Bruder");

			XSSFColor azulOscuro = new XSSFColor(new java.awt.Color(30, 60, 120), new DefaultIndexedColorMap());
			XSSFColor dorado = new XSSFColor(new java.awt.Color(255, 215, 100), new DefaultIndexedColorMap());
			XSSFColor grisFila = new XSSFColor(new java.awt.Color(245, 245, 245), new DefaultIndexedColorMap());

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
			titleCell.setCellValue("📉 Reporte de Bajas - Cervecería Bruder");
			titleCell.setCellStyle(titleStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

			// ===== ENCABEZADOS =====
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

			String[] columnas = { "Fecha", "Cerveza", "Cantidad", "Tipo", "Lote / Barril", "Detalle de Baja",
					"Usuario" };

			Row headerRow = sheet.createRow(2);
			for (int i = 0; i < columnas.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columnas[i]);
				cell.setCellStyle(headerStyle);
			}

			// ===== CONTENIDO =====
			int rowNum = 3;

			for (Bajas b : lista) {

				Row row = sheet.createRow(rowNum);

				row.createCell(0).setCellValue(b.getFecha_registro());
				row.createCell(1).setCellValue(b.getInventarioFinal().getCerveza().getNombre());
				row.createCell(2).setCellValue(b.getCantidad());
				row.createCell(3).setCellValue(b.getInventarioFinal().getCerveza().getTipo());
				row.createCell(4)
						.setCellValue(b.getInventarioFinal().getCerveza().getTipo().equals("Barril")
								? b.getInventarioFinal().getNumBarril()
								: b.getInventarioFinal().getLote());
				row.createCell(5).setCellValue(b.getRazon());
				row.createCell(6).setCellValue(b.getUsuario() != null ? b.getUsuario().getNombre() : "—");

				rowNum++;
			}

			for (int i = 0; i < columnas.length; i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(response.getOutputStream());
		}
	}

}
