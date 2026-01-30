package com.bruder.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.bruder.model.Usuario;
import com.bruder.service.IUsuarioService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private IUsuarioService usuarioService;

	@Override
	public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

		Usuario usuario = usuarioService.findByCorreo(correo)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		//NORMALIZAR ROL 
		String rol = usuario.getTipo().toUpperCase().replace(" ", "_");

		return new User(usuario.getCorreo(), usuario.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_" + rol)));
	}
}
