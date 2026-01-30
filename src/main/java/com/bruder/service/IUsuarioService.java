package com.bruder.service;

import java.util.List;
import java.util.Optional;
import com.bruder.model.Usuario;

public interface IUsuarioService {

	Usuario save(Usuario usuario);

	void update(Usuario usuario);

	void delete(Integer id);

	Optional<Usuario> findById(Integer id);

	List<Usuario> findAll();

	Optional<Usuario> findByCorreo(String correo);

	List<Usuario> findByNombre(String nombre);

	List<Usuario> findByTipo(String tipo);
}
