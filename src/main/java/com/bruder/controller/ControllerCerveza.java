package com.bruder.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bruder.model.Cerveza;
import com.bruder.service.ICervezaService;

@Controller
@RequestMapping("/cerveza")
public class ControllerCerveza {

	@Autowired
	private ICervezaService cervezaService;

	// Listar todas las cervezas
	@GetMapping("/producto")
	public String listar(Model model) {
		List<Cerveza> cervezas = cervezaService.findAllOrderByNombreAsc();
		model.addAttribute("cervezas", cervezas);
		model.addAttribute("cerveza", new Cerveza());
		return "cerveza/producto";
	}

	// Guardar o editar cerveza
	@PostMapping("/guardar")
	public String guardar(@ModelAttribute Cerveza cerveza) {
		if (cerveza.getId() == null) {
			cerveza.setActivo(true);
			cervezaService.save(cerveza);
		} else {
			cervezaService.update(cerveza);
		}
		return "redirect:/cerveza/producto";
	}

	// Editar cerveza
	@GetMapping("/editar/{id}")
	public String editar(@PathVariable Integer id, Model model) {
		Optional<Cerveza> cervezaOpt = cervezaService.findById(id);
		model.addAttribute("cerveza", cervezaOpt.orElse(new Cerveza()));
		model.addAttribute("cervezas", cervezaService.findAllOrderByNombreAsc());
		return "cerveza/producto";
	}

	// Activar / Inactivar
	@GetMapping("/cambiarEstado/{id}")
	public String cambiarEstado(@PathVariable Integer id) {
		Optional<Cerveza> cervezaOpt = cervezaService.findById(id);
		cervezaOpt.ifPresent(c -> {
			c.setActivo(!c.isActivo());

			cervezaService.update(c);
		});
		return "redirect:/cerveza/producto";
	}

	
}
