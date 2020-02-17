package com.verynex.faadm.web.rest;

import com.verynex.faadm.FaadmApp;
import com.verynex.faadm.domain.Factura;
import com.verynex.faadm.repository.FacturaRepository;
import com.verynex.faadm.service.FacturaService;
import com.verynex.faadm.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.verynex.faadm.web.rest.TestUtil.sameInstant;
import static com.verynex.faadm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FacturaResource} REST controller.
 */
@SpringBootTest(classes = FaadmApp.class)
public class FacturaResourceIT {

    private static final String DEFAULT_IDENTIFICACION = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICACION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FECHA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Float DEFAULT_MONTO = 1F;
    private static final Float UPDATED_MONTO = 2F;

    private static final ZonedDateTime DEFAULT_FECHA_VENCIMIENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FECHA_VENCIMIENTO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_FACTURAD_REALIZADA = false;
    private static final Boolean UPDATED_FACTURAD_REALIZADA = true;

    private static final Boolean DEFAULT_FACTURA_ENVIADA = false;
    private static final Boolean UPDATED_FACTURA_ENVIADA = true;

    private static final Boolean DEFAULT_FACTURA_COBRADA = false;
    private static final Boolean UPDATED_FACTURA_COBRADA = true;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restFacturaMockMvc;

