package co.edu.usbcali.autosusbcali.dto.request;

// El cliente envía el id de la marca, no la marca completa
public record CrearModificarModeloRequest(
        String nombre,
        Long marcaId) {
}
