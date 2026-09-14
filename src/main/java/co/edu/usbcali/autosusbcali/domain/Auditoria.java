package co.edu.usbcali.autosusbcali.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "accion", length = 100, nullable = false)
    private String accion;

    @Column(name = "entidad", length = 50, nullable = false)
    private String entidad;

    @Column(name = "entidad_id")
    private Long entidadId;

    @Column(name = "datos_anteriores", columnDefinition = "TEXT")
    private String datosAnteriores;

    @Column(name = "datos_nuevos", columnDefinition = "TEXT")
    private String datosNuevos;

    @Column(name = "ip_origen", length = 45)
    private String ipOrigen;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;
}