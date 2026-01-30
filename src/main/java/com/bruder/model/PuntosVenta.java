package com.bruder.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "puntoVenta")
public class PuntosVenta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nombre;
	private String direccion;
	private boolean activo = true;

	// Relaciones
	@OneToMany(mappedBy = "puntoVenta")
	private List<Despachos> despachos;

	@OneToMany(mappedBy = "puntoVenta")
	private List<Movimiento> movimientos;

	// Constructor vacío
	public PuntosVenta() {
	}

	public PuntosVenta(Integer id, String nombre, String direccion, boolean activo, List<Despachos> despachos,
			List<Movimiento> movimientos) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.activo = activo;
		this.despachos = despachos;
		this.movimientos = movimientos;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public List<Despachos> getDespachos() {
		return despachos;
	}

	public void setDespachos(List<Despachos> despachos) {
		this.despachos = despachos;
	}

	public List<Movimiento> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(List<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}

	@Override
	public String toString() {
		return "PuntosVenta [id=" + id + ", nombre=" + nombre + ", direccion=" + direccion + ", activo=" + activo
				+ ", despachos=" + despachos + ", movimientos=" + movimientos + "]";
	}

}