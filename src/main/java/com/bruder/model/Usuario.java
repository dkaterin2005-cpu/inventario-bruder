package com.bruder.model;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String correo;
    private String tipo;
    private String password;

    // 🔹 Relaciones principales
    @OneToMany(mappedBy = "usuario")
    private List<Produccion> producciones;

    @OneToMany(mappedBy = "usuario")
    private List<Despachos> despachos;

    @OneToMany(mappedBy = "usuario")
    private List<Movimiento> movimientos;

    // 🔹 Constructor vacío
    public Usuario() {
    }

    // 🔹 Constructor con parámetros
    public Usuario(Integer id, String nombre, String correo, String tipo, String password) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.tipo = tipo;
        this.password = password;
    }

    // 🔹 Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<Produccion> getProducciones() { return producciones; }
    public void setProducciones(List<Produccion> producciones) { this.producciones = producciones; }

    public List<Despachos> getDespachos() { return despachos; }
    public void setDespachos(List<Despachos> despachos) { this.despachos = despachos; }

    public List<Movimiento> getMovimientos() { return movimientos; }
    public void setMovimientos(List<Movimiento> movimientos) { this.movimientos = movimientos; }

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", correo=" + correo + ", tipo=" + tipo + ", password="
				+ password + ", producciones=" + producciones + ", despachos=" + despachos + ", movimientos="
				+ movimientos + "]";
	}

    
}
