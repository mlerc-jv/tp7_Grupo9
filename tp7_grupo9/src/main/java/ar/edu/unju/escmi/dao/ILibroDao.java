package ar.edu.unju.escmi.dao;

import java.util.List;

import ar.edu.unju.escmi.entities.Libro;

public interface ILibroDao {
	public void guardarLibro(Libro libro);
	public void borrarLibro(Libro libro);
	public List<Libro> obtenerLibros();
	public void modificarLibro(Libro libro);
	public Libro obtenerLibroPorId(Long idLibro);

}
