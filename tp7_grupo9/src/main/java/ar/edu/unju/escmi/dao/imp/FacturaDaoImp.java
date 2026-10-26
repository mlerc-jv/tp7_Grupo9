
package ar.edu.unju.escmi.dao.imp;

import java.util.List;

import jakarta.persistence.EntityManager;

import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IFacturaDao;
import ar.edu.unju.escmi.entities.Factura;

public class FacturaDaoImp implements IFacturaDao {

    @Override
    public void guardarFactura(Factura factura) {
        EntityManager manager = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        try {
            manager.getTransaction().begin();
            manager.persist(factura);
            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            System.out.println("No se pudo guardar la factura");
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }

    @Override
    public Factura buscarFactura(Long id) {
        EntityManager manager = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        try {
            return manager.find(Factura.class, id);
        } finally {
            manager.close();
        }
    }

    @Override
    public List<Factura> obtenerFacturas() {
        EntityManager manager = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        try {
            return manager.createQuery(
                    "SELECT f FROM Factura f WHERE f.estado = true",
                    Factura.class
            ).getResultList();
        } finally {
            manager.close();
        }
    }

    @Override
    public List<Factura> obtenerFacturasMayoresA(double monto) {
        EntityManager manager = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        try {
            return manager.createQuery(
                    "SELECT f FROM Factura f WHERE f.estado = true AND f.total > :monto",
                    Factura.class
            ).setParameter("monto", monto)
             .getResultList();
        } finally {
            manager.close();
        }
    }

    @Override
    public void eliminarFacturaLogica(Factura factura) {
        EntityManager manager = EmfSingleton.getInstance()
                .getEmf().createEntityManager();

        try {
            manager.getTransaction().begin();

            Factura facturaEncontrada =
                    manager.find(Factura.class, factura.getId());

            if (facturaEncontrada != null) {
                facturaEncontrada.setEstado(false);
            }

            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            System.out.println("No se pudo dar de baja la factura");
            e.printStackTrace();
        } finally {
            manager.close();
        }
    }
}
