package co.edu.usbcali.autosusbcali.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pqrs")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pqrs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_radicado", length = 30, nullable = false, unique = true)
    private String numeroRadicado;

    @Column(name = "comprador_id", nullable = false)
    private Long compradorId;

    @Column(name = "tipo", length = 20, nullable = false)
    private String tipo;

    @Column(name = "asunto", length = 200, nullable = false)
    private String asunto;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "venta_id")
    private Long ventaId;

    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    @Column(name = "usuario_asignado_id")
    private Long usuarioAsignadoId;

    @Column(name = "respuesta")
    private String respuesta;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;
}