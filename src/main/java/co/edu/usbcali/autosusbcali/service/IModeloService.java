package co.edu.usbcali.autosusbcali.service;

import co.edu.usbcali.autosusbcali.dto.request.CrearModificarModeloRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerModeloResponse;

import java.util.List;

public interface IModeloService {
    // Busca todos los modelos con su marca
    List<ObtenerModeloResponse> obtenerTodos();

    ObtenerModeloResponse obtenerModeloPorId(Long id);

    // Busca los modelos de una marca por el id de la marca
    List<ObtenerModeloResponse> buscarModelosPorMarcaId(Long marcaId);

    // Busca los modelos de una marca por el nombre de la marca
    List<ObtenerModeloResponse> buscarModelosPorNombreMarca(String nombreMarca);

    // Método para crear un modelo
    ObtenerModeloResponse crearModelo(CrearModificarModeloRequest crearModificarModeloRequest) throws Exception;

    // Método para modificar un modelo
    ObtenerModeloResponse modificarModelo(Long id, CrearModificarModeloRequest modificarModeloRequest) throws Exception;

    // Método para eliminar un modelo (eliminación física)
    void eliminarModelo(Long id) throws Exception;
}
