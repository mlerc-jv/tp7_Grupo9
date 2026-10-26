package ar.edu.unju.escmi.dao;

import java.util.List;
import ar.edu.unju.escmi.entities.Factura;

public interface IFacturaDao {

    public void guardarFactura(Factura factura);

    public Factura buscarFactura(Long id);

    public List<Factura> obtenerFacturas();

    public List<Factura> obtenerFacturasMayoresA(double monto);

    public void eliminarFacturaLogica(Factura factura);
}
