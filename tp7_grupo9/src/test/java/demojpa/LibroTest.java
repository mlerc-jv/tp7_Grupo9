package demojpa;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import ar.edu.unju.escmi.dao.ILibroDao;
import ar.edu.unju.escmi.dao.imp.LibroDaoImp;
import ar.edu.unju.escmi.entities.Libro;

class LibroTest {
	ILibroDao libroDao = new LibroDaoImp();

	@Test
	void testGuardarLibro() {
		Libro libro = new Libro("125466","Algebra I",LocalDate.of(2000, 10, 20));
		libroDao.guardarLibro(libro);
		assertTrue(libro != null);
	}
	
	/*@Test
	void testModificarLibro() {
		Libro libro = libroDao.obtenerLibroPorId(1L);
		//modificamos el libro
		//libro.setTitulo("Algebra II");
		//libroDao.modificarLibro(libro);
		libroDao.borrarLibro(libro);
		assertTrue(libro != null);
	}*/

}
