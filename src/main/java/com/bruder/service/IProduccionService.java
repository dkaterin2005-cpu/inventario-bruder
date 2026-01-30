package com.bruder.service;

import java.util.List;
import java.util.Optional;
import com.bruder.model.Produccion;

public interface IProduccionService {

	Produccion save(Produccion produccion);

	void update(Produccion produccion);

	void delete(Integer id);

	Optional<Produccion> findById(Integer id);

	List<Produccion> findAll();

	List<Produccion> findByFechaProduccion(String fechaProduccion);

	List<Produccion> findByCerveza(Integer cervezaId);

	List<Produccion> findByFechaVencimiento(String fechaVencimiento);

	Produccion findUltimaPorCerveza(Integer idCerveza);

	List<Produccion> listarTodos();

	int obtenerTotalProduccion();

	List<Produccion> filtrar(List<String> cerveza, List<String> lote, List<String> barril, String fechaInicio,
			String fechaFin);
}
