package com.bruder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bruder.model.Cerveza;
import com.bruder.model.InventarioInicial;
import com.bruder.service.ICervezaService;
import com.bruder.service.IInventarioInicialService;
import com.bruder.service.IProduccionService;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

@Controller
@RequestMapping("/inventarioInicial")
public class ControllerInventarioInicial {

	@Autowired
	private IInventarioInicialService inventarioService;

	@Autowired
	private ICervezaService cervezaService;

	@Autowired
	private IProduccionService produccionService;

	// Listar todos los registros de inventario inicial
//	@GetMapping
//	public String listarInventario(Model model) {
//		List<InventarioInicial> inventarios = inventarioService.findAll();
//
//		// Total general = suma de todas las cantidades
//		double totalGeneral = inventarios.stream().mapToDouble(inv -> {
//			try {
//				return Double.parseDouble(inv.getCantidad().toString());
//			} catch (Exception e) {
//				return 0.0;
//			}
//		}).sum();
//
//		model.addAttribute("inventarios", inventarios);
//		model.addAttribute("totalGeneral", totalGeneral);
//		return "Inventario/inventarioInicial";
//	}

	@GetMapping
	public String listarInventario(Model model) {

		List<InventarioInicial> inventarios = inventarioService.findAll();

		model.addAttribute("inventarios", inventarios);

		// 🔥 DATOS PARA LOS FILTROS
		model.addAttribute("cervezas",
				inventarios.stream().map(inv -> inv.getCerveza().getNombre()).distinct().collect(Collectors.toList()));

		model.addAttribute("lotes", inventarios.stream().map(InventarioInicial::getLote)
				.filter(l -> l != null && !l.isEmpty()).distinct().collect(Collectors.toList()));

		model.addAttribute("barriles", inventarios.stream().map(inv -> String.valueOf(inv.getNumBarril()))
				.filter(b -> b != null).distinct().collect(Collectors.toList()));

		return "Inventario/inventarioInicial";
	}

	// Formulario para crear nuevo inventario
	@GetMapping("/nuevo")
	public String mostrarFormularioNuevo(Model model) {
		model.addAttribute("inventario", new InventarioInicial());

		List<Cerveza> cervezasActivas = cervezaService.findAll().stream().filter(Cerveza::isActivo)
				.collect(Collectors.toList());
		model.addAttribute("cervezas", cervezasActivas);

		model.addAttribute("producciones", produccionService.findAll());
		return "Inventario/nuevo_inventario";
	}

	// Guardar un nuevo registro
	@PostMapping("/guardar")
	public String guardarInventario(@ModelAttribute InventarioInicial inventario) {
		inventarioService.save(inventario);
		return "redirect:/inventarioInicial";
	}

	// Formulario para editar un inventario existente
	@GetMapping("/editar/{id}")
	public String mostrarFormularioEdicion(@PathVariable Integer id, Model model) {
		InventarioInicial inventario = inventarioService.findById(id).orElse(null);
		model.addAttribute("inventario", inventario);

		// ✅ Solo cervezas activas
		List<Cerveza> cervezasActivas = cervezaService.findAll().stream().filter(Cerveza::isActivo)
				.collect(Collectors.toList());
		model.addAttribute("cervezas", cervezasActivas);

		model.addAttribute("producciones", produccionService.findAll());
		return "Inventario/editarinventario";
	}

	// Actualizar registro existente
	@PostMapping("/actualizar")
	public String actualizarInventario(@ModelAttribute InventarioInicial inventario) {
		inventarioService.save(inventario);
		return "redirect:/inventarioInicial";
	}

	// 🔹 Filtrar inventario y calcular total general basado en cantidad
//	@GetMapping("/filtrar")
//	public String filtrarInventario(@RequestParam(required = false) String nombreCerveza,
//			@RequestParam(required = false) String tipo, @RequestParam(required = false) String lote,
//			@RequestParam(required = false) String numBarril, @RequestParam(required = false) String fechaInicio,
//			@RequestParam(required = false) String fechaFin, Model model) {
//
//		List<InventarioInicial> inventarios = inventarioService.findAll();
//
//		List<InventarioInicial> filtrados = inventarios.stream()
//				.filter(inv -> (nombreCerveza == null || nombreCerveza.isEmpty()
//						|| (inv.getCerveza() != null
//								&& inv.getCerveza().getNombre().toLowerCase().contains(nombreCerveza.toLowerCase()))))
//				.filter(inv -> (tipo == null || tipo.isEmpty()
//						|| (inv.getCerveza() != null
//								&& inv.getCerveza().getTipo().toLowerCase().contains(tipo.toLowerCase()))))
//				.filter(inv -> (lote == null || lote.isEmpty()
//						|| (inv.getLote() != null && inv.getLote().toLowerCase().contains(lote.toLowerCase()))))
//				.filter(inv -> (numBarril == null || numBarril.isEmpty()
//						|| (inv.getNumBarril() != null && String.valueOf(inv.getNumBarril()).contains(numBarril))))
//				.filter(inv -> {
//					if ((fechaInicio == null || fechaInicio.isEmpty()) && (fechaFin == null || fechaFin.isEmpty()))
//						return true;
//
//					String registro = inv.getFechaRegistro();
//					if (registro == null || registro.isEmpty())
//						return false;
//
//					boolean desde = (fechaInicio == null || fechaInicio.isEmpty())
//							|| registro.compareTo(fechaInicio) >= 0;
//					boolean hasta = (fechaFin == null || fechaFin.isEmpty()) || registro.compareTo(fechaFin) <= 0;
//					return desde && hasta;
//				}).collect(Collectors.toList());
//
//		// Suma de cantidades filtradas
//		double totalGeneral = filtrados.stream().mapToDouble(inv -> {
//			try {
//				return Double.parseDouble(inv.getCantidad().toString());
//			} catch (Exception e) {
//				return 0.0;
//			}
//		}).sum();
//
//		model.addAttribute("inventarios", filtrados);
//		model.addAttribute("totalGeneral", totalGeneral);
//		return "Inventario/inventarioInicial";
//	}

