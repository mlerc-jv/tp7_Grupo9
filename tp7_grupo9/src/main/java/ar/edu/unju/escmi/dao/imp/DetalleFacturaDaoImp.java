
package ar.edu.unju.escmi.dao.imp;

import java.util.List;

import jakarta.persistence.EntityManager;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IDetalleFacturaDao;
import ar.edu.unju.escmi.entities.DetalleFactura;

public class DetalleFacturaDaoImp implements IDetalleFacturaDao {

    @Override
    public void guardarDetalleFactura(DetalleFactura detalle) {
        EntityManager manager = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        try {
            manager.getTransaction().begin();
            manager.persist(detalle);
            manager.getTransaction().commit();

        } catch (Exception e) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }

            System.out.println("No se pudo guardar el detalle de factura");
            e.printStackTrace();

        } finally {
            manager.close();
        }
    }

    @Override
    public DetalleFactura buscarDetalleFactura(Long id) {
        EntityManager manager = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        try {
            return manager.find(DetalleFactura.class, id);

        } finally {
            manager.close();
        }
    }

    @Override
    public List<DetalleFactura> obtenerDetallesFactura() {
        EntityManager manager = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        try {
            return manager.createQuery(
                    "SELECT d FROM DetalleFactura d",
                    DetalleFactura.class
            ).getResultList();

        } finally {
            manager.close();
        }
    }

    @Override
    public List<DetalleFactura> obtenerDetallesPorFactura(Long idFactura) {
        EntityManager manager = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        try {
            return manager.createQuery(
                    "SELECT d FROM DetalleFactura d WHERE d.factura.id = :idFactura",
                    DetalleFactura.class
            ).setParameter("idFactura", idFactura)
             .getResultList();

        } finally {
            manager.close();
        }
    }
}