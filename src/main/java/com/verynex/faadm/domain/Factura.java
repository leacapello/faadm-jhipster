package com.verynex.faadm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Factura.
 */
@Entity
@Table(name = "factura")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "identificacion", nullable = false, unique = true)
    private String identificacion;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private ZonedDateTime fecha;

    @NotNull
    @Column(name = "monto", nullable = false)
    private Float monto;

    @Column(name = "fecha_vencimiento")
    private ZonedDateTime fechaVencimiento;

    @Column(name = "facturad_realizada")
    private Boolean facturadRealizada;

    @Column(name = "factura_enviada")
    private Boolean facturaEnviada;

    @Column(name = "factura_cobrada")
    private Boolean facturaCobrada;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public Factura identificacion(String identificacion) {
        this.identificacion = identificacion;
        return this;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public ZonedDateTime getFecha() {
        return fecha;
    }

    public Factura fecha(ZonedDateTime fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(ZonedDateTime fecha) {
        this.fecha = fecha;
    }

    public Float getMonto() {
        return monto;
    }

    public Factura monto(Float monto) {
        this.monto = monto;
        return this;
    }

    public void setMonto(Float monto) {
        this.monto = monto;
    }

    public ZonedDateTime getFechaVencimiento() {
        return fechaVencimiento;
    }

    public Factura fechaVencimiento(ZonedDateTime fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
        return this;
    }

    public void setFechaVencimiento(ZonedDateTime fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Boolean isFacturadRealizada() {
        return facturadRealizada;
    }

    public Factura facturadRealizada(Boolean facturadRealizada) {
        this.facturadRealizada = facturadRealizada;
        return this;
    }

    public void setFacturadRealizada(Boolean facturadRealizada) {
        this.facturadRealizada = facturadRealizada;
    }

    public Boolean isFacturaEnviada() {
        return facturaEnviada;
    }

    public Factura facturaEnviada(Boolean facturaEnviada) {
        this.facturaEnviada = facturaEnviada;
        return this;
    }

    public void setFacturaEnviada(Boolean facturaEnviada) {
        this.facturaEnviada = facturaEnviada;
    }

    public Boolean isFacturaCobrada() {
        return facturaCobrada;
    }

    public Factura facturaCobrada(Boolean facturaCobrada) {
        this.facturaCobrada = facturaCobrada;
        return this;
    }

    public void setFacturaCobrada(Boolean facturaCobrada) {
        this.facturaCobrada = facturaCobrada;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Factura)) {
            return false;
        }
        return id != null && id.equals(((Factura) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Factura{" +
            "id=" + getId() +
            ", identificacion='" + getIdentificacion() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", monto=" + getMonto() +
            ", fechaVencimiento='" + getFechaVencimiento() + "'" +
            ", facturadRealizada='" + isFacturadRealizada() + "'" +
            ", facturaEnviada='" + isFacturaEnviada() + "'" +
            ", facturaCobrada='" + isFacturaCobrada() + "'" +
            "}";
    }
}
