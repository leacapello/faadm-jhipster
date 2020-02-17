package com.verynex.faadm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.verynex.faadm.domain.enumeration.IdentificacionTipo;

/**
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 40)
    @Column(name = "descripcion", nullable = false, unique = true)
    private String descripcion;

    @NotNull
    @Size(min = 5)
    @Column(name = "identificacion", nullable = false, unique = true)
    private String identificacion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "identificacion_tipo", nullable = false)
    private IdentificacionTipo identificacionTipo;

    @OneToOne
    @JoinColumn(unique = true)
    private Direccion direccion;

    @ManyToOne
    @JsonIgnoreProperties("clientes")
    private Factura factura;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Cliente descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public Cliente identificacion(String identificacion) {
        this.identificacion = identificacion;
        return this;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public IdentificacionTipo getIdentificacionTipo() {
        return identificacionTipo;
    }

    public Cliente identificacionTipo(IdentificacionTipo identificacionTipo) {
        this.identificacionTipo = identificacionTipo;
        return this;
    }

    public void setIdentificacionTipo(IdentificacionTipo identificacionTipo) {
        this.identificacionTipo = identificacionTipo;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public Cliente direccion(Direccion direccion) {
        this.direccion = direccion;
        return this;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Factura getFactura() {
        return factura;
    }

    public Cliente factura(Factura factura) {
        this.factura = factura;
        return this;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        return id != null && id.equals(((Cliente) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", descripcion='" + getDescripcion() + "'" +
            ", identificacion='" + getIdentificacion() + "'" +
            ", identificacionTipo='" + getIdentificacionTipo() + "'" +
            "}";
    }
}
