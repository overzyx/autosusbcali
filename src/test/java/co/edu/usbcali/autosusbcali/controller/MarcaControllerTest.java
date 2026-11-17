package co.edu.usbcali.autosusbcali.controller;

import co.edu.usbcali.autosusbcali.domain.Marca;
import co.edu.usbcali.autosusbcali.repository.MarcaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * @Transactional en el test: al terminar cada prueba se hace rollback,
 * así no queda basura en la base de datos servicios_db.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MarcaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MarcaRepository marcaRepository;

    @Test
    void crearMarcaDevuelve201() throws Exception {
        mockMvc.perform(post("/api/marcas/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"MARCA TEST\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("MARCA TEST"));

        assertThat(marcaRepository.findByNombre("MARCA TEST").orElseThrow().getActivo()).isTrue();
    }

    @Test
    void crearMarcaConNombreRepetidoFalla() {
        assertThatThrownBy(() -> mockMvc.perform(post("/api/marcas/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"Chevrolet\"}")))
                .hasRootCauseMessage("Ya existe una marca con el nombre Chevrolet");
    }

    @Test
    void modificarMarcaDevuelve200() throws Exception {
        Marca marca = marcaRepository.save(new Marca(null, "MARCA TEST", true));

        mockMvc.perform(put("/api/marcas/modificar/{id}", marca.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"MARCA MODIFICADA\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("MARCA MODIFICADA"));
    }

    @Test
    void eliminarMarcaSinModelosLaBorraDeLaBaseDeDatos() throws Exception {
        Marca marca = marcaRepository.save(new Marca(null, "MARCA TEST", true));

        mockMvc.perform(delete("/api/marcas/eliminar/{id}", marca.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("MARCA TEST"));

        assertThat(marcaRepository.existsById(marca.getId())).isFalse();
    }

    @Test
    void eliminarMarcaConModelosFalla() {
        Long idChevrolet = marcaRepository.findByNombre("Chevrolet").orElseThrow().getId();

        assertThatThrownBy(() -> mockMvc.perform(delete("/api/marcas/eliminar/{id}", idChevrolet)))
                .hasRootCauseMessage("No se puede eliminar la marca Chevrolet porque tiene modelos asociados.");
    }
}
