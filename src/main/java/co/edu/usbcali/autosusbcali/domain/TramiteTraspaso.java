package co.edu.usbcali.autosusbcali.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tramites_traspaso")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TramiteTraspaso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "venta_id", nullable = false)
    private Long ventaId;

    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "usuario_gestiona_id")
    private Long usuarioGestionaId;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_completado")
    private LocalDateTime fechaCompletado;
}