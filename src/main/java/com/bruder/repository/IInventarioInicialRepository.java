package com.bruder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.bruder.model.InventarioInicial;
import java.util.List;

@Repository
public interface IInventarioInicialRepository extends JpaRepository<InventarioInicial, Integer> {

	// Buscar por cerveza
	List<InventarioInicial> findByCervezaId(Integer cervezaId);

	// Buscar por fecha de registro
	List<InventarioInicial> findByFechaRegistro(String fechaRegistro);

	@Query("SELECT COALESCE(SUM(i.cantidad), 0) FROM InventarioInicial i")
	int obtenerTotalInventarioInicial();

	InventarioInicial findByCervezaIdAndLoteAndNumBarril(Integer cervezaId, String lote, String numBarril);


}
