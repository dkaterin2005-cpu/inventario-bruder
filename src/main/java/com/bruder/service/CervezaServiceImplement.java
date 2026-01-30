package com.bruder.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bruder.model.Cerveza;
import com.bruder.repository.ICervezaRepository;
import com.bruder.repository.IInventarioFinalRepository;

@Service
public class CervezaServiceImplement implements ICervezaService {

	@Autowired
	private ICervezaRepository cervezaRepository;
	
	@Autowired
	private IInventarioFinalRepository inventarioFinalRepository;
	
	@Override
	public Cerveza save(Cerveza cerveza) {
		return cervezaRepository.save(cerveza);
	}

	@Override
	public void update(Cerveza cerveza) {
		cervezaRepository.save(cerveza);
	}

	@Override
	public void delete(Integer id) {
		cervezaRepository.deleteById(id);
	}

	@Override
	public Optional<Cerveza> findById(Integer id) {
		return cervezaRepository.findById(id);
	}

	@Override
	public List<Cerveza> findAll() {
		return cervezaRepository.findAll();
	}

	@Override
	public Cerveza findByNombre(String nombre) {
		return cervezaRepository.findByNombre(nombre);
	}

	@Override
	public List<Cerveza> findByNombreParcial(String nombre) {
		return cervezaRepository.findByNombreParcial(nombre);
	}

	@Override
	public List<Cerveza> findByTipo(String tipo) {
		return cervezaRepository.findByTipo(tipo);
	}

	@Override
	public List<Cerveza> findActivas() {
		return cervezaRepository.findActivas();
	}

	@Override
	public List<Cerveza> findInactivas() {
		return cervezaRepository.findInactivas();
	}

	@Override
	public List<Cerveza> findAllOrderByNombreAsc() {
		return cervezaRepository.findAllOrderByNombreAsc();
	}

	@Override
	public List<Cerveza> findByActivoTrue() {
		return cervezaRepository.findByActivoTrue();
	}

	@Override
	public List<Cerveza> listarTodos() {
		return cervezaRepository.findAll();
	}

	@Override
	public List<String> obtenerTipos() {
		return inventarioFinalRepository.findAll().stream().map(i -> i.getCerveza().getTipo()).distinct().sorted()
				.toList();
	}

}
