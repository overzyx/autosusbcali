package co.edu.usbcali.autosusbcali.service;

import co.edu.usbcali.autosusbcali.domain.Marca;
import co.edu.usbcali.autosusbcali.domain.Modelo;
import co.edu.usbcali.autosusbcali.dto.request.CrearModificarModeloRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerModeloResponse;
import co.edu.usbcali.autosusbcali.mapper.ModeloMapper;
import co.edu.usbcali.autosusbcali.repository.MarcaRepository;
import co.edu.usbcali.autosusbcali.repository.ModeloRepository;
import co.edu.usbcali.autosusbcali.repository.VehiculoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ModeloService implements IModeloService {
    private final ModeloRepository modeloRepository;
    private final MarcaRepository marcaRepository;
    private final VehiculoRepository vehiculoRepository;

    @Override
    public List<ObtenerModeloResponse> obtenerTodos() {
        log.debug("Consultando todos los modelos con su marca");
        // JOIN FETCH: una sola consulta trae modelos y marcas
        return ModeloMapper.listaModelosAListaObtenerModeloResponse(modeloRepository.buscarTodosConMarca());
    }

    @Override
    public ObtenerModeloResponse obtenerModeloPorId(Long id) {
        log.debug("Buscando modelo por id {}", id);
        Modelo modeloPorId = modeloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado el modelo por id "+id));
        return ModeloMapper.modeloAObtenerModeloResponse(modeloPorId);
    }

    @Override
    public List<ObtenerModeloResponse> buscarModelosPorMarcaId(Long marcaId) {
        log.debug("Buscando modelos de la marca con id {}", marcaId);
        if (!marcaRepository.existsById(marcaId)) {
            throw new RuntimeException("No se ha encontrado la marca por id "+marcaId);
        }
        List<Modelo> modelos = modeloRepository.findByMarcaId(marcaId);
        return ModeloMapper.listaModelosAListaObtenerModeloResponse(modelos);
    }

    @Override
    public List<ObtenerModeloResponse> buscarModelosPorNombreMarca(String nombreMarca) {
        log.debug("Buscando modelos de la marca {}", nombreMarca);
        List<Modelo> modelos = modeloRepository.findByMarcaNombre(nombreMarca);
        return ModeloMapper.listaModelosAListaObtenerModeloResponse(modelos);
    }

    @Override
    public ObtenerModeloResponse crearModelo(CrearModificarModeloRequest crearModificarModeloRequest) throws Exception {
        // Validaciones del objeto #CrearModificarModeloRequest
        validarRequest(crearModificarModeloRequest);

        // Log de entrada
        log.info("Creando modelo {} para la marca con id {}",
                crearModificarModeloRequest.nombre(), crearModificarModeloRequest.marcaId());

        // Validar que la marca exista y esté activa (la llave foránea debe apuntar a algo válido)
        Marca marca = buscarMarcaActiva(crearModificarModeloRequest.marcaId());

        // Validar que la marca no tenga ya un modelo con ese nombre
        if (modeloRepository.existsByMarcaIdAndNombre(marca.getId(), crearModificarModeloRequest.nombre())) {
            log.warn("No se creó el modelo: la marca {} ya tiene un modelo llamado {}",
                    marca.getNombre(), crearModificarModeloRequest.nombre());
            throw new Exception("La marca "+marca.getNombre()+" ya tiene un modelo con el nombre "
                    +crearModificarModeloRequest.nombre());
        }

        // Usar mapper para convertir DTO a objeto de Domain (con la marca ya buscada)
        Modelo modelo = ModeloMapper.crearModificarModeloRequestAModelo(crearModificarModeloRequest, marca);

        // Persistir el Modelo en base de datos
        modelo = modeloRepository.save(modelo);

        // Log de salida
        log.info("Modelo {} creado con id {} para la marca {}", modelo.getNombre(), modelo.getId(), marca.getNombre());

        // Retornar el modelo creado como DTO
        return ModeloMapper.modeloAObtenerModeloResponse(modelo);
    }

    @Override
    public ObtenerModeloResponse modificarModelo(Long id, CrearModificarModeloRequest modificarModeloRequest) throws Exception {
        // Validaciones del id
        if (id == null || id <= 0) {
            throw new Exception("El id no puede ser nulo ni menor o igual a cero.");
        }
        // Validaciones del objeto #CrearModificarModeloRequest
        validarRequest(modificarModeloRequest);

        // Log de entrada
        log.info("Modificando modelo con id {}", id);

        // Buscar el modelo que se quiere modificar
        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new Exception("No se ha encontrado el modelo por id "+id));

        // Validar la marca nueva (se puede cambiar el modelo de marca)
        Marca marca = buscarMarcaActiva(modificarModeloRequest.marcaId());

        // Validar que otro modelo de esa marca no tenga ya ese nombre
        if (modeloRepository.existsByMarcaIdAndNombreAndIdNot(marca.getId(), modificarModeloRequest.nombre(), id)) {
            log.warn("No se modificó el modelo {}: la marca {} ya tiene otro modelo llamado {}",
                    id, marca.getNombre(), modificarModeloRequest.nombre());
            throw new Exception("La marca "+marca.getNombre()+" ya tiene otro modelo con el nombre "
                    +modificarModeloRequest.nombre());
        }

        // Actualizar los datos del modelo
        String descripcionAnterior = modelo.getNombre()+" ("+modelo.getMarca().getNombre()+")";
        modelo.setNombre(modificarModeloRequest.nombre());
        modelo.setMarca(marca);

        // Persistir los cambios en base de datos
        modelo = modeloRepository.save(modelo);

        // Log de salida
        log.info("Modelo {} modificado: {} -> {} ({})", id, descripcionAnterior, modelo.getNombre(), marca.getNombre());

        // Retornar el modelo modificado como DTO
        return ModeloMapper.modeloAObtenerModeloResponse(modelo);
    }

    @Override
    public void eliminarModelo(Long id) throws Exception {

        if (id == null || id <= 0) {
            throw new Exception("El id no puede ser nulo ni menor o igual a cero.");
        }

        // Log de entrada
        log.info("Solicitud de eliminación del modelo con id {}", id);


        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new Exception("No se ha encontrado el modelo por id "+id));

        // No permitir eliminar un modelo que tenga vehículos asociados
        if (vehiculoRepository.existsByModeloId(id)) {
            log.warn("No se eliminó el modelo {} (id {}): tiene vehículos asociados", modelo.getNombre(), id);
            throw new Exception("No se puede eliminar el modelo "+modelo.getNombre()+" porque tiene vehículos asociados.");
        }


        modeloRepository.delete(modelo);

        // Log de auditoría: queda constancia de QUÉ se borró
        log.info("AUDITORIA - Modelo eliminado físicamente: id={}, nombre={}, marca={}",
                modelo.getId(), modelo.getNombre(), modelo.getMarca().getNombre());
    }

    // Validaciones comunes de crear y modificar
    private void validarRequest(CrearModificarModeloRequest request) throws Exception {
        if (request == null) {
            throw new Exception("El objeto CrearModificarModeloRequest no puede ser nulo.");
        }
        if (request.nombre() == null || request.nombre().isBlank()) {
            throw new Exception("El nombre no puede ser nulo ni estar vacío.");
        }
        if (request.marcaId() == null || request.marcaId() <= 0) {
            throw new Exception("El id de la marca no puede ser nulo ni menor o igual a cero.");
        }
    }

    // Busca la marca y verifica que esté activa
    private Marca buscarMarcaActiva(Long marcaId) throws Exception {
        Marca marca = marcaRepository.findById(marcaId)
                .orElseThrow(() -> new Exception("No se ha encontrado la marca por id "+marcaId));
        if (!marca.getActivo()) {
            throw new Exception("La marca "+marca.getNombre()+" está inactiva.");
        }
        return marca;
    }
}
