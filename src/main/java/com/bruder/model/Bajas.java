package com.bruder.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bajas")
public class Bajas {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String razon;
	private String cantidad;
	private String Fecha_registro;
	private String fechaVencimiento;
	private String total;
	private String lote;
	private String numBarril;

	@ManyToOne
	@JoinColumn(name = "cerveza_id")
	private Cerveza cerveza;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "despacho_id", nullable = true)
	private Despachos despacho;

	@ManyToOne
	@JoinColumn(name = "inventario_final_id")
	private InventarioFinal inventarioFinal;

	// Constructor vacío
	public Bajas() {
	}

	public Bajas(Integer id, String razon, String cantidad, String fecha_registro, String fechaVencimiento,
			String total, String lote, String numBarril, Cerveza cerveza, Usuario usuario, Despachos despacho,
			InventarioFinal inventarioFinal) {
		super();
		this.id = id;
		this.razon = razon;
		this.cantidad = cantidad;
		Fecha_registro = fecha_registro;
		this.fechaVencimiento = fechaVencimiento;
		this.total = total;
		this.lote = lote;
		this.numBarril = numBarril;
		this.cerveza = cerveza;
		this.usuario = usuario;
		this.despacho = despacho;
		this.inventarioFinal = inventarioFinal;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRazon() {
		return razon;
	}

	public void setRazon(String razon) {
		this.razon = razon;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public String getFecha_registro() {
		return Fecha_registro;
	}

	public void setFecha_registro(String fecha_registro) {
		Fecha_registro = fecha_registro;
	}

	public String getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
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

	public InventarioFinal getInventarioFinal() {
		return inventarioFinal;
	}

	public void setInventarioFinal(InventarioFinal inventarioFinal) {
		this.inventarioFinal = inventarioFinal;
	}

	@Override
	public String toString() {
		return "Bajas [id=" + id + ", razon=" + razon + ", cantidad=" + cantidad + ", Fecha_registro=" + Fecha_registro
				+ ", fechaVencimiento=" + fechaVencimiento + ", total=" + total + ", lote=" + lote + ", numBarril="
				+ numBarril + ", cerveza=" + cerveza + ", usuario=" + usuario + ", despacho=" + despacho
				+ ", inventarioFinal=" + inventarioFinal + "]";
	}

}