	@GetMapping("/filtrar")
	public String filtrarInventario(@RequestParam(required = false) List<String> cerveza,
			@RequestParam(required = false) String tipo, @RequestParam(required = false) List<String> lote,
			@RequestParam(required = false) List<String> numBarril, @RequestParam(required = false) String fechaInicio,
			@RequestParam(required = false) String fechaFin, Model model) {

		List<InventarioInicial> inventarios = inventarioService.findAll();

		List<InventarioInicial> filtrados = inventarios.stream()

				// 🍺 CERVEZA (checkbox / multiple)
				.filter(inv -> cerveza == null || cerveza.isEmpty() || cerveza.contains(inv.getCerveza().getNombre()))

				// 🧪 TIPO (input normal)
				.filter(inv -> tipo == null || tipo.isEmpty()
						|| (inv.getCerveza() != null && inv.getCerveza().getTipo().equalsIgnoreCase(tipo)))

				// 🧾 LOTE (checkbox / multiple)
				.filter(inv -> lote == null || lote.isEmpty() || lote.contains(inv.getLote()))

				// 🛢️ BARRIL (checkbox / multiple, manejado como String)
				.filter(inv -> numBarril == null || numBarril.isEmpty()
						|| numBarril.contains(String.valueOf(inv.getNumBarril())))

				// 📅 FECHAS (String yyyy-MM-dd)
				.filter(inv -> {
					if ((fechaInicio == null || fechaInicio.isEmpty()) && (fechaFin == null || fechaFin.isEmpty())) {
						return true;
					}

					String fecha = inv.getFechaRegistro();
					if (fecha == null || fecha.isEmpty())
						return false;

					boolean desde = (fechaInicio == null || fechaInicio.isEmpty()) || fecha.compareTo(fechaInicio) >= 0;

					boolean hasta = (fechaFin == null || fechaFin.isEmpty()) || fecha.compareTo(fechaFin) <= 0;

					return desde && hasta;
				})

				.collect(Collectors.toList());

		// 🔢 TOTAL GENERAL
		double totalGeneral = filtrados.stream().mapToDouble(inv -> {
			try {
				return Double.parseDouble(inv.getCantidad().toString());
			} catch (Exception e) {
				return 0.0;
			}
		}).sum();

		// 📦 TABLA
		model.addAttribute("inventarios", filtrados);
		model.addAttribute("totalGeneral", totalGeneral);

		// 🔁 RECARGAR FILTROS (CLAVE PARA QUE NO DESAPAREZCAN)
		model.addAttribute("cervezas",
				inventarios.stream().map(inv -> inv.getCerveza().getNombre()).distinct().collect(Collectors.toList()));

		model.addAttribute("lotes", inventarios.stream().map(InventarioInicial::getLote)
				.filter(l -> l != null && !l.isEmpty()).distinct().collect(Collectors.toList()));

		model.addAttribute("barriles", inventarios.stream().map(inv -> String.valueOf(inv.getNumBarril())).distinct()
				.collect(Collectors.toList()));

		return "Inventario/inventarioInicial";
	}

