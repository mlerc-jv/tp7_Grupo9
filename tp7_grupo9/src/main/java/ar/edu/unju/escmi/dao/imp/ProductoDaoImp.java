package ar.edu.unju.escmi.dao.imp;

import java.util.List;

import jakarta.persistence.EntityManager;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IProductoDao;
import ar.edu.unju.escmi.entities.Producto;

public class ProductoDaoImp implements IProductoDao {

    @Override
    public void guardarProducto(Producto producto) {

        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }

        if (producto.getDescripcion() == null ||
                producto.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripcion es obligatoria");
        }

        if (!Double.isFinite(producto.getPrecioUnitario()) ||
                producto.getPrecioUnitario() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }

        EntityManager manager = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        try {
            manager.getTransaction().begin();

            producto.setEstado(true);
            manager.persist(producto);

            manager.getTransaction().commit();

        } catch (Exception e) {

            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }

            throw new IllegalStateException(
                    "No se pudo guardar el producto", e);

        } finally {
            manager.close();
        }
    }

    @Override
    public Producto obtenerProductoPorId(Long idProducto) {

        if (idProducto == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        EntityManager manager = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        try {
            return manager.find(Producto.class, idProducto);

        } finally {
            manager.close();
        }
    }

    @Override
    public List<Producto> obtenerProductos() {

        EntityManager manager = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        try {
            return manager.createQuery(
                    "SELECT p FROM Producto p WHERE p.estado = true",
                    Producto.class
            ).getResultList();

        } finally {
            manager.close();
        }
    }

    @Override
    public void modificarPrecioProducto(Long idProducto, double nuevoPrecio) {

        if (idProducto == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        if (!Double.isFinite(nuevoPrecio) || nuevoPrecio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }

        EntityManager manager = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        try {
            manager.getTransaction().begin();

            Producto producto = manager.find(Producto.class, idProducto);

            if (producto == null || !producto.isEstado()) {
                throw new IllegalArgumentException(
                        "El producto no existe o esta dado de baja");
            }

            producto.setPrecioUnitario(nuevoPrecio);

            manager.getTransaction().commit();

        } catch (Exception e) {

            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }

            if (e instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e;
            }

            throw new IllegalStateException(
                    "No se pudo modificar el precio del producto", e);

        } finally {
            manager.close();
        }
    }

    @Override
    public void eliminarProducto(Producto producto) {

        if (producto == null || producto.getId() == null) {
            throw new IllegalArgumentException("El producto no es valido");
        }

        EntityManager manager = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        try {
            manager.getTransaction().begin();

            Producto productoEncontrado =
                    manager.find(Producto.class, producto.getId());

            if (productoEncontrado == null) {
                throw new IllegalArgumentException(
                        "No se encontro el producto");
            }

            productoEncontrado.setEstado(false);

            manager.getTransaction().commit();

        } catch (Exception e) {

            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }

            if (e instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e;
            }

            throw new IllegalStateException(
                    "No se pudo dar de baja al producto", e);

        } finally {
            manager.close();
        }
    }
}