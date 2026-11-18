package co.edu.usbcali.autosusbcali.controller;

import co.edu.usbcali.autosusbcali.dto.request.CrearModificarModeloRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerModeloResponse;
import co.edu.usbcali.autosusbcali.service.IModeloService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modelos")
@AllArgsConstructor
public class ModeloController {
    private final IModeloService modeloService;

    @GetMapping
    public List<ObtenerModeloResponse> obtenerTodos() {
        return modeloService.obtenerTodos();
    }

    @GetMapping("/por-id/{id}")
    public ResponseEntity<ObtenerModeloResponse> obtenerPorId(@PathVariable Long id) {
        return new ResponseEntity<>(
                modeloService.obtenerModeloPorId(id),
                HttpStatus.OK
        );
    }

    @GetMapping("/por-marca/{marcaId}")
    public List<ObtenerModeloResponse> buscarPorMarcaId(@PathVariable Long marcaId) {
        return modeloService.buscarModelosPorMarcaId(marcaId);
    }

    @GetMapping("/por-nombre-marca/{nombreMarca}")
    public List<ObtenerModeloResponse> buscarPorNombreMarca(@PathVariable String nombreMarca) {
        return modeloService.buscarModelosPorNombreMarca(nombreMarca);
    }

    @PostMapping("/guardar")
    public ResponseEntity<ObtenerModeloResponse> crearModelo(
            @RequestBody CrearModificarModeloRequest crearModeloRequest) throws Exception {
        return new ResponseEntity<>(
                modeloService.crearModelo(crearModeloRequest),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/modificar/{id}")
    public ResponseEntity<ObtenerModeloResponse> modificarModelo(
            @PathVariable Long id,
            @RequestBody CrearModificarModeloRequest modificarModeloRequest
    ) throws Exception {
        return new ResponseEntity<>(
                modeloService.modificarModelo(id, modificarModeloRequest),
                HttpStatus.OK
        );
    }

    // 204 No Content: se eliminó y no hay nada que devolver en el cuerpo
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarModelo(@PathVariable Long id) throws Exception {
        modeloService.eliminarModelo(id);
        return ResponseEntity.noContent().build();
    }
}
