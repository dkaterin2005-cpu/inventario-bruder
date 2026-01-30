package com.bruder.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bruder.model.Bajas;

@Repository
public interface IBajasRepository extends JpaRepository<Bajas, Integer> {

	// Buscar bajas por nombre de cerveza
	@Query("SELECT b FROM Bajas b WHERE b.cerveza.nombre LIKE %:nombreCerveza%")
	List<Bajas> findByNombreCerveza(String nombreCerveza);

	// Buscar bajas por usuario
	@Query("SELECT b FROM Bajas b WHERE b.usuario.id = :usuarioId")
	List<Bajas> findByUsuarioId(Integer usuarioId);

	// Buscar bajas por razón
	@Query("SELECT b FROM Bajas b WHERE LOWER(b.razon) LIKE LOWER(CONCAT('%', :razon, '%'))")
	List<Bajas> findByRazon(String razon);

	// Buscar bajas por fecha de registro
	@Query("SELECT b FROM Bajas b WHERE b.Fecha_registro = :fechaRegistro")
	List<Bajas> findByFechaRegistro(String fechaRegistro);

	// Buscar bajas próximas a vencer
	@Query("SELECT b FROM Bajas b WHERE b.fechaVencimiento <= :fechaLimite")
	List<Bajas> findBajasPorVencimiento(String fechaLimite);

	// Consultar todas las bajas ordenadas por fecha de registro descendente
	@Query("SELECT b FROM Bajas b ORDER BY b.Fecha_registro DESC")
	List<Bajas> findAllOrderByFechaRegistroDesc();

	@Query("""
			SELECT b FROM Bajas b
			WHERE (:cervezaId IS NULL OR b.cerveza.id = :cervezaId)
			AND (:desde IS NULL OR b.Fecha_registro >= :desde)
			AND (:hasta IS NULL OR b.Fecha_registro <= :hasta)
			""")
	List<Bajas> buscarPorFiltros(@Param("cervezaId") Integer cervezaId, @Param("desde") String desde,
			@Param("hasta") String hasta);

	@Query("""
				SELECT COALESCE(SUM(CAST(b.cantidad AS int)), 0)
				FROM Bajas b
				WHERE b.inventarioFinal.id = :inventarioId
			""")
	int totalBajasPorInventario(@Param("inventarioId") Integer inventarioId);

}
