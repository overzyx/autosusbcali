package co.edu.usbcali.autosusbcali.service;

import co.edu.usbcali.autosusbcali.domain.Marca;
import co.edu.usbcali.autosusbcali.dto.request.CrearModificarMarcaRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerMarcaResponse;
import co.edu.usbcali.autosusbcali.mapper.MarcaMapper;
import co.edu.usbcali.autosusbcali.repository.MarcaRepository;
import co.edu.usbcali.autosusbcali.repository.ModeloRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MarcaService implements IMarcaService {
    private final MarcaRepository marcaRepository;
    private final ModeloRepository modeloRepository;

    @Override
    public List<ObtenerMarcaResponse> obtenerTodas() {
        log.debug("Consultando todas las marcas");
        return MarcaMapper.listaMarcasAListaObtenerMarcaResponse(marcaRepository.findAll());
    }

    @Override
    public List<ObtenerMarcaResponse> buscarMarcaPorEstado(Boolean activo) {
        log.debug("Consultando marcas con activo = {}", activo);
        List<Marca> marcasPorEstado = marcaRepository.findByActivo(activo);
        return MarcaMapper.listaMarcasAListaObtenerMarcaResponse(marcasPorEstado);
    }

    @Override
    public ObtenerMarcaResponse obtenerMarcaPorNombre(String nombre) {
        log.debug("Buscando marca por nombre {}", nombre);
        Marca marcaPorNombre = marcaRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado la marca por nombre: "+nombre));
        return MarcaMapper.marcaAObtenerMarcaResponse(marcaPorNombre);
    }

    @Override
    public ObtenerMarcaResponse obtenerMarcaPorId(Long id) {
        log.debug("Buscando marca por id {}", id);
        Marca marcaPorId = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado la marca por id "+id));
        return MarcaMapper.marcaAObtenerMarcaResponse(marcaPorId);
    }

    @Override
    public ObtenerMarcaResponse crearMarca(CrearModificarMarcaRequest crearModificarMarcaRequest) throws Exception {
        // Validaciones del objeto #CrearModificarMarcaRequest
        if (crearModificarMarcaRequest == null) {
            throw new Exception("El objeto CrearModificarMarcaRequest no puede ser nulo.");
        }
        if (crearModificarMarcaRequest.nombre() == null
            || crearModificarMarcaRequest.nombre().isBlank()) {
            throw new Exception("El nombre no puede ser nulo ni estar vacío.");
        }

        // Log de entrada
        log.info("Creando marca con nombre {}", crearModificarMarcaRequest.nombre());

        // Validar que no exista otra marca con el mismo nombre
        if (marcaRepository.existsByNombre(crearModificarMarcaRequest.nombre())) {
            log.warn("No se creó la marca: ya existe una con el nombre {}", crearModificarMarcaRequest.nombre());
            throw new Exception("Ya existe una marca con el nombre "+crearModificarMarcaRequest.nombre());
        }

        // Usar mapper para convertir DTO a objeto de Domain
        Marca marca = MarcaMapper.crearModificarMarcaRequestAMarca(crearModificarMarcaRequest);

        // Persistir la Marca en base de datos
        marca = marcaRepository.save(marca);

        // Log de salida
        log.info("Marca {} creada con id {}", marca.getNombre(), marca.getId());

        // Retornar la marca creada como DTO
        return MarcaMapper.marcaAObtenerMarcaResponse(marca);
    }

    @Override
    public ObtenerMarcaResponse modificarMarca(Long id, CrearModificarMarcaRequest modificarMarcaRequest) throws Exception {
        // Validaciones del id
        if (id == null || id <= 0) {
            throw new Exception("El id no puede ser nulo ni menor o igual a cero.");
        }
        // Validaciones del objeto #CrearModificarMarcaRequest
        if (modificarMarcaRequest == null) {
            throw new Exception("El objeto CrearModificarMarcaRequest no puede ser nulo.");
        }
        if (modificarMarcaRequest.nombre() == null
            || modificarMarcaRequest.nombre().isBlank()) {
            throw new Exception("El nombre no puede ser nulo ni estar vacío.");
        }

        // Log de entrada
        log.info("Modificando marca con id {}", id);

        // Buscar la marca que se quiere modificar
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new Exception("No se ha encontrado la marca por id "+id));

        // Validar que el nuevo nombre no lo tenga otra marca distinta
        if (marcaRepository.existsByNombreAndIdNot(modificarMarcaRequest.nombre(), id)) {
            log.warn("No se modificó la marca {}: el nombre {} ya lo tiene otra marca", id, modificarMarcaRequest.nombre());
            throw new Exception("Ya existe otra marca con el nombre "+modificarMarcaRequest.nombre());
        }

        // Actualizar los datos de la marca
        String nombreAnterior = marca.getNombre();
        marca.setNombre(modificarMarcaRequest.nombre());

        // Persistir los cambios en base de datos
        marca = marcaRepository.save(marca);

        // Log de salida
        log.info("Marca {} modificada: nombre {} -> {}", id, nombreAnterior, marca.getNombre());

        // Retornar la marca modificada como DTO
        return MarcaMapper.marcaAObtenerMarcaResponse(marca);
    }

    @Override
    public ObtenerMarcaResponse eliminarMarca(Long id) throws Exception {
        // Validaciones del id
        if (id == null || id <= 0) {
            throw new Exception("El id no puede ser nulo ni menor o igual a cero.");
        }

        // Log de entrada
        log.info("Solicitud de eliminación de la marca con id {}", id);

        // Buscar la marca que se quiere eliminar
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new Exception("No se ha encontrado la marca por id "+id));

        // No permitir eliminar una marca que tenga modelos asociados
        if (modeloRepository.existsByMarcaId(id)) {
            log.warn("No se eliminó la marca {} (id {}): tiene modelos asociados", marca.getNombre(), id);
            throw new Exception("No se puede eliminar la marca "+marca.getNombre()+" porque tiene modelos asociados.");
        }

        // Eliminación física: la fila desaparece de la tabla marcas
        marcaRepository.delete(marca);

        // Log de auditoría: queda constancia de QUÉ se borró
        log.info("AUDITORIA - Marca eliminada físicamente: id={}, nombre={}", marca.getId(), marca.getNombre());

        // Retornar la marca eliminada como DTO
        return MarcaMapper.marcaAObtenerMarcaResponse(marca);
    }
}
