package com.bruder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bruder.model.Despachos;

@Repository
public interface IDespachosRepository extends JpaRepository<Despachos, Integer> {

	// Buscar despachos por fecha exacta
	@Query("SELECT d FROM Despachos d WHERE d.fechaDespacho = :fecha")
	List<Despachos> findByFechaDespacho(String fecha);

	// Buscar despachos por rango de fechas
	@Query("SELECT d FROM Despachos d WHERE d.fechaDespacho BETWEEN :inicio AND :fin")
	List<Despachos> findByRangoFechas(String inicio, String fin);

	// Buscar despachos por nombre de cerveza
	@Query("SELECT d FROM Despachos d WHERE LOWER(d.cerveza.nombre) LIKE LOWER(CONCAT('%', :nombreCerveza, '%'))")
	List<Despachos> findByNombreCerveza(String nombreCerveza);

	// Buscar despachos por usuario
	@Query("SELECT d FROM Despachos d WHERE d.usuario.id = :usuarioId")
	List<Despachos> findByUsuarioId(Integer usuarioId);

	// Buscar despachos por punto de venta
	@Query("SELECT d FROM Despachos d WHERE d.puntoVenta.id = :puntoVentaId")
	List<Despachos> findByPuntoVenta(Integer puntoVentaId);

	// Listar todos los despachos ordenados por fecha de registro descendente
	@Query("SELECT d FROM Despachos d ORDER BY d.fechaRegistro DESC")
	List<Despachos> findAllOrderByFechaRegistroDesc();


}
