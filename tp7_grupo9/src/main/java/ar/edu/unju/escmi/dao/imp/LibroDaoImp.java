package ar.edu.unju.escmi.dao.imp;

import java.util.List;

import jakarta.persistence.EntityManager;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.ILibroDao;
import ar.edu.unju.escmi.entities.Libro;

public class LibroDaoImp implements ILibroDao {
	private static EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();

	@Override
	public void guardarLibro(Libro libro) {
		try {
			manager.getTransaction().begin();
			manager.persist(libro);
			manager.getTransaction().commit();
		}catch(Exception e) {
			if(manager.getTransaction() != null) {
				manager.getTransaction().rollback();
			}
			System.out.println("No se pudo guardar el libro");
		}finally {
			manager.close();
		}

	}

	@Override
	public void borrarLibro(Libro libro) {
		try {
			manager.getTransaction().begin();
			manager.remove(libro);
			manager.getTransaction().commit();
		}catch(Exception e) {
			if(manager.getTransaction() != null) {
				manager.getTransaction().rollback();
			}
			System.out.println("No se puede eliminar el libro");
		}finally {
			manager.close();
		}

	}

	@Override
	public List<Libro> obtenerLibros() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modificarLibro(Libro libro) {
		try {
			manager.getTransaction().begin();
			manager.merge(libro);
			manager.getTransaction().commit();
		}catch(Exception e) {
			if(manager.getTransaction() != null) {
				manager.getTransaction().rollback();
			}
			System.out.println("No se puede modificar el libro");
		}finally {
			
		}

	}

	@Override
	public Libro obtenerLibroPorId(Long idLibro) {
		return manager.find(Libro.class, idLibro);
	}

}
