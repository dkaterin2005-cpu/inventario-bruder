package com.bruder.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bruder.model.Despachos;
import com.bruder.model.InventarioFinal;
import com.bruder.repository.IDespachosRepository;
import com.bruder.repository.IInventarioFinalRepository;

@Service
public class DespachosServiceImplement implements IDespachosService {

	@Autowired
	private IDespachosRepository despachosRepository;

	@Autowired
	private IInventarioFinalRepository inventarioFinalRepository;

	@Override
	public Despachos save(Despachos despacho) {
		return despachosRepository.save(despacho);
	}

	@Override
	public void update(Despachos despacho) {
		despachosRepository.save(despacho);
	}

	@Override
	public void delete(Integer id) {
		despachosRepository.deleteById(id);
	}

	@Override
	public Optional<Despachos> findById(Integer id) {
		return despachosRepository.findById(id);
	}

	@Override
	public List<Despachos> findAll() {
		return despachosRepository.findAll();
	}

	@Override
	public List<Despachos> findByFechaDespacho(String fecha) {
		return despachosRepository.findByFechaDespacho(fecha);
	}

	@Override
	public List<Despachos> findByRangoFechas(String inicio, String fin) {
		return despachosRepository.findByRangoFechas(inicio, fin);
	}

	@Override
	public List<Despachos> findByNombreCerveza(String nombreCerveza) {
		return despachosRepository.findByNombreCerveza(nombreCerveza);
	}

	@Override
	public List<Despachos> findByUsuarioId(Integer usuarioId) {
		return despachosRepository.findByUsuarioId(usuarioId);
	}

	@Override
	public List<Despachos> findByPuntoVenta(Integer puntoVentaId) {
		return despachosRepository.findByPuntoVenta(puntoVentaId);
	}

	@Override
	public List<Despachos> findAllOrderByFechaRegistroDesc() {
		return despachosRepository.findAllOrderByFechaRegistroDesc();
	}

	@Override
	public List<Despachos> listarTodos() {
		return despachosRepository.findAll();
	}

	@Override
	public void deleteById(Integer id) {
		despachosRepository.deleteById(id);
	}

	@Override
	public void descontarStock(Despachos d) {
		InventarioFinal inv = inventarioFinalRepository.findByCervezaIdAndLoteAndNumBarril(d.getCerveza().getId(),
				d.getLote(), d.getNumBarril());

		if (inv != null) {
			int actual = Integer.parseInt(inv.getCantidad());
			int cantidadDespacho = Integer.parseInt(d.getCantidad());

			int nueva = actual - cantidadDespacho;
			if (nueva < 0)
				nueva = 0;

			inv.setCantidad(String.valueOf(nueva));
			inventarioFinalRepository.save(inv);
		}
	}

	@Override
	public void revertirStock(Despachos d) {
		InventarioFinal inv = inventarioFinalRepository.findByCervezaIdAndLoteAndNumBarril(d.getCerveza().getId(),
				d.getLote(), d.getNumBarril());

		if (inv != null) {
			int actual = Integer.parseInt(inv.getCantidad());
			int cantidadDespacho = Integer.parseInt(d.getCantidad());

			int nueva = actual + cantidadDespacho;

			inv.setCantidad(String.valueOf(nueva));
			inventarioFinalRepository.save(inv);
		}
	}

	@Override
	public List<Despachos> filtrar(List<String> cerveza, List<String> tipo, List<String> loteBarril,
			List<String> puntoVenta, String fechaInicio, String fechaFin) {

		return findAllOrderByFechaRegistroDesc().stream()

				// 🍺 CERVEZA
				.filter(d -> cerveza == null || cerveza.isEmpty()
						|| cerveza.contains(d.getInventarioFinal().getCerveza().getNombre()))

				// 🍻 TIPO
				.filter(d -> tipo == null || tipo.isEmpty()
						|| tipo.contains(d.getInventarioFinal().getCerveza().getTipo()))

				// 🏷 LOTE / BARRIL
				.filter(d -> loteBarril == null || loteBarril.isEmpty()
						|| loteBarril.contains(d.getInventarioFinal().getCerveza().getTipo().equals("Barril")
								? d.getInventarioFinal().getNumBarril()
								: d.getInventarioFinal().getLote()))

				// 🏪 PUNTO DE VENTA (si no lo usas, déjalo pasar)
				.filter(d -> puntoVenta == null || puntoVenta.isEmpty()
						|| (d.getPuntoVenta() != null && puntoVenta.contains(d.getPuntoVenta().getNombre())))

				// 📅 FECHAS
				.filter(d -> fechaInicio == null || fechaInicio.isBlank()
						|| d.getFechaDespacho().compareTo(fechaInicio) >= 0)

				.filter(d -> fechaFin == null || fechaFin.isBlank() || d.getFechaDespacho().compareTo(fechaFin) <= 0)

				.collect(Collectors.toList());
	}

	@Override
	public List<String> obtenerCervezas() {
		return despachosRepository.findAll().stream().map(d -> d.getInventarioFinal().getCerveza().getNombre())
				.distinct().collect(Collectors.toList());
	}

	@Override
	public List<String> obtenerTipos() {
		return despachosRepository.findAll().stream().map(d -> d.getInventarioFinal().getCerveza().getTipo()).distinct()
				.collect(Collectors.toList());
	}

	@Override
	public List<String> obtenerLotesBarriles() {
		return despachosRepository.findAll().stream()
				.map(d -> d.getInventarioFinal().getCerveza().getTipo().equals("Barril")
						? d.getInventarioFinal().getNumBarril()
						: d.getInventarioFinal().getLote())
				.filter(l -> l != null && !l.isBlank()).distinct().collect(Collectors.toList());
	}

}
