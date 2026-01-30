package com.bruder.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bruder.model.InventarioFinal;
import com.bruder.repository.IInventarioFinalRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class InventarioFinalServiceImplements implements IInventarioFinalService {

	@Autowired
	private IInventarioFinalRepository inventarioFinalRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public InventarioFinal save(InventarioFinal inventarioFinal) {
		return inventarioFinalRepository.save(inventarioFinal);
	}

	@Override
	public void update(InventarioFinal inventarioFinal) {
		if (inventarioFinal.getId() != null && inventarioFinalRepository.existsById(inventarioFinal.getId())) {
			inventarioFinalRepository.save(inventarioFinal);
		}
	}

	@Override
	public void delete(Integer id) {
		inventarioFinalRepository.deleteById(id);
	}

	@Override
	public List<InventarioFinal> findAll() {
		entityManager.clear();
		return inventarioFinalRepository.findAll();
	}

	@Override
	public Optional<InventarioFinal> findById(Integer id) {
		entityManager.clear();
		return inventarioFinalRepository.findById(id);
	}

	@Override
	public List<InventarioFinal> findByCervezaId(Integer cervezaId) {
		return inventarioFinalRepository.findByCervezaId(cervezaId);
	}

	@Override
	public List<InventarioFinal> findByFechaRegistro(String fechaRegistro) {
		return inventarioFinalRepository.findByFechaRegistro(fechaRegistro);
	}

	// ✅ Obtener cantidad disponible
	@Override
	public int obtenerCantidadDisponible(Integer cervezaId, String lote, String numBarril) {
		int total = 0;
		InventarioFinal inv = null;

		if (lote != null && !lote.isEmpty()) {
			inv = inventarioFinalRepository.findByCervezaIdAndLote(cervezaId, lote);
		} else if (numBarril != null && !numBarril.isEmpty()) {
			inv = inventarioFinalRepository.findByCervezaIdAndNumBarril(cervezaId, numBarril);
		}

		if (inv != null && inv.getCantidad() != null && !inv.getCantidad().isEmpty()) {
			try {
				total = Integer.parseInt(inv.getCantidad());
			} catch (NumberFormatException e) {
				total = 0;
			}
		}

		return total;
	}

	// ✅ Descontar stock (con cantidad tipo String)
	@Override
	public void descontarStock(Integer cervezaId, String lote, String numBarril, int cantidad) {
		InventarioFinal inv = null;

		if (lote != null && !lote.isEmpty()) {
			inv = inventarioFinalRepository.findByCervezaIdAndLote(cervezaId, lote);
		} else if (numBarril != null && !numBarril.isEmpty()) {
			inv = inventarioFinalRepository.findByCervezaIdAndNumBarril(cervezaId, numBarril);
		}

		if (inv != null) {
			int cantidadActual = 0;
			try {
				cantidadActual = (inv.getCantidad() != null && !inv.getCantidad().isEmpty())
						? Integer.parseInt(inv.getCantidad())
						: 0;
			} catch (NumberFormatException e) {
				cantidadActual = 0;
			}

			int nuevaCantidad = cantidadActual - cantidad;
			if (nuevaCantidad < 0)
				nuevaCantidad = 0;

			inv.setCantidad(String.valueOf(nuevaCantidad));
			inventarioFinalRepository.save(inv);
		}
	}

	@Override
	public List<Object[]> obtenerInventarioDisponible() {
		List<Object[]> datos = inventarioFinalRepository.obtenerInventarioDisponible();
		System.out.println("✅ Inventario Final cargado: " + datos.size() + " registros encontrados.");
		for (Object[] fila : datos) {
			System.out.println("   🟢 " + fila[1] + " - " + fila[2] + " | Lote: " + fila[3] + " | Barril: " + fila[4]
					+ " | Cantidad: " + fila[5]);
		}
		return datos;
	}

	@Override
	public void deleteAll() {
		inventarioFinalRepository.deleteAll();
	}

	@Override
	public InventarioFinal findByCervezaAndLoteAndNumBarril(Integer cervezaId, String lote, String numBarril) {
		return inventarioFinalRepository.findByCervezaIdAndLoteAndNumBarril(cervezaId, lote, numBarril);
	}

	@Override
	public List<Object[]> obtenerInventarioAgrupado() {
		return inventarioFinalRepository.obtenerInventarioAgrupado();
	}

	@Override
	public int obtenerTotalInventarioFinal() {
		return inventarioFinalRepository.obtenerTotalInventarioFinal();
	}

	@Override
	public List<String> obtenerCervezas() {
		return inventarioFinalRepository.findAll().stream().map(i -> i.getCerveza().getNombre()).distinct().sorted()
				.toList();
	}

	@Override
	public List<String> obtenerLotesBarriles() {
		return inventarioFinalRepository.findAll().stream()
				.map(i -> i.getCerveza().getTipo().equals("Barril") ? i.getNumBarril() : i.getLote())
				.filter(l -> l != null && !l.isBlank()).distinct().sorted().toList();
	}

}
