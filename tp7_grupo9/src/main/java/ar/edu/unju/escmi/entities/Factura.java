
package ar.edu.unju.escmi.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private String domicilio;
    private double total;
    private boolean estado;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL,
               fetch = FetchType.LAZY)
    private List<DetalleFactura> detalles = new ArrayList<>();

    public Factura() {
        this.fecha = LocalDate.now();
        this.estado = true;
    }

    public Factura(Cliente cliente) {
        this();
        this.cliente = cliente;

        if (cliente != null) {
            this.domicilio = cliente.getDomicilio();
        }
    }

    public void agregarDetalle(DetalleFactura detalle) {
        if (detalle != null) {
            detalle.setFactura(this);
            detalles.add(detalle);
            calcularTotal();
        }
    }

    public void calcularTotal() {
        total = 0;

        for (DetalleFactura detalle : detalles) {
            detalle.calcularSubtotal();
            total += detalle.getSubtotal();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;

        if (cliente != null) {
            this.domicilio = cliente.getDomicilio();
        }
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles;

        for (DetalleFactura detalle : detalles) {
            detalle.setFactura(this);
        }

        calcularTotal();
    }
}
