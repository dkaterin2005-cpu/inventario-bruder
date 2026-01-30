package com.bruder.service;

import java.util.List;
import java.util.Optional;
import com.bruder.model.InventarioInicial;

public interface IInventarioInicialService {

	InventarioInicial save(InventarioInicial inventarioInicial);

	void update(InventarioInicial inventarioInicial);

	void delete(Integer id);

	Optional<InventarioInicial> findById(Integer id);

	List<InventarioInicial> findAll();

	List<InventarioInicial> findByCervezaId(Integer cervezaId);

	List<InventarioInicial> findByFechaRegistro(String fechaRegistro);

	int obtenerTotalInventarioInicial();

}
