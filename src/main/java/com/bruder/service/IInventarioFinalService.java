package com.bruder.service;

import java.util.List;
import java.util.Optional;

import com.bruder.model.InventarioFinal;

public interface IInventarioFinalService {

	InventarioFinal save(InventarioFinal inventarioFinal);

	void update(InventarioFinal inventarioFinal);

	void delete(Integer id);

	Optional<InventarioFinal> findById(Integer id);

	List<InventarioFinal> findAll();

	List<InventarioFinal> findByCervezaId(Integer cervezaId);

	List<InventarioFinal> findByFechaRegistro(String fechaRegistro);

	int obtenerCantidadDisponible(Integer cervezaId, String lote, String numBarril);

	void descontarStock(Integer cervezaId, String lote, String numBarril, int cantidad);

	List<Object[]> obtenerInventarioDisponible();

	void deleteAll();

	InventarioFinal findByCervezaAndLoteAndNumBarril(Integer cervezaId, String lote, String numBarril);

	List<Object[]> obtenerInventarioAgrupado();

	int obtenerTotalInventarioFinal();

	List<String> obtenerCervezas();

	List<String> obtenerLotesBarriles();

}
