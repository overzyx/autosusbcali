package co.edu.usbcali.autosusbcali.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehiculo_id", nullable = false, unique = true)
    private Long vehiculoId;

    @Column(name = "oportunidad_id", nullable = false)
    private Long oportunidadId;

    @Column(name = "comprador_id", nullable = false)
    private Long compradorId;

    @Column(name = "asesor_id")
    private Long asesorId;

    @Column(name = "monto_reserva", precision = 12, scale = 2)
    private BigDecimal montoReserva;

    @Column(name = "vigencia_hasta", nullable = false)
    private LocalDateTime vigenciaHasta;

    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
}