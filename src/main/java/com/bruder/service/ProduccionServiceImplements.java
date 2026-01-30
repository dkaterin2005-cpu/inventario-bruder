package com.bruder.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bruder.model.Produccion;
import com.bruder.repository.IProduccionRepository;

@Service
public class ProduccionServiceImplements implements IProduccionService {

	@Autowired
	private IProduccionRepository produccionRepository;

	@Override
	public Produccion save(Produccion produccion) {
		return produccionRepository.save(produccion);
	}

	@Override
	public void update(Produccion produccion) {
		if (produccion.getId() != null && produccionRepository.existsById(produccion.getId())) {
			produccionRepository.save(produccion);
		}
	}

	@Override
	public void delete(Integer id) {
		produccionRepository.deleteById(id);
	}

	@Override
	public Optional<Produccion> findById(Integer id) {
		return produccionRepository.findById(id);
	}

	@Override
	public List<Produccion> findAll() {
		return produccionRepository.findAll();
	}

	@Override
	public List<Produccion> findByFechaProduccion(String fechaProduccion) {
		return produccionRepository.findByFechaProduccion(fechaProduccion);
	}

	@Override
	public List<Produccion> findByCerveza(Integer cervezaId) {
		return produccionRepository.findByCerveza_Id(cervezaId);
	}

	@Override
	public List<Produccion> findByFechaVencimiento(String fechaVencimiento) {
		return produccionRepository.findByFechaVencimiento(fechaVencimiento);
	}

	@Override
	public Produccion findUltimaPorCerveza(Integer cervezaId) {
		return produccionRepository.findTopByCerveza_IdOrderByIdDesc(cervezaId);
	}

	@Override
	public List<Produccion> listarTodos() {
		return produccionRepository.findAll();
	}

	@Override
	public int obtenerTotalProduccion() {
		return produccionRepository.obtenerTotalProduccion();
	}

	@Override
	public List<Produccion> filtrar(List<String> cerveza, List<String> lote, List<String> barril, String fechaInicio,
			String fechaFin) {

		return produccionRepository.findAll().stream()

				// 🍺 CERVEZA
				.filter(p -> cerveza == null || cerveza.isEmpty() || cerveza.contains(p.getCerveza().getNombre()))

				// 📦 LOTE
				.filter(p -> lote == null || lote.isEmpty() || lote.contains(p.getLote()))

				// 🛢️ BARRIL
				.filter(p -> barril == null || barril.isEmpty() || barril.contains(p.getNumBarril()))

				// 📅 FECHA INICIO (STRING)
				.filter(p -> fechaInicio == null || fechaInicio.isEmpty()
						|| p.getFechaProduccion().compareTo(fechaInicio) >= 0)

				// 📅 FECHA FIN (STRING)
				.filter(p -> fechaFin == null || fechaFin.isEmpty() || p.getFechaProduccion().compareTo(fechaFin) <= 0)

				.collect(Collectors.toList());
	}
}
