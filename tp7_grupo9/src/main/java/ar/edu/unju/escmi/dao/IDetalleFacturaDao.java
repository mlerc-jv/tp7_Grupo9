package ar.edu.unju.escmi.dao;

import java.util.List;
import ar.edu.unju.escmi.entities.DetalleFactura;

public interface IDetalleFacturaDao {

    public void guardarDetalleFactura(DetalleFactura detalle);

    public DetalleFactura buscarDetalleFactura(Long id);

    public List<DetalleFactura> obtenerDetallesFactura();

    public List<DetalleFactura> obtenerDetallesPorFactura(Long idFactura);
}