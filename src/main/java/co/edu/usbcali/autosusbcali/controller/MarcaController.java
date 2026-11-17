package co.edu.usbcali.autosusbcali.controller;

import co.edu.usbcali.autosusbcali.dto.request.CrearModificarMarcaRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerMarcaResponse;
import co.edu.usbcali.autosusbcali.service.IMarcaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marcas")
@AllArgsConstructor
public class MarcaController {
    private final IMarcaService marcaService;

    @GetMapping
    public List<ObtenerMarcaResponse> obtenerTodas() {
        return marcaService.obtenerTodas();
    }

    @GetMapping("/activas")
    public List<ObtenerMarcaResponse> obtenerTodasActivas() {
        return marcaService.buscarMarcaPorEstado(true);
    }

    @GetMapping("/inactivas")
    public List<ObtenerMarcaResponse> obtenerTodasInactivas() {
        return marcaService.buscarMarcaPorEstado(false);
    }

    @GetMapping("/por-nombre/{nombre}")
    public ResponseEntity<ObtenerMarcaResponse> obtenerPorNombre(@PathVariable String nombre) {
        return new ResponseEntity<>(
                marcaService.obtenerMarcaPorNombre(nombre),
                HttpStatus.OK
        );
    }

    @GetMapping("/por-id/{id}")
    public ResponseEntity<ObtenerMarcaResponse> obtenerPorId(@PathVariable Long id) {
        return new ResponseEntity<>(
                marcaService.obtenerMarcaPorId(id),
                HttpStatus.OK
        );
    }

    @PostMapping("/guardar")
    public ResponseEntity<ObtenerMarcaResponse> crearMarca(
            @RequestBody CrearModificarMarcaRequest crearMarcaRequest) throws Exception {
        return new ResponseEntity<>(
                marcaService.crearMarca(crearMarcaRequest),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<ObtenerMarcaResponse> modificarMarca(
            @PathVariable Long id,
            @RequestBody CrearModificarMarcaRequest modificarMarcaRequest
    ) throws Exception {
        return new ResponseEntity<>(
                marcaService.modificarMarca(id, modificarMarcaRequest),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ObtenerMarcaResponse> eliminarMarca(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(
                marcaService.eliminarMarca(id),
                HttpStatus.OK
        );
    }
}
