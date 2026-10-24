package ar.edu.unju.escmi.dao.imp;

import java.util.List;
import jakarta.persistence.EntityManager;
import ar.edu.unju.escmi.config.EmfSingleton;
import ar.edu.unju.escmi.dao.IClienteDao;
import ar.edu.unju.escmi.entities.Cliente;

public class ClienteDaoImp implements IClienteDao {

    @Override
    public void guardarCliente(Cliente cliente) {
        EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();

        try {
            manager.getTransaction().begin();
            manager.persist(cliente);
            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            System.out.println("No se pudo guardar el cliente");
        } finally {
            manager.close();
        }
    }

    @Override
    public void modificarCliente(Cliente cliente) {
        EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();

        try {
            manager.getTransaction().begin();
            manager.merge(cliente);
            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            System.out.println("No se pudo modificar el cliente");
        } finally {
            manager.close();
        }
    }

    @Override
    public List<Cliente> obtenerClientes() {
        EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();

        try {
            return manager.createQuery("SELECT c FROM Cliente c", Cliente.class)
                          .getResultList();
        } finally {
            manager.close();
        }
    }

    @Override
    public Cliente obtenerClientePorId(Long idCliente) {
        EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();

        try {
            return manager.find(Cliente.class, idCliente);
        } finally {
            manager.close();
        }
    }

    @Override
    public void eliminarCliente(Cliente cliente) {
        EntityManager manager = EmfSingleton.getInstance().getEmf().createEntityManager();

        try {
            manager.getTransaction().begin();

            Cliente clienteEncontrado = manager.find(Cliente.class, cliente.getId());

            if (clienteEncontrado != null) {
                clienteEncontrado.setEstado(false);
            }

            manager.getTransaction().commit();
        } catch (Exception e) {
            if (manager.getTransaction().isActive()) {
                manager.getTransaction().rollback();
            }
            System.out.println("No se pudo dar de baja al cliente");
        } finally {
            manager.close();
        }
    }
}