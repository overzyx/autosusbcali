package co.edu.usbcali.autosusbcali.service;

import co.edu.usbcali.autosusbcali.dto.request.CrearModificarMarcaRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerMarcaResponse;

import java.util.List;

public interface IMarcaService {
    // Busca todas las marcas sin importar su estado
    List<ObtenerMarcaResponse> obtenerTodas();

    // Buscamos las marcas por estado (ya sea activo o inactivo)
    List<ObtenerMarcaResponse> buscarMarcaPorEstado(Boolean activo);

    ObtenerMarcaResponse obtenerMarcaPorNombre(String nombre);

    ObtenerMarcaResponse obtenerMarcaPorId(Long id);

    // Método para crear una marca
    ObtenerMarcaResponse crearMarca(CrearModificarMarcaRequest crearModificarMarcaRequest) throws Exception;

    // Método para modificar una marca
    ObtenerMarcaResponse modificarMarca(Long id, CrearModificarMarcaRequest modificarMarcaRequest) throws Exception;

    // Método para eliminar una marca (eliminación física)
    ObtenerMarcaResponse eliminarMarca(Long id) throws Exception;
}
