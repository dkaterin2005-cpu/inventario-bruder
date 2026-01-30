package com.bruder.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bruder.model.Movimiento;
import com.bruder.repository.IMovimientoRepository;

@Service
public class MovimientoServiceImplements implements IMovimientoService {

	@Override
	public Movimiento save(Movimiento movimiento) {
		return movimientoRepository.save(movimiento);
	}

	@Override
	public void update(Movimiento movimiento) {
		if (movimiento.getId() != null && movimientoRepository.existsById(movimiento.getId())) {
			movimientoRepository.save(movimiento);
		}
	}

	@Override
	public void delete(Integer id) {
		movimientoRepository.deleteById(id);
	}

	@Override
	public Optional<Movimiento> findById(Integer id) {
		return movimientoRepository.findById(id);
	}

	@Override
	public List<Movimiento> findAll() {
		return movimientoRepository.findAll();
	}

	@Override
	public List<Movimiento> findByCervezaId(Integer cervezaId) {
		return movimientoRepository.findByCervezaId(cervezaId);
	}

	@Override
	public List<Movimiento> findByUsuarioId(Integer usuarioId) {
		return movimientoRepository.findByUsuarioId(usuarioId);
	}

	@Override
	public List<Movimiento> findByFechaMovimiento(String fechaMovimiento) {
		return movimientoRepository.findByFechaMovimiento(fechaMovimiento);
	}

	@Override
	public List<Movimiento> findByProduccionId(Integer produccionId) {
		return movimientoRepository.findByProduccionId(produccionId);
	}

	@Override
	public List<Movimiento> findByDespachoId(Integer despachoId) {
		return movimientoRepository.findByDespachoId(despachoId);
	}

	@Override
	public List<Movimiento> findByBajaId(Integer bajaId) {
		return movimientoRepository.findByBajaId(bajaId);
	}

	@Autowired
	private IMovimientoRepository movimientoRepository;

	@Override
	public List<Movimiento> listarTodos() {
		return movimientoRepository.findAll();
	}

	@Override
	public void guardar(Movimiento movimiento) {
		movimientoRepository.save(movimiento);
	}

	@Override
	public void eliminar(Integer id) {
		movimientoRepository.deleteById(id);
	}

	@Override
	public Movimiento buscarPorId(Integer id) {
		return movimientoRepository.findById(id).orElse(null);
	}

}
