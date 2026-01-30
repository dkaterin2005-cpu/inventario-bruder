package com.bruder.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "produccion")
public class Produccion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String fechaProduccion;
	private String cantidad;
	private String fechaVencimiento;
	private String observacion;
	private String lote;
	private String numBarril;

	// Relaciones
	@ManyToOne
	@JoinColumn(name = "cerveza_id")
	private Cerveza cerveza;

	@OneToMany(mappedBy = "produccion")
	private List<Movimiento> movimientos;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	public Produccion() {
	}

	public Produccion(Integer id, String fechaProduccion, String cantidad, String fechaVencimiento, String observacion,
			String lote, String numBarril, Cerveza cerveza, List<Movimiento> movimientos, Usuario usuario) {
		super();
		this.id = id;
		this.fechaProduccion = fechaProduccion;
		this.cantidad = cantidad;
		this.fechaVencimiento = fechaVencimiento;
		this.observacion = observacion;
		this.lote = lote;
		this.numBarril = numBarril;
		this.cerveza = cerveza;
		this.movimientos = movimientos;
		this.usuario = usuario;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFechaProduccion() {
		return fechaProduccion;
	}

	public void setFechaProduccion(String fechaProduccion) {
		this.fechaProduccion = fechaProduccion;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public String getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getNumBarril() {
		return numBarril;
	}

	public void setNumBarril(String numBarril) {
		this.numBarril = numBarril;
	}

	public Cerveza getCerveza() {
		return cerveza;
	}

	public void setCerveza(Cerveza cerveza) {
		this.cerveza = cerveza;
	}

	public List<Movimiento> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(List<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "Produccion [id=" + id + ", fechaProduccion=" + fechaProduccion + ", cantidad=" + cantidad
				+ ", fechaVencimiento=" + fechaVencimiento + ", observacion=" + observacion + ", lote=" + lote
				+ ", numBarril=" + numBarril + ", cerveza=" + cerveza + ", movimientos=" + movimientos + ", usuario="
				+ usuario + "]";
	}

}
