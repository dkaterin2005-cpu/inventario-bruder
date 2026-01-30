package com.bruder.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventarioFinal")
public class InventarioFinal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String cantidad;
	private String fechaRegistro;
	private String total;
	private String tipo;
	private String lote;
	private String numBarril;

	@ManyToOne
	@JoinColumn(name = "cerveza_id")
	private Cerveza cerveza;

	@ManyToOne
	@JoinColumn(name = "produccion_id")
	private Produccion produccion;

	@ManyToOne
	@JoinColumn(name = "inventario_inicial_id")
	private InventarioInicial inventarioInicial;

	public InventarioFinal() {
	}

	public InventarioFinal(Integer id, String cantidad, String fechaRegistro, String total, String tipo, String lote,
			String numBarril, Cerveza cerveza, Produccion produccion, InventarioInicial inventarioInicial) {
		super();
		this.id = id;
		this.cantidad = cantidad;
		this.fechaRegistro = fechaRegistro;
		this.total = total;
		this.tipo = tipo;
		this.lote = lote;
		this.numBarril = numBarril;
		this.cerveza = cerveza;
		this.produccion = produccion;
		this.inventarioInicial = inventarioInicial;
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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

	public Produccion getProduccion() {
		return produccion;
	}

	public void setProduccion(Produccion produccion) {
		this.produccion = produccion;
	}

	public InventarioInicial getInventarioInicial() {
		return inventarioInicial;
	}

	public void setInventarioInicial(InventarioInicial inventarioInicial) {
		this.inventarioInicial = inventarioInicial;
	}

	@Override
	public String toString() {
		return "InventarioFinal [id=" + id + ", cantidad=" + cantidad + ", fechaRegistro=" + fechaRegistro + ", total="
				+ total + ", tipo=" + tipo + ", lote=" + lote + ", numBarril=" + numBarril + ", cerveza=" + cerveza
				+ ", produccion=" + produccion + ", inventarioInicial=" + inventarioInicial + "]";
	}

}
