package co.edu.usbcali.autosusbcali.dto.response;

// Incluye datos de la marca: vienen de la relación (JOIN) entre modelos y marcas
public record ObtenerModeloResponse(
        Long id,
        String nombre,
        Long marcaId,
        String marcaNombre) {
}
