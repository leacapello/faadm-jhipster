package com.verynex.faadm.service;

import com.verynex.faadm.domain.Factura;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Factura}.
 */
public interface FacturaService {

    /**
     * Save a factura.
     *
     * @param factura the entity to save.
     * @return the persisted entity.
     */
    Factura save(Factura factura);

    /**
     * Get all the facturas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Factura> findAll(Pageable pageable);

    /**
     * Get the "id" factura.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Factura> findOne(Long id);

    /**
     * Delete the "id" factura.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
