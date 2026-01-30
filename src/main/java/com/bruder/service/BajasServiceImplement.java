package com.bruder.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bruder.model.Bajas;
import com.bruder.model.InventarioFinal;
import com.bruder.repository.IBajasRepository;
import com.bruder.repository.IInventarioFinalRepository;

@Service
public class BajasServiceImplement implements IBajasService {

	@Autowired
	private IBajasRepository bajasRepository;

	@Autowired
	private IInventarioFinalRepository inventarioFinalRepository;

	@Override
	public Bajas save(Bajas bajas) {
		return bajasRepository.save(bajas);
	}

	@Override
	public void update(Bajas bajas) {
		bajasRepository.save(bajas);
	}

	@Override
	public void delete(Integer id) {
		bajasRepository.deleteById(id);
	}

	@Override
	public Optional<Bajas> findById(Integer id) {
		return bajasRepository.findById(id);
	}

	@Override
	public List<Bajas> findAll() {
		return bajasRepository.findAll();
	}

	@Override
	public void deleteById(Integer id) {
		bajasRepository.deleteById(id);
	}

	@Override
	public int totalBajas() {
		return (int) bajasRepository.count();
	}

	@Override
	public List<Bajas> buscarPorFiltros(Integer cervezaId, String desde, String hasta) {
		return bajasRepository.buscarPorFiltros(cervezaId, desde, hasta);
	}

	// 🔥 NUEVO MÉTODO — DESCONTAR STOCK con inventarioFinal incluido en la Baja
	@Override
	public void descontarStock(Bajas baja) {

		InventarioFinal inv = baja.getInventarioFinal();

		if (inv != null) {
			int actual = Integer.parseInt(inv.getCantidad());
			int quitar = Integer.parseInt(baja.getCantidad());

			int nueva = actual - quitar;
			if (nueva < 0)
				nueva = 0;

			inv.setCantidad(String.valueOf(nueva));
			inventarioFinalRepository.save(inv);
		}
	}

	// 🔥 NUEVO MÉTODO — DEVOLVER STOCK cuando se edita o elimina una baja
	@Override
	public void revertirStock(Bajas baja) {

		InventarioFinal inv = baja.getInventarioFinal();

		if (inv != null) {
			int actual = Integer.parseInt(inv.getCantidad());
			int devolver = Integer.parseInt(baja.getCantidad());

			int nueva = actual + devolver;

			inv.setCantidad(String.valueOf(nueva));
			inventarioFinalRepository.save(inv);
		}
	}

	public void recalcularStock(InventarioFinal inv) {

		// 🔥 stock inicial REAL
		int stockInicial = Integer.parseInt(inv.getInventarioInicial().getCantidad());

		// 🔥 total de bajas hechas sobre este inventario
		int totalBajas = bajasRepository.totalBajasPorInventario(inv.getId());

		int stockActual = stockInicial - totalBajas;
		if (stockActual < 0)
			stockActual = 0;

		inv.setCantidad(String.valueOf(stockActual));
		inventarioFinalRepository.save(inv);
	}

}
