package com.bruder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bruder.model.Cerveza;

@Repository
public interface ICervezaRepository extends JpaRepository<Cerveza, Integer> {

	// Buscar cerveza por nombre exacto
	//Cerveza findByNombre(String nombre);

	@Query("SELECT c FROM Cerveza c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
	List<Cerveza> findByNombreParcial(String nombre);

	@Query("SELECT c FROM Cerveza c WHERE LOWER(c.tipo) LIKE LOWER(CONCAT('%', :tipo, '%'))")
	List<Cerveza> findByTipo(String tipo);

	@Query("SELECT c FROM Cerveza c WHERE c.activo = true")
	List<Cerveza> findActivas();

	@Query("SELECT c FROM Cerveza c WHERE c.activo = false")
	List<Cerveza> findInactivas();

	@Query("SELECT c FROM Cerveza c ORDER BY c.nombre ASC")
	List<Cerveza> findAllOrderByNombreAsc();

	List<Cerveza> findByActivoTrue();
	
	@Query("SELECT c FROM Cerveza c WHERE LOWER(c.nombre) = LOWER(:nombre)")
	Cerveza findByNombre(@Param("nombre") String nombre);


}
