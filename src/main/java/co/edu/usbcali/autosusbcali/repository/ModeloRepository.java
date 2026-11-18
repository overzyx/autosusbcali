package co.edu.usbcali.autosusbcali.repository;

import co.edu.usbcali.autosusbcali.domain.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {
    Boolean existsByMarcaId(Long marcaId);

    // JOIN explícito con JPQL: trae cada modelo junto con su marca en UNA sola consulta
    @Query("SELECT mo FROM Modelo mo JOIN FETCH mo.marca ORDER BY mo.id")
    List<Modelo> buscarTodosConMarca();

    // Filtra por la llave foránea marca_id
    List<Modelo> findByMarcaId(Long marcaId);

    // JOIN implícito: Spring navega modelo.marca.nombre y genera el JOIN con la tabla marcas
    List<Modelo> findByMarcaNombre(String nombreMarca);

    // Para validar nombres repetidos DENTRO de la misma marca
    Boolean existsByMarcaIdAndNombre(Long marcaId, String nombre);
    Boolean existsByMarcaIdAndNombreAndIdNot(Long marcaId, String nombre, Long id);
}
