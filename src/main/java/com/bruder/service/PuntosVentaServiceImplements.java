package com.bruder.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bruder.model.PuntosVenta;
import com.bruder.repository.IPuntosVentaRepository;

@Service
public class PuntosVentaServiceImplements implements IPuntosVentaService {

	@Autowired
	private IPuntosVentaRepository puntosVentaRepository;

	@Override
	public PuntosVenta save(PuntosVenta puntoVenta) {
		return puntosVentaRepository.save(puntoVenta);
	}

	@Override
	public void update(PuntosVenta puntoVenta) {
		if (puntoVenta.getId() != null && puntosVentaRepository.existsById(puntoVenta.getId())) {
			puntosVentaRepository.save(puntoVenta);
		}
	}

	@Override
	public void delete(Integer id) {
		puntosVentaRepository.deleteById(id);
	}

	@Override
	public Optional<PuntosVenta> findById(Integer id) {
		return puntosVentaRepository.findById(id);
	}

	@Override
	public List<PuntosVenta> findAll() {
		return puntosVentaRepository.findAll();
	}

	@Override
	public List<PuntosVenta> findByNombre(String nombre) {
		return puntosVentaRepository.findByNombreContainingIgnoreCase(nombre);
	}

	@Override
	public List<PuntosVenta> findByDireccion(String direccion) {
		return puntosVentaRepository.findByDireccionContainingIgnoreCase(direccion);
	}

	@Override
	public List<PuntosVenta> listarTodos() {
		return puntosVentaRepository.findAll();
	}

	@Override
	public List<PuntosVenta> findByActivoTrue() {
		return puntosVentaRepository.findByActivoTrue();
	}

	@Override
	public List<String> listarNombres() {
		return puntosVentaRepository.findAll().stream().map(PuntosVenta::getNombre).distinct().sorted()
				.collect(Collectors.toList());
	}
}
