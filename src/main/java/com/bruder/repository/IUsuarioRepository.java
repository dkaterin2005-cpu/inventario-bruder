package com.bruder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bruder.model.Usuario;
import java.util.Optional;
import java.util.List;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

	// Buscar por correo
	Optional<Usuario> findByCorreo(String correo);

	// Buscar por nombre
	List<Usuario> findByNombreContainingIgnoreCase(String nombre);

	// Buscar por tipo de usuario (ej: "Administrador", "Empleado")
	List<Usuario> findByTipo(String tipo);
}
