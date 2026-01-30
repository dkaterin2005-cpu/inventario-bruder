package com.bruder.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventario_inicial")
public class InventarioInicial {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String cantidad;
	private String fechaRegistro;
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
	@JoinColumn(name = "produccion_id")
	private Produccion produccion;

	// Constructor vacío requerido por JPA
	public InventarioInicial() {
	}

	public InventarioInicial(Integer id, String cantidad, String fechaRegistro, String total, String lote,
			String numBarril, Cerveza cerveza, Usuario usuario, Produccion produccion) {
		super();
		this.id = id;
		this.cantidad = cantidad;
		this.fechaRegistro = fechaRegistro;
		this.total = total;
		this.lote = lote;
		this.numBarril = numBarril;
		this.cerveza = cerveza;
		this.usuario = usuario;
		this.produccion = produccion;
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

	public String getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
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

	public Produccion getProduccion() {
		return produccion;
	}

	public void setProduccion(Produccion produccion) {
		this.produccion = produccion;
	}

	@Override
	public String toString() {
		return "InventarioInicial [id=" + id + ", cantidad=" + cantidad + ", fechaRegistro=" + fechaRegistro
				+ ", total=" + total + ", lote=" + lote + ", numBarril=" + numBarril + ", cerveza=" + cerveza
				+ ", usuario=" + usuario + ", produccion=" + produccion + "]";
	}

}