    private Factura factura;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FacturaResource facturaResource = new FacturaResource(facturaService);
        this.restFacturaMockMvc = MockMvcBuilders.standaloneSetup(facturaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factura createEntity(EntityManager em) {
        Factura factura = new Factura()
            .identificacion(DEFAULT_IDENTIFICACION)
            .fecha(DEFAULT_FECHA)
            .monto(DEFAULT_MONTO)
            .fechaVencimiento(DEFAULT_FECHA_VENCIMIENTO)
            .facturadRealizada(DEFAULT_FACTURAD_REALIZADA)
            .facturaEnviada(DEFAULT_FACTURA_ENVIADA)
            .facturaCobrada(DEFAULT_FACTURA_COBRADA);
        return factura;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factura createUpdatedEntity(EntityManager em) {
        Factura factura = new Factura()
            .identificacion(UPDATED_IDENTIFICACION)
            .fecha(UPDATED_FECHA)
            .monto(UPDATED_MONTO)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .facturadRealizada(UPDATED_FACTURAD_REALIZADA)
            .facturaEnviada(UPDATED_FACTURA_ENVIADA)
            .facturaCobrada(UPDATED_FACTURA_COBRADA);
        return factura;
    }

    @BeforeEach
    public void initTest() {
        factura = createEntity(em);
    }

    @Test
    @Transactional
    public void createFactura() throws Exception {
        int databaseSizeBeforeCreate = facturaRepository.findAll().size();

        // Create the Factura
        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isCreated());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeCreate + 1);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getIdentificacion()).isEqualTo(DEFAULT_IDENTIFICACION);
        assertThat(testFactura.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testFactura.getMonto()).isEqualTo(DEFAULT_MONTO);
        assertThat(testFactura.getFechaVencimiento()).isEqualTo(DEFAULT_FECHA_VENCIMIENTO);
        assertThat(testFactura.isFacturadRealizada()).isEqualTo(DEFAULT_FACTURAD_REALIZADA);
        assertThat(testFactura.isFacturaEnviada()).isEqualTo(DEFAULT_FACTURA_ENVIADA);
        assertThat(testFactura.isFacturaCobrada()).isEqualTo(DEFAULT_FACTURA_COBRADA);
    }

    @Test
    @Transactional
    public void createFacturaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = facturaRepository.findAll().size();

        // Create the Factura with an existing ID
        factura.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkIdentificacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setIdentificacion(null);

        // Create the Factura, which fails.

        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setFecha(null);

        // Create the Factura, which fails.

        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontoIsRequired() throws Exception {
        int databaseSizeBeforeTest = facturaRepository.findAll().size();
        // set the field null
        factura.setMonto(null);

        // Create the Factura, which fails.

        restFacturaMockMvc.perform(post("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isBadRequest());

        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFacturas() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList
        restFacturaMockMvc.perform(get("/api/facturas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factura.getId().intValue())))
            .andExpect(jsonPath("$.[*].identificacion").value(hasItem(DEFAULT_IDENTIFICACION)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(sameInstant(DEFAULT_FECHA))))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO.doubleValue())))
            .andExpect(jsonPath("$.[*].fechaVencimiento").value(hasItem(sameInstant(DEFAULT_FECHA_VENCIMIENTO))))
            .andExpect(jsonPath("$.[*].facturadRealizada").value(hasItem(DEFAULT_FACTURAD_REALIZADA.booleanValue())))
            .andExpect(jsonPath("$.[*].facturaEnviada").value(hasItem(DEFAULT_FACTURA_ENVIADA.booleanValue())))
            .andExpect(jsonPath("$.[*].facturaCobrada").value(hasItem(DEFAULT_FACTURA_COBRADA.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get the factura
        restFacturaMockMvc.perform(get("/api/facturas/{id}", factura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factura.getId().intValue()))
            .andExpect(jsonPath("$.identificacion").value(DEFAULT_IDENTIFICACION))
            .andExpect(jsonPath("$.fecha").value(sameInstant(DEFAULT_FECHA)))
            .andExpect(jsonPath("$.monto").value(DEFAULT_MONTO.doubleValue()))
            .andExpect(jsonPath("$.fechaVencimiento").value(sameInstant(DEFAULT_FECHA_VENCIMIENTO)))
            .andExpect(jsonPath("$.facturadRealizada").value(DEFAULT_FACTURAD_REALIZADA.booleanValue()))
            .andExpect(jsonPath("$.facturaEnviada").value(DEFAULT_FACTURA_ENVIADA.booleanValue()))
            .andExpect(jsonPath("$.facturaCobrada").value(DEFAULT_FACTURA_COBRADA.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFactura() throws Exception {
        // Get the factura
        restFacturaMockMvc.perform(get("/api/facturas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFactura() throws Exception {
        // Initialize the database
        facturaService.save(factura);

        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Update the factura
        Factura updatedFactura = facturaRepository.findById(factura.getId()).get();
        // Disconnect from session so that the updates on updatedFactura are not directly saved in db
        em.detach(updatedFactura);
        updatedFactura
            .identificacion(UPDATED_IDENTIFICACION)
            .fecha(UPDATED_FECHA)
            .monto(UPDATED_MONTO)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .facturadRealizada(UPDATED_FACTURAD_REALIZADA)
            .facturaEnviada(UPDATED_FACTURA_ENVIADA)
            .facturaCobrada(UPDATED_FACTURA_COBRADA);

        restFacturaMockMvc.perform(put("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFactura)))
            .andExpect(status().isOk());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getIdentificacion()).isEqualTo(UPDATED_IDENTIFICACION);
        assertThat(testFactura.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testFactura.getMonto()).isEqualTo(UPDATED_MONTO);
        assertThat(testFactura.getFechaVencimiento()).isEqualTo(UPDATED_FECHA_VENCIMIENTO);
        assertThat(testFactura.isFacturadRealizada()).isEqualTo(UPDATED_FACTURAD_REALIZADA);
        assertThat(testFactura.isFacturaEnviada()).isEqualTo(UPDATED_FACTURA_ENVIADA);
        assertThat(testFactura.isFacturaCobrada()).isEqualTo(UPDATED_FACTURA_COBRADA);
    }

    @Test
    @Transactional
    public void updateNonExistingFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Create the Factura

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaMockMvc.perform(put("/api/facturas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(factura)))
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFactura() throws Exception {
        // Initialize the database
        facturaService.save(factura);

        int databaseSizeBeforeDelete = facturaRepository.findAll().size();

        // Delete the factura
        restFacturaMockMvc.perform(delete("/api/facturas/{id}", factura.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
