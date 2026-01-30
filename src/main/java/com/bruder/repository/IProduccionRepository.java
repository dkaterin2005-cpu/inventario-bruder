package com.bruder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.bruder.model.Produccion;
import java.util.List;

@Repository
public interface IProduccionRepository extends JpaRepository<Produccion, Integer> {

	List<Produccion> findByFechaProduccion(String fechaProduccion);

	List<Produccion> findByCerveza_Id(Integer cervezaId);

	List<Produccion> findByFechaVencimiento(String fechaVencimiento);

	Produccion findTopByCerveza_IdOrderByIdDesc(Integer cervezaId);

	@Query("SELECT COALESCE(SUM(CAST(p.cantidad AS integer)), 0) FROM Produccion p")
	int obtenerTotalProduccion();

}
