package com.bruder.controller;

import com.bruder.model.PuntosVenta;
import com.bruder.service.IPuntosVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/puntosVenta")
public class ControllerPuntoVenta {

	@Autowired
	private IPuntosVentaService puntosVentaService;

	// Listar
	@GetMapping
	public String listarPuntosVenta(Model model) {
		List<PuntosVenta> lista = puntosVentaService.findAll();
		model.addAttribute("puntosVenta", lista);
		return "puntosVenta/lista";
	}

	//Formulario
	@GetMapping("/nuevo")
	public String nuevoPuntoVenta(Model model) {
		model.addAttribute("puntoVenta", new PuntosVenta());
		return "puntosVenta/form";
	}

	// Guardar 
	@PostMapping("/guardar")
	public String guardarPuntoVenta(@ModelAttribute("puntoVenta") PuntosVenta puntoVenta) {
		puntosVentaService.save(puntoVenta);
		return "redirect:/puntosVenta";
	}

	// Editar 
	@GetMapping("/editar/{id}")
	public String editarPuntoVenta(@PathVariable("id") Integer id, Model model) {
		PuntosVenta punto = puntosVentaService.findById(id).orElse(null);
		if (punto == null) {
			return "redirect:/puntosVenta";
		}
		model.addAttribute("puntoVenta", punto);
		return "puntosVenta/form";
	}


	// Activar/Inactivar 
	@GetMapping("/toggleActivo/{id}")
	public String toggleActivo(@PathVariable("id") Integer id) {
		PuntosVenta punto = puntosVentaService.findById(id).orElse(null);
		if (punto != null) {
			punto.setActivo(!punto.isActivo());
			puntosVentaService.update(punto);
		}
		return "redirect:/puntosVenta";
	}
}
