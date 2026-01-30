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
@Table(name = "despacho")
public class Despachos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String fechaDespacho;
	private String cantidad;
	private String fechaRegistro;
	private String observacion;
	private String Lote;
	private String NumBarril;
	private String total;

	// Relaciones
	@ManyToOne
	@JoinColumn(name = "cerveza_id")
	private Cerveza cerveza;

	@ManyToOne
	@JoinColumn(name = "punto_venta_id")
	private PuntosVenta puntoVenta;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@OneToMany(mappedBy = "despacho")
	private List<Movimiento> movimientos;

	@ManyToOne
	@JoinColumn(name = "inventario_final_id")
	private InventarioFinal inventarioFinal;

	public Despachos() {
	}

	public Despachos(Integer id, String fechaDespacho, String cantidad, String fechaRegistro, String observacion,
			String lote, String numBarril, String total, Cerveza cerveza, PuntosVenta puntoVenta, Usuario usuario,
			List<Movimiento> movimientos, InventarioFinal inventarioFinal) {
		super();
		this.id = id;
		this.fechaDespacho = fechaDespacho;
		this.cantidad = cantidad;
		this.fechaRegistro = fechaRegistro;
		this.observacion = observacion;
		Lote = lote;
		NumBarril = numBarril;
		this.total = total;
		this.cerveza = cerveza;
		this.puntoVenta = puntoVenta;
		this.usuario = usuario;
		this.movimientos = movimientos;
		this.inventarioFinal = inventarioFinal;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFechaDespacho() {
		return fechaDespacho;
	}

	public void setFechaDespacho(String fechaDespacho) {
		this.fechaDespacho = fechaDespacho;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public String getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getLote() {
		return Lote;
	}

	public void setLote(String lote) {
		Lote = lote;
	}

	public String getNumBarril() {
		return NumBarril;
	}

	public void setNumBarril(String numBarril) {
		NumBarril = numBarril;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Movimiento> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(List<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}

	public InventarioFinal getInventarioFinal() {
		return inventarioFinal;
	}

	public void setInventarioFinal(InventarioFinal inventarioFinal) {
		this.inventarioFinal = inventarioFinal;
	}

	@Override
	public String toString() {
		return "Despachos [id=" + id + ", fechaDespacho=" + fechaDespacho + ", cantidad=" + cantidad
				+ ", fechaRegistro=" + fechaRegistro + ", observacion=" + observacion + ", Lote=" + Lote
				+ ", NumBarril=" + NumBarril + ", total=" + total + ", cerveza=" + cerveza + ", puntoVenta="
				+ puntoVenta + ", usuario=" + usuario + ", movimientos=" + movimientos + ", inventarioFinal="
				+ inventarioFinal + "]";
	}

}
