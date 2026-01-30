package com.bruder.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "movimiento")
public class Movimiento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String cantidad;
	private String fechaMovimiento;
	private String fechaRegistro;
	private String tipoMovimiento;
	private String total;

	@ManyToOne
	@JoinColumn(name = "cerveza_id")
	private Cerveza cerveza;

	@ManyToOne
	@JoinColumn(name = "punto_venta_id")
	private PuntosVenta puntoVenta;

	@ManyToOne
	@JoinColumn(name = "produccion_id", nullable = true)
	private Produccion produccion;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "despacho_id", nullable = true)
	private Despachos despacho;

	@ManyToOne
	@JoinColumn(name = "baja_id", nullable = true)
	private Bajas baja;

	// Constructos vacio
	public Movimiento() {
	}

	public Movimiento(Integer id, String cantidad, String fechaMovimiento, String fechaRegistro, String tipoMovimiento,
			String total, Cerveza cerveza, PuntosVenta puntoVenta, Produccion produccion, Usuario usuario,
			Despachos despacho, Bajas baja) {
		super();
		this.id = id;
		this.cantidad = cantidad;
		this.fechaMovimiento = fechaMovimiento;
		this.fechaRegistro = fechaRegistro;
		this.tipoMovimiento = tipoMovimiento;
		this.total = total;
		this.cerveza = cerveza;
		this.puntoVenta = puntoVenta;
		this.produccion = produccion;
		this.usuario = usuario;
		this.despacho = despacho;
		this.baja = baja;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public String getFechaMovimiento() {
		return fechaMovimiento;
	}

	public void setFechaMovimiento(String fechaMovimiento) {
		this.fechaMovimiento = fechaMovimiento;
	}

	public String getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public Cerveza getCerveza() {
		return cerveza;
	}

	public void setCerveza(Cerveza cerveza) {
		this.cerveza = cerveza;
	}

	public PuntosVenta getPuntoVenta() {
		return puntoVenta;
	}

	public void setPuntoVenta(PuntosVenta puntoVenta) {
		this.puntoVenta = puntoVenta;
	}

	public Produccion getProduccion() {
		return produccion;
	}

	public void setProduccion(Produccion produccion) {
		this.produccion = produccion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Despachos getDespacho() {
		return despacho;
	}

	public void setDespacho(Despachos despacho) {
		this.despacho = despacho;
	}

	public Bajas getBaja() {
		return baja;
	}

	public void setBaja(Bajas baja) {
		this.baja = baja;
	}

	@Override
	public String toString() {
		return "Movimiento [id=" + id + ", cantidad=" + cantidad + ", fechaMovimiento=" + fechaMovimiento
				+ ", fechaRegistro=" + fechaRegistro + ", tipoMovimiento=" + tipoMovimiento + ", total=" + total
				+ ", cerveza=" + cerveza + ", puntoVenta=" + puntoVenta + ", produccion=" + produccion + ", usuario="
				+ usuario + ", despacho=" + despacho + ", baja=" + baja + "]";
	}

}
