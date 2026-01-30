package com.bruder.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bruder.model.Usuario;
import com.bruder.repository.IUsuarioRepository;

@Service
public class UsuarioServiceImplements implements IUsuarioService {

	@Autowired
	private IUsuarioRepository usuarioRepository;

	@Override
	public Usuario save(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	@Override
	public void update(Usuario usuario) {
		if (usuario.getId() != null && usuarioRepository.existsById(usuario.getId())) {
			usuarioRepository.save(usuario);
		}
	}

	@Override
	public void delete(Integer id) {
		usuarioRepository.deleteById(id);
	}

	@Override
	public Optional<Usuario> findById(Integer id) {
		return usuarioRepository.findById(id);
	}

	@Override
	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}

	@Override
	public Optional<Usuario> findByCorreo(String correo) {
		return usuarioRepository.findByCorreo(correo);
	}

	@Override
	public List<Usuario> findByNombre(String nombre) {
		return usuarioRepository.findByNombreContainingIgnoreCase(nombre);
	}

	@Override
	public List<Usuario> findByTipo(String tipo) {
		return usuarioRepository.findByTipo(tipo);
	}
}
