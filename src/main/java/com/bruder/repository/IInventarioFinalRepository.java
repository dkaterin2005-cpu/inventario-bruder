package com.bruder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bruder.model.InventarioFinal;

@Repository
public interface IInventarioFinalRepository extends JpaRepository<InventarioFinal, Integer> {

	// Buscar por cerveza
	List<InventarioFinal> findByCervezaId(Integer cervezaId);

	// Buscar por fecha de registro
	List<InventarioFinal> findByFechaRegistro(String fechaRegistro);

	// Buscar un registro específico (para validaciones)
	InventarioFinal findByCervezaIdAndNumBarril(Integer cervezaId, String numBarril);

	InventarioFinal findByCervezaIdAndLote(Integer cervezaId, String lote);

	InventarioFinal findByCervezaIdAndLoteAndNumBarril(Integer cervezaId, String lote, String numBarril);

	@Query(value = """
			SELECT
			    i.id,
			    c.nombre,
			    c.tipo,
			    i.lote,
			    i.numBarril,
			    i.cantidad
			FROM inventariofinal i
			INNER JOIN cerveza c ON i.cerveza_id = c.id
			WHERE i.cantidad > 0

			UNION ALL

			SELECT
			    ii.id,
			    c2.nombre,
			    c2.tipo,
			    ii.lote,
			    ii.numBarril,
			    ii.cantidad
			FROM inventario_inicial ii
			INNER JOIN cerveza c2 ON ii.cerveza_id = c2.id
			WHERE ii.cantidad > 0
			""", nativeQuery = true)
	List<Object[]> obtenerInventarioDisponible();

	@Query(value = """
			    SELECT
			        c.nombre AS nombre_cerveza,
			        c.tipo AS tipo_cerveza,
			        i.lote AS lote,
			        i.numBarril AS num_barril,
			        i.cantidad AS cantidad
			    FROM inventariofinal i
			    JOIN cerveza c ON c.id = i.cerveza_id
			""", nativeQuery = true)
	List<Object[]> obtenerInventarioAgrupado();

	@Query("SELECT COALESCE(SUM(f.cantidad), 0) FROM InventarioFinal f")
	int obtenerTotalInventarioFinal();

}
