package com.bruder.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bruder.model.InventarioFinal;
import com.bruder.model.InventarioInicial;
import com.bruder.repository.IInventarioFinalRepository;
import com.bruder.repository.IInventarioInicialRepository;

@Service
public class InventarioInicialServiceImplements implements IInventarioInicialService {

	@Autowired
	private IInventarioInicialRepository inventarioInicialRepository;
	@Autowired
	private IInventarioFinalRepository inventarioFinalRepository;

	@Override
	public InventarioInicial save(InventarioInicial ini) {

		// 1. Guardar inventario inicial
		InventarioInicial guardado = inventarioInicialRepository.save(ini);

		// 2. Crear inventario final real
		InventarioFinal inv = new InventarioFinal();

		inv.setCerveza(guardado.getCerveza());
		inv.setCantidad(guardado.getCantidad()); // String ✔
		inv.setLote(guardado.getLote());
		inv.setNumBarril(guardado.getNumBarril());
		inv.setInventarioInicial(guardado);
		inv.setProduccion(null);

		inventarioFinalRepository.save(inv);

		return guardado;
	}

	@Override
	public void update(InventarioInicial inventarioInicial) {
		if (inventarioInicial.getId() != null && inventarioInicialRepository.existsById(inventarioInicial.getId())) {
			inventarioInicialRepository.save(inventarioInicial);
		}
	}

	@Override
	public void delete(Integer id) {
		inventarioInicialRepository.deleteById(id);
	}

	@Override
	public Optional<InventarioInicial> findById(Integer id) {
		return inventarioInicialRepository.findById(id);
	}

	@Override
	public List<InventarioInicial> findAll() {
		return inventarioInicialRepository.findAll();
	}

	@Override
	public List<InventarioInicial> findByCervezaId(Integer cervezaId) {
		return inventarioInicialRepository.findByCervezaId(cervezaId);
	}

	@Override
	public List<InventarioInicial> findByFechaRegistro(String fechaRegistro) {
		return inventarioInicialRepository.findByFechaRegistro(fechaRegistro);
	}

	@Override
	public int obtenerTotalInventarioInicial() {
		return inventarioInicialRepository.obtenerTotalInventarioInicial();
	}

}
