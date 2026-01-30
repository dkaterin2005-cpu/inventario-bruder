package com.bruder.service;

import java.util.List;
import java.util.Optional;

import com.bruder.model.Bajas;

public interface IBajasService {

	Bajas save(Bajas bajas);

	void update(Bajas bajas);

	void delete(Integer id);

	Optional<Bajas> findById(Integer id);

	List<Bajas> findAll();

	void deleteById(Integer id);

	int totalBajas();

	List<Bajas> buscarPorFiltros(Integer cervezaId, String desde, String hasta);

	// Operaciones de inventario final
	void descontarStock(Bajas baja);

	void revertirStock(Bajas baja);
}
