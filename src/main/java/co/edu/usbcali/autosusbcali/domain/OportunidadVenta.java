package co.edu.usbcali.autosusbcali.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "oportunidades_venta")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OportunidadVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehiculo_id", nullable = false)
    private Long vehiculoId;

    @Column(name = "comprador_id", nullable = false)
    private Long compradorId;

    @Column(name = "asesor_id")
    private Long asesorId;

    @Column(name = "estado", length = 30, nullable = false)
    private String estado;

    @Column(name = "origen", length = 20)
    private String origen;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}