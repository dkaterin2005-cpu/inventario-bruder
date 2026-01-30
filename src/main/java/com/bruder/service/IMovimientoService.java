package com.bruder.service;

import java.util.List;
import java.util.Optional;

import com.bruder.model.Movimiento;

public interface IMovimientoService {

	Movimiento save(Movimiento movimiento);

	void update(Movimiento movimiento);

	void delete(Integer id);

	Optional<Movimiento> findById(Integer id);

	List<Movimiento> findAll();

	List<Movimiento> findByCervezaId(Integer cervezaId);

	List<Movimiento> findByUsuarioId(Integer usuarioId);

	List<Movimiento> findByFechaMovimiento(String fechaMovimiento);

	List<Movimiento> findByProduccionId(Integer produccionId);

	List<Movimiento> findByDespachoId(Integer despachoId);

	List<Movimiento> findByBajaId(Integer bajaId);

	List<Movimiento> listarTodos();

	void guardar(Movimiento movimiento);

	void eliminar(Integer id);

	Movimiento buscarPorId(Integer id);

}
