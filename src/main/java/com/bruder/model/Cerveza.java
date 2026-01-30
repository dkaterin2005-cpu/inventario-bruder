package com.bruder.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cerveza")
public class Cerveza {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nombre;
	private String tipo;

	private boolean activo = true; 

	// Relaciones
	@OneToMany(mappedBy = "cerveza")
	private List<Produccion> producciones;

	@OneToMany(mappedBy = "cerveza")
	private List<Despachos> despachos;

	@OneToMany(mappedBy = "cerveza")
	private List<Bajas> bajas;

	@OneToMany(mappedBy = "cerveza")
	private List<InventarioInicial> inventariosInicial;

	@OneToMany(mappedBy = "cerveza")
	private List<InventarioFinal> inventariosFinal;

	@OneToMany(mappedBy = "cerveza")
	private List<Movimiento> movimientos;

	// Constructor vacío
	public Cerveza() {
	}

	public Cerveza(Integer id, String nombre, String tipo, boolean activo, List<Produccion> producciones,
			List<Despachos> despachos, List<Bajas> bajas, List<InventarioInicial> inventariosInicial,
			List<InventarioFinal> inventariosFinal, List<Movimiento> movimientos) {
		this.id = id;
		this.nombre = nombre;
		this.tipo = tipo;
		this.activo = activo;
		this.producciones = producciones;
		this.despachos = despachos;
		this.bajas = bajas;
		this.inventariosInicial = inventariosInicial;
		this.inventariosFinal = inventariosFinal;
		this.movimientos = movimientos;
	}

	// Getters y Setters
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean isActivo() {
		return activo;
	} // ✅ usa "is" para boolean

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public List<Produccion> getProducciones() {
		return producciones;
	}

	public void setProducciones(List<Produccion> producciones) {
		this.producciones = producciones;
	}

	public List<Despachos> getDespachos() {
		return despachos;
	}

	public void setDespachos(List<Despachos> despachos) {
		this.despachos = despachos;
	}

	public List<Bajas> getBajas() {
		return bajas;
	}

	public void setBajas(List<Bajas> bajas) {
		this.bajas = bajas;
	}

	public List<InventarioInicial> getInventariosInicial() {
		return inventariosInicial;
	}

	public void setInventariosInicial(List<InventarioInicial> inventariosInicial) {
		this.inventariosInicial = inventariosInicial;
	}

	public List<InventarioFinal> getInventariosFinal() {
		return inventariosFinal;
	}

	public void setInventariosFinal(List<InventarioFinal> inventariosFinal) {
		this.inventariosFinal = inventariosFinal;
	}

	public List<Movimiento> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(List<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}

	@Override
	public String toString() {
		return "Cerveza [id=" + id + ", nombre=" + nombre + ", tipo=" + tipo + ", activo=" + activo + "]";
	}
}
