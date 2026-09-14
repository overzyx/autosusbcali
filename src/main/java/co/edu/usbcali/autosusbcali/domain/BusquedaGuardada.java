package co.edu.usbcali.autosusbcali.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "busquedas_guardadas")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BusquedaGuardada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "criterios", nullable = false, columnDefinition = "TEXT")
    private String criterios;

    @Column(name = "alerta_activa", nullable = false)
    private Boolean alertaActiva;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
}