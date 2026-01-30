package com.bruder.service;

import java.util.List;
import java.util.Optional;

import com.bruder.model.PuntosVenta;

public interface IPuntosVentaService {

	PuntosVenta save(PuntosVenta puntoVenta);

	void update(PuntosVenta puntoVenta);

	void delete(Integer id);

	Optional<PuntosVenta> findById(Integer id);

	List<PuntosVenta> findAll();

	List<PuntosVenta> findByNombre(String nombre);

	List<PuntosVenta> findByDireccion(String direccion);

	List<PuntosVenta> listarTodos();

	List<PuntosVenta> findByActivoTrue();

	List<String> listarNombres();

}
