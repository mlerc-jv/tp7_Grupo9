package ar.edu.unju.escmi.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ar.edu.unju.escmi.dao.IClienteDao;
import ar.edu.unju.escmi.dao.IFacturaDao;
import ar.edu.unju.escmi.dao.IProductoDao;
import ar.edu.unju.escmi.dao.imp.ClienteDaoImp;
import ar.edu.unju.escmi.dao.imp.FacturaDaoImp;
import ar.edu.unju.escmi.dao.imp.ProductoDaoImp;
import ar.edu.unju.escmi.entities.Cliente;
import ar.edu.unju.escmi.entities.DetalleFactura;
import ar.edu.unju.escmi.entities.Factura;
import ar.edu.unju.escmi.entities.Producto;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final IClienteDao clienteDao = new ClienteDaoImp();
    private static final IProductoDao productoDao = new ProductoDaoImp();
    private static final IFacturaDao facturaDao = new FacturaDaoImp();

    public static void main(String[] args) {
        int opcion = -1;
        do {
            mostrarMenu();
            try {
                opcion = leerEntero("Ingrese una opción: ");
                switch (opcion) {
                    case 1 -> altaCliente();
                    case 2 -> altaProducto();
                    case 3 -> realizarVenta();
                    case 4 -> buscarFactura();
                    case 5 -> eliminarFactura();
                    case 6 -> eliminarProducto();
                    case 7 -> modificarCliente();
                    case 8 -> modificarPrecioProducto();
                    case 9 -> eliminarCliente();
                    case 10 -> mostrarFacturas();
                    case 11 -> mostrarClientes();
                    case 12 -> mostrarFacturasMayores();
                    case 0 -> System.out.println("Saliendo del programa...");
                    default -> System.out.println("Opción inválida. Ingrese un número entre 0 y 12.");
                }
            } catch (Exception e) {
                // Cualquier error de una operación se informa y el menú sigue funcionando
                System.out.println("Ocurrió un error: " + e.getMessage());
            }
        } while (opcion != 0);
        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n========== MENÚ ==========");
        System.out.println("1  - Alta de cliente");
        System.out.println("2  - Alta de producto");
        System.out.println("3  - Realizar venta (nueva factura)");
        System.out.println("4  - Buscar factura por número");
        System.out.println("5  - Eliminar factura");
        System.out.println("6  - Eliminar producto");
        System.out.println("7  - Modificar datos de cliente");
        System.out.println("8  - Modificar precio de producto");
        System.out.println("9  - Eliminar cliente");
        System.out.println("10 - Mostrar todas las facturas");
        System.out.println("11 - Mostrar todos los clientes");
        System.out.println("12 - Mostrar facturas que superen $900.000");
        System.out.println("0  - Salir");
    }

    // ===================== OPCIONES =====================

    private static void altaCliente() {
        Cliente cliente = new Cliente();
        cliente.setNombre(leerTexto("Nombre: "));
        cliente.setApellido(leerTexto("Apellido: "));
        cliente.setDomicilio(leerTexto("Domicilio: "));
        cliente.setDni(leerEnteroPositivo("DNI: "));
        cliente.setEstado(true);
        clienteDao.guardarCliente(cliente);
        System.out.println("Cliente registrado correctamente.");
    }

    private static void altaProducto() {
        Producto producto = new Producto();
        producto.setDescripcion(leerTexto("Descripción: "));
        producto.setPrecioUnitario(leerDoublePositivo("Precio unitario: "));
        producto.setEstado(true);
        productoDao.guardarProducto(producto);
        System.out.println("Producto registrado correctamente.");
    }

    private static void realizarVenta() {
        List<Cliente> clientes = clientesActivos();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes activos. Primero dé de alta un cliente.");
            return;
        }
        if (productosActivos().isEmpty()) {
            System.out.println("No hay productos activos. Primero dé de alta un producto.");
            return;
        }

        System.out.println("\n--- Clientes disponibles ---");
        clientes.forEach(Main::imprimirCliente);
        Cliente cliente = clienteDao.obtenerClientePorId(leerLong("Ingrese el ID del cliente: "));
        if (cliente == null || !cliente.isEstado()) {
            System.out.println("El cliente no existe o está dado de baja.");
            return;
        }

        List<DetalleFactura> detalles = new ArrayList<>();
        double total = 0;
        boolean seguir = true;

        while (seguir) {
            System.out.println("\n--- Productos disponibles ---");
            productosActivos().forEach(Main::imprimirProducto);
            long idProducto = leerLong("ID del producto (0 para terminar): ");
            if (idProducto == 0) {
                seguir = false;
                continue;
            }
            Producto producto = productoDao.obtenerProductoPorId(idProducto);
            if (producto == null || !producto.isEstado()) {
                System.out.println("El producto no existe o está dado de baja.");
                continue;
            }
            int cantidad = leerEnteroPositivo("Cantidad: ");

            DetalleFactura detalle = new DetalleFactura();
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setSubtotal(cantidad * producto.getPrecioUnitario());
            detalles.add(detalle);
            total += detalle.getSubtotal();

            System.out.printf("Agregado: %s x%d = $%.2f (total parcial: $%.2f)%n",
                    producto.getDescripcion(), cantidad, detalle.getSubtotal(), total);
        }

        if (detalles.isEmpty()) {
            System.out.println("Venta cancelada: no se agregaron productos.");
            return;
        }

        Factura factura = new Factura();
        factura.setFecha(LocalDate.now());
        factura.setCliente(cliente);
        factura.setDomicilio(cliente.getDomicilio());
        factura.setDetalles(detalles);
        factura.setTotal(total);
        factura.setEstado(true);
        facturaDao.guardarFactura(factura);

        System.out.println("Factura registrada correctamente.");
        imprimirFactura(factura);
    }

    private static void buscarFactura() {
        Factura factura = facturaDao.buscarFactura(leerLong("Número de factura: "));
        if (factura == null || !factura.isEstado()) {
            System.out.println("No se encontró una factura activa con ese número.");
            return;
        }
        imprimirFactura(factura);
    }

    private static void eliminarFactura() {
        Factura factura = facturaDao.buscarFactura(leerLong("Número de factura a eliminar: "));
        if (factura == null || !factura.isEstado()) {
            System.out.println("No se encontró una factura activa con ese número.");
            return;
        }
        if (confirmar("¿Seguro que desea eliminar la factura N° " + factura.getId() + "? (s/n): ")) {
            facturaDao.eliminarFacturaLogica(factura);
            System.out.println("Factura eliminada.");
        }
    }

    private static void eliminarProducto() {
        Producto producto = productoDao.obtenerProductoPorId(leerLong("ID del producto a eliminar: "));
        if (producto == null || !producto.isEstado()) {
            System.out.println("No se encontró un producto activo con ese ID.");
            return;
        }
        if (confirmar("¿Seguro que desea eliminar " + producto.getDescripcion() + "? (s/n): ")) {
            productoDao.eliminarProducto(producto);
            System.out.println("Producto eliminado.");
        }
    }

    private static void modificarCliente() {
        Cliente cliente = clienteDao.obtenerClientePorId(leerLong("ID del cliente a modificar: "));
        if (cliente == null || !cliente.isEstado()) {
            System.out.println("No se encontró un cliente activo con ese ID.");
            return;
        }
        System.out.println("Datos actuales:");
        imprimirCliente(cliente);
        System.out.println("(Presione Enter para dejar el dato sin cambios)");

        String nombre = leerOpcional("Nuevo nombre: ");
        if (!nombre.isEmpty()) cliente.setNombre(nombre);

        String apellido = leerOpcional("Nuevo apellido: ");
        if (!apellido.isEmpty()) cliente.setApellido(apellido);

        String domicilio = leerOpcional("Nuevo domicilio: ");
        if (!domicilio.isEmpty()) cliente.setDomicilio(domicilio);

        String dni = leerOpcional("Nuevo DNI: ");
        if (!dni.isEmpty()) {
            try {
                int nuevoDni = Integer.parseInt(dni);
                if (nuevoDni <= 0) throw new NumberFormatException();
                cliente.setDni(nuevoDni);
            } catch (NumberFormatException e) {
                System.out.println("DNI inválido, se mantiene el anterior.");
            }
        }

        clienteDao.modificarCliente(cliente);
        System.out.println("Cliente modificado correctamente.");
    }

    private static void modificarPrecioProducto() {
        Producto producto = productoDao.obtenerProductoPorId(leerLong("ID del producto: "));
        if (producto == null || !producto.isEstado()) {
            System.out.println("No se encontró un producto activo con ese ID.");
            return;
        }
        System.out.printf("Precio actual de %s: $%.2f%n", producto.getDescripcion(), producto.getPrecioUnitario());
        productoDao.modificarPrecioProducto(producto.getId(), leerDoublePositivo("Nuevo precio: "));
        System.out.println("Precio actualizado.");
    }

    private static void eliminarCliente() {
        Cliente cliente = clienteDao.obtenerClientePorId(leerLong("ID del cliente a eliminar: "));
        if (cliente == null || !cliente.isEstado()) {
            System.out.println("No se encontró un cliente activo con ese ID.");
            return;
        }
        if (confirmar("¿Seguro que desea eliminar a " + cliente.getNombre() + " " + cliente.getApellido() + "? (s/n): ")) {
            clienteDao.eliminarCliente(cliente);
            System.out.println("Cliente eliminado.");
        }
    }

    private static void mostrarFacturas() {
        List<Factura> facturas = facturaDao.obtenerFacturas().stream().filter(Factura::isEstado).toList();
        if (facturas.isEmpty()) {
            System.out.println("No hay facturas registradas.");
            return;
        }
        facturas.forEach(Main::imprimirFactura);
    }

    private static void mostrarClientes() {
        List<Cliente> clientes = clientesActivos();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        clientes.forEach(Main::imprimirCliente);
    }

    private static void mostrarFacturasMayores() {
        List<Factura> facturas = facturaDao.obtenerFacturasMayoresA(900000).stream()
                .filter(Factura::isEstado).toList();
        if (facturas.isEmpty()) {
            System.out.println("No hay facturas que superen los $900.000.");
            return;
        }
        facturas.forEach(Main::imprimirFactura);
    }

    // ===================== AUXILIARES DE LISTADO =====================

    private static List<Cliente> clientesActivos() {
        return clienteDao.obtenerClientes().stream().filter(Cliente::isEstado).toList();
    }

    private static List<Producto> productosActivos() {
        return productoDao.obtenerProductos().stream().filter(Producto::isEstado).toList();
    }

    private static void imprimirCliente(Cliente c) {
        System.out.printf("ID: %d | %s %s | DNI: %d | Domicilio: %s%n",
                c.getId(), c.getNombre(), c.getApellido(), c.getDni(), c.getDomicilio());
    }

    private static void imprimirProducto(Producto p) {
        System.out.printf("ID: %d | %s | $%.2f%n", p.getId(), p.getDescripcion(), p.getPrecioUnitario());
    }

    private static void imprimirFactura(Factura f) {
        System.out.println("\n----------------------------------------");
        System.out.println("Factura N°: " + f.getId());
        System.out.println("Fecha: " + f.getFecha());
        System.out.println("Cliente: " + f.getCliente().getNombre() + " " + f.getCliente().getApellido()
                + " (DNI " + f.getCliente().getDni() + ")");
        System.out.println("Domicilio: " + f.getDomicilio());
        System.out.println("Detalle:");
        for (DetalleFactura d : f.getDetalles()) {
            System.out.printf("  %-25s x%-4d $%,.2f%n",
                    d.getProducto().getDescripcion(), d.getCantidad(), d.getSubtotal());
        }
        System.out.printf("TOTAL: $%,.2f%n", f.getTotal());
        System.out.println("----------------------------------------");
    }

    // ===================== LECTURA SEGURA DE DATOS =====================

    private static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = sc.nextLine().trim();
            if (!texto.isEmpty()) return texto;
            System.out.println("El dato no puede estar vacío.");
        }
    }

    private static String leerOpcional(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero válido.");
            }
        }
    }

    private static int leerEnteroPositivo(String mensaje) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor > 0) return valor;
            System.out.println("El valor debe ser mayor a 0.");
        }
    }

    private static long leerLong(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                long valor = Long.parseLong(sc.nextLine().trim());
                if (valor >= 0) return valor;
                System.out.println("El número no puede ser negativo.");
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
            }
        }
    }

    private static double leerDoublePositivo(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                double valor = Double.parseDouble(sc.nextLine().trim().replace(",", "."));
                if (valor > 0) return valor;
                System.out.println("El valor debe ser mayor a 0.");
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido (ej: 1500.50).");
            }
        }
    }

    private static boolean confirmar(String mensaje) {
        String respuesta = leerTexto(mensaje);
        return respuesta.equalsIgnoreCase("s");
    }
}
