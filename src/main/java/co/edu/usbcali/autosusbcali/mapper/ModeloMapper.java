package co.edu.usbcali.autosusbcali.mapper;

import co.edu.usbcali.autosusbcali.domain.Marca;
import co.edu.usbcali.autosusbcali.domain.Modelo;
import co.edu.usbcali.autosusbcali.dto.request.CrearModificarModeloRequest;
import co.edu.usbcali.autosusbcali.dto.response.ObtenerModeloResponse;

import java.util.List;

public class ModeloMapper {
    // Convierte un Modelo (Entidad de Dominio) a un DTO de respuesta.
    // modelo.getMarca() navega la relación @ManyToOne para sacar los datos de la marca.
    public static ObtenerModeloResponse modeloAObtenerModeloResponse(Modelo modelo) {
        return new ObtenerModeloResponse(
                modelo.getId(),
                modelo.getNombre(),
                modelo.getMarca().getId(),
                modelo.getMarca().getNombre());
    }

    // Convierte una lista de Modelos a una lista de DTOs de respuesta
    public static List<ObtenerModeloResponse> listaModelosAListaObtenerModeloResponse(List<Modelo> modelos) {
        return modelos.stream().map(ModeloMapper::modeloAObtenerModeloResponse).toList();
    }

    // Convierte un Request a un Modelo. Recibe la Marca ya buscada en la base de datos,
    // porque el request solo trae el marcaId.
    public static Modelo crearModificarModeloRequestAModelo(CrearModificarModeloRequest crearModificarModeloRequest,
                                                            Marca marca) {
        return new Modelo(null,
                marca,
                crearModificarModeloRequest.nombre());
    }
}
