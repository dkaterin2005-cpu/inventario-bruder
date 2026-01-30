package com.bruder.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.bruder.model.Usuario;
import com.bruder.service.IUsuarioService;

@Controller
@RequestMapping("/")
public class ControllerUsuarios {

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("")
	public String root() {
		return "redirect:/login";
	}

	// LOGIN
	@GetMapping("/login")
	public String mostrarLogin() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
			return "redirect:/inicio";
		}

		return "Usuarios/login";
	}

	// INICIO (PROTEGIDO)
	@GetMapping("/inicio")
	public String mostrarInicio(Model model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		String correo = auth.getName(); // 👈 correo autenticado

		Usuario usuario = usuarioService.findByCorreo(correo).orElse(null);

		if (usuario == null) {
			return "redirect:/login";
		}

		model.addAttribute("nombreUsuario", usuario.getNombre());
		model.addAttribute("tipoUsuario", usuario.getTipo());

		return "Usuarios/inicio";
	}

	// =========================
	// REGISTRO
	// =========================
	@GetMapping("/registro")
	public String mostrarRegistro(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "Usuarios/registro";
	}

	@PostMapping("/registro")
	public String procesarRegistro(@ModelAttribute Usuario usuario, Model model) {

		// Validación contraseña
		if (!usuario.getPassword().matches("\\d{5,10}")) {
			model.addAttribute("error", "La contraseña debe tener solo números y entre 5 y 10 dígitos ❌");
			return "Usuarios/registro";
		}

		Optional<Usuario> existente = usuarioService.findByCorreo(usuario.getCorreo());

		if (existente.isPresent()) {
			model.addAttribute("error", "El correo ya está registrado ❗");
			return "Usuarios/registro";
		}

		// cifrar
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

		usuarioService.save(usuario);

		return "redirect:/login";
	}

	@GetMapping("/acceso-denegado")
	public String accesoDenegado() {
		return "errores/403";
	}

}