	@GetMapping("/exportarExcel")
	public void exportarExcel(@RequestParam(required = false) List<String> cerveza,
			@RequestParam(required = false) String tipo, @RequestParam(required = false) List<String> lote,
			@RequestParam(required = false) List<String> numBarril, @RequestParam(required = false) String fechaInicio,
			@RequestParam(required = false) String fechaFin, HttpServletResponse response) throws IOException {

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=inventario_inicial_filtrado.xlsx");

		// 🔥 APLICAR LOS MISMOS FILTROS QUE EN /filtrar
		List<InventarioInicial> lista = inventarioService.findAll().stream()

				// 🍺 CERVEZA
				.filter(inv -> cerveza == null || cerveza.isEmpty() || cerveza.contains(inv.getCerveza().getNombre()))

				// 🧪 TIPO
				.filter(inv -> tipo == null || tipo.isEmpty() || inv.getCerveza().getTipo().equalsIgnoreCase(tipo))

				// 🧾 LOTE
				.filter(inv -> lote == null || lote.isEmpty() || lote.contains(inv.getLote()))

				// 🛢️ BARRIL
				.filter(inv -> numBarril == null || numBarril.isEmpty()
						|| numBarril.contains(String.valueOf(inv.getNumBarril())))

				// 📅 FECHAS
				.filter(inv -> {
					if ((fechaInicio == null || fechaInicio.isEmpty()) && (fechaFin == null || fechaFin.isEmpty())) {
						return true;
					}

					String fecha = inv.getFechaRegistro();
					if (fecha == null || fecha.isEmpty())
						return false;

					boolean desde = fechaInicio == null || fechaInicio.isEmpty() || fecha.compareTo(fechaInicio) >= 0;

					boolean hasta = fechaFin == null || fechaFin.isEmpty() || fecha.compareTo(fechaFin) <= 0;

					return desde && hasta;
				})

				.collect(Collectors.toList());

		// ================== EXCEL ==================

		try (Workbook workbook = new XSSFWorkbook()) {

			Sheet sheet = workbook.createSheet("Inventario Inicial Bruder");

			// 🎨 COLORES
			XSSFColor azulOscuro = new XSSFColor(new java.awt.Color(30, 60, 120), new DefaultIndexedColorMap());
			XSSFColor dorado = new XSSFColor(new java.awt.Color(255, 215, 100), new DefaultIndexedColorMap());
			XSSFColor grisFila = new XSSFColor(new java.awt.Color(245, 245, 245), new DefaultIndexedColorMap());

			// 🎨 TÍTULO
			CellStyle titleStyle = workbook.createCellStyle();
			XSSFFont titleFont = ((XSSFWorkbook) workbook).createFont();
			titleFont.setBold(true);
			titleFont.setFontHeight(16);
			titleStyle.setFont(titleFont);
			titleStyle.setAlignment(HorizontalAlignment.CENTER);
			((XSSFCellStyle) titleStyle).setFillForegroundColor(dorado);
			titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			Row titleRow = sheet.createRow(0);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellValue("📦 Reporte de Inventario Inicial - Cervecería Bruder");
			titleCell.setCellStyle(titleStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

			// 🎨 ENCABEZADOS
			CellStyle headerStyle = workbook.createCellStyle();
			XSSFFont headerFont = ((XSSFWorkbook) workbook).createFont();
			headerFont.setBold(true);
			headerFont.setColor(org.apache.poi.ss.usermodel.IndexedColors.WHITE.getIndex());
			headerStyle.setFont(headerFont);
			((XSSFCellStyle) headerStyle).setFillForegroundColor(azulOscuro);
			headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			headerStyle.setAlignment(HorizontalAlignment.CENTER);
			headerStyle.setBorderBottom(BorderStyle.THIN);
			headerStyle.setBorderTop(BorderStyle.THIN);
			headerStyle.setBorderLeft(BorderStyle.THIN);
			headerStyle.setBorderRight(BorderStyle.THIN);

			String[] columnas = { "ID", "Cerveza", "Presentación", "Cantidad", "Lote", "N° Barril", "Fecha Registro",
					"Total" };

			Row headerRow = sheet.createRow(2);
			for (int i = 0; i < columnas.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columnas[i]);
				cell.setCellStyle(headerStyle);
			}

			// 🎨 CUERPO
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

			for (InventarioInicial inv : lista) {

				CellStyle estilo = (rowNum % 2 == 0) ? bodyAlt : bodyStyle;
				Row row = sheet.createRow(rowNum++);
				int col = 0;

				row.createCell(col).setCellValue(inv.getId());
				row.getCell(col++).setCellStyle(estilo);

				row.createCell(col).setCellValue(inv.getCerveza().getNombre());
				row.getCell(col++).setCellStyle(estilo);

				row.createCell(col).setCellValue(inv.getCerveza().getTipo());
				row.getCell(col++).setCellStyle(estilo);

				row.createCell(col).setCellValue(inv.getCantidad());
				row.getCell(col++).setCellStyle(estilo);

				row.createCell(col).setCellValue(inv.getLote());
				row.getCell(col++).setCellStyle(estilo);

				row.createCell(col).setCellValue(inv.getNumBarril());
				row.getCell(col++).setCellStyle(estilo);

				row.createCell(col).setCellValue(inv.getFechaRegistro());
				row.getCell(col++).setCellStyle(estilo);

				row.createCell(col).setCellValue(inv.getCantidad());
				row.getCell(col).setCellStyle(estilo);
			}

			for (int i = 0; i < columnas.length; i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(response.getOutputStream());
		}
	}

}
