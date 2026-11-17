package co.edu.usbcali.autosusbcali.mapper;

import co.edu.usbcali.autosusbcali.domain.Marca;
import co.edu.usbcali.autosusbcali.dto.request.CrearModificarMarcaRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerMarcaResponse;

import java.util.List;

public class MarcaMapper {
    // Convierte una Marca (Entidad de Dominio) a un DTO de respuesta
    public static ObtenerMarcaResponse marcaAObtenerMarcaResponse(Marca marca) {
        return new ObtenerMarcaResponse(marca.getId(), marca.getNombre());
    }

    // Convierte una lista de Marcas a una lista de DTOs de respuesta
    public static List<ObtenerMarcaResponse> listaMarcasAListaObtenerMarcaResponse(List<Marca> marcas) {
        return marcas.stream().map(MarcaMapper::marcaAObtenerMarcaResponse).toList();
    }

    // Convierte un Request de Crear Marca a una Marca (Entidad de Dominio)
    public static Marca crearModificarMarcaRequestAMarca(CrearModificarMarcaRequest crearModificarMarcaRequest) {
        return new Marca(null,
                crearModificarMarcaRequest.nombre(),
                true);
    }
}
