package co.edu.usbcali.autosusbcali.controller;

import co.edu.usbcali.autosusbcali.domain.Marca;
import co.edu.usbcali.autosusbcali.domain.Modelo;
import co.edu.usbcali.autosusbcali.repository.MarcaRepository;
import co.edu.usbcali.autosusbcali.repository.ModeloRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * @Transactional en el test: al terminar cada prueba se hace rollback,
 * así no queda basura en la base de datos servicios_db.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ModeloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private ModeloRepository modeloRepository;

    private Long idMarca(String nombre) {
        return marcaRepository.findByNombre(nombre).orElseThrow().getId();
    }

    @Test
    void listarModelosTraeLosDatosDeLaMarca() throws Exception {
        mockMvc.perform(get("/api/modelos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marcaNombre").exists());
    }

    @Test
    void buscarPorNombreDeMarcaSoloTraeModelosDeEsaMarca() throws Exception {
        mockMvc.perform(get("/api/modelos/por-nombre-marca/{nombre}", "Toyota"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].marcaNombre", everyItem(is("Toyota"))));
    }

    @Test
    void crearModeloDevuelve201ConLaMarca() throws Exception {
        Long idToyota = idMarca("Toyota");

        mockMvc.perform(post("/api/modelos/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"MODELO TEST\", \"marcaId\": " + idToyota + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("MODELO TEST"))
                .andExpect(jsonPath("$.marcaNombre").value("Toyota"));
    }

    @Test
    void crearModeloConMarcaInexistenteFalla() {
        assertThatThrownBy(() -> mockMvc.perform(post("/api/modelos/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"MODELO TEST\", \"marcaId\": 999999}")))
                .hasRootCauseMessage("No se ha encontrado la marca por id 999999");
    }

    @Test
    void crearModeloRepetidoEnLaMismaMarcaFalla() {
        Long idToyota = idMarca("Toyota");

        assertThatThrownBy(() -> mockMvc.perform(post("/api/modelos/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"Corolla\", \"marcaId\": " + idToyota + "}")))
                .hasRootCauseMessage("La marca Toyota ya tiene un modelo con el nombre Corolla");
    }

    @Test
    void modificarModeloCambiandoDeMarca() throws Exception {
        Marca toyota = marcaRepository.findByNombre("Toyota").orElseThrow();
        Modelo modelo = modeloRepository.save(new Modelo(null, toyota, "MODELO TEST"));

        mockMvc.perform(put("/api/modelos/modificar/{id}", modelo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"MODELO MODIFICADO\", \"marcaId\": " + idMarca("Mazda") + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("MODELO MODIFICADO"))
                .andExpect(jsonPath("$.marcaNombre").value("Mazda"));
    }

    @Test
    void eliminarModeloSinVehiculosLoBorraDeLaBaseDeDatos() throws Exception {
        Marca toyota = marcaRepository.findByNombre("Toyota").orElseThrow();
        Modelo modelo = modeloRepository.save(new Modelo(null, toyota, "MODELO TEST"));

        mockMvc.perform(delete("/api/modelos/eliminar/{id}", modelo.getId()))
                .andExpect(status().isNoContent());

        assertThat(modeloRepository.existsById(modelo.getId())).isFalse();
    }

    @Test
    void eliminarModeloConVehiculosFalla() {
        // Tracker (id 1) tiene un vehículo asociado
        assertThatThrownBy(() -> mockMvc.perform(delete("/api/modelos/eliminar/{id}", 1)))
                .hasRootCauseMessage("No se puede eliminar el modelo Tracker porque tiene vehículos asociados.");
    }
}
