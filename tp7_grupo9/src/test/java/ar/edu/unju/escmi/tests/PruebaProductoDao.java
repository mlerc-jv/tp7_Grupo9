package ar.edu.unju.escmi.tests;

import ar.edu.unju.escmi.dao.IProductoDao;
import ar.edu.unju.escmi.dao.imp.ProductoDaoImp;
import ar.edu.unju.escmi.entities.Producto;

public class PruebaProductoDao {

    public static void main(String[] args) {

        IProductoDao dao = new ProductoDaoImp();

        try {
            Producto producto = new Producto("Producto de prueba", 15000.0);

            dao.guardarProducto(producto);
            System.out.println("Producto guardado con ID: " + producto.getId());

            Producto encontrado = dao.obtenerProductoPorId(producto.getId());
            System.out.println("Producto encontrado: " + encontrado.getDescripcion());

            dao.modificarPrecioProducto(producto.getId(), 20000.0);
            System.out.println("Precio modificado correctamente");

            System.out.println("Productos activos: " + dao.obtenerProductos().size());

            dao.eliminarProducto(producto);
            System.out.println("Producto dado de baja correctamente");

            Producto dadoDeBaja = dao.obtenerProductoPorId(producto.getId());
            System.out.println("Estado final del producto: " + dadoDeBaja.isEstado());

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}