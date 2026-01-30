package com.bruder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bruder.model.PuntosVenta;
import java.util.List;

@Repository
public interface IPuntosVentaRepository extends JpaRepository<PuntosVenta, Integer> {

	// Buscar por nombre
	List<PuntosVenta> findByNombreContainingIgnoreCase(String nombre);

	// Buscar por dirección
	List<PuntosVenta> findByDireccionContainingIgnoreCase(String direccion);

	List<PuntosVenta> findByActivoTrue();

}
