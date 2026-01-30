package com.bruder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bruder.model.Movimiento;
import java.util.List;

@Repository
public interface IMovimientoRepository extends JpaRepository<Movimiento, Integer> {

	// Buscar movimientos por cerveza
	List<Movimiento> findByCervezaId(Integer cervezaId);

	// Buscar por usuario
	List<Movimiento> findByUsuarioId(Integer usuarioId);

	// Buscar por fecha de movimiento
	List<Movimiento> findByFechaMovimiento(String fechaMovimiento);

	// Buscar por producción
	List<Movimiento> findByProduccionId(Integer produccionId);

	// Buscar por despacho
	List<Movimiento> findByDespachoId(Integer despachoId);

	// Buscar por baja
	List<Movimiento> findByBajaId(Integer bajaId);

	List<Movimiento> findByCervezaTipo(String tipo);
}
