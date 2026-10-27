package ar.edu.unju.escmi.dao;

import java.util.List;

import ar.edu.unju.escmi.entities.Producto;

public interface IProductoDao {

    public void guardarProducto(Producto producto);

    public Producto obtenerProductoPorId(Long idProducto);

    public List<Producto> obtenerProductos();

    public void modificarPrecioProducto(Long idProducto, double nuevoPrecio);

    public void eliminarProducto(Producto producto);
}