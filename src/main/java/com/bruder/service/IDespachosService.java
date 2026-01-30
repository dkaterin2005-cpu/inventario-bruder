package com.bruder.service;

import java.util.List;
import java.util.Optional;

import com.bruder.model.Despachos;

public interface IDespachosService {

	Despachos save(Despachos despacho);

	void update(Despachos despacho);

	void delete(Integer id);

	Optional<Despachos> findById(Integer id);

	List<Despachos> findAll();

	List<Despachos> findByFechaDespacho(String fecha);

	List<Despachos> findByRangoFechas(String inicio, String fin);

	List<Despachos> findByNombreCerveza(String nombreCerveza);

	List<Despachos> findByUsuarioId(Integer usuarioId);

	List<Despachos> findByPuntoVenta(Integer puntoVentaId);

	List<Despachos> findAllOrderByFechaRegistroDesc();

	List<Despachos> listarTodos();

	void deleteById(Integer id);

	void descontarStock(Despachos despacho);

	void revertirStock(Despachos despacho);

	List<String> obtenerCervezas();

	List<String> obtenerTipos();

	List<String> obtenerLotesBarriles();


	List<Despachos> filtrar(List<String> cerveza, List<String> tipo, List<String> loteBarril, List<String> puntoVenta,
			String fechaInicio, String fechaFin);

}
