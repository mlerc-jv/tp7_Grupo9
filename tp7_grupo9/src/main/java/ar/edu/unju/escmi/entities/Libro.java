package ar.edu.unju.escmi.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
@Entity
@Table(name = "libros")
public class Libro {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="lib_id")
	private Long id;
	
	@Column(name="lib_isbn")
	private String isbn;
	
	@Column(name="lib_titulo", length = 30,nullable = false ) 
	private String titulo;
	
	@Column(name="lib_fecha_publicacion")
	private LocalDate fechaPublicacion;
	
	@Column(name="lib_estado")
	private boolean estado;
	
	public Libro() {
		
	}

	
	
	public Libro(String isbn, String titulo, LocalDate fechaPublicacion) {
		super();
		this.isbn = isbn;
		this.titulo = titulo;
		this.fechaPublicacion = fechaPublicacion;
		this.estado = true;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public LocalDate getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(LocalDate fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "Libro [id=" + id + ", isbn=" + isbn + ", titulo=" + titulo + ", fechaPublicacion=" + fechaPublicacion
				+ ", estado=" + estado + "]";
	}
	
	
	

}
