package com.bruder.service;

import java.util.List;
import java.util.Optional;

import com.bruder.model.Cerveza;

public interface ICervezaService {

	Cerveza save(Cerveza cerveza);

	void update(Cerveza cerveza);

	void delete(Integer id);

	Optional<Cerveza> findById(Integer id);

	List<Cerveza> findAll();

	Cerveza findByNombre(String nombre);

	List<Cerveza> findByNombreParcial(String nombre);

	List<Cerveza> findByTipo(String tipo);

	List<Cerveza> findActivas();

	List<Cerveza> findInactivas();

	List<Cerveza> findAllOrderByNombreAsc();

	List<Cerveza> findByActivoTrue();

	List<Cerveza> listarTodos();

	List<String> obtenerTipos();

}
