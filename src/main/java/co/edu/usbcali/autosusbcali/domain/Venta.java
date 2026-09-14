package co.edu.usbcali.autosusbcali.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventas")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_venta", length = 30, nullable = false, unique = true)
    private String numeroVenta;

    @Column(name = "oportunidad_id", nullable = false, unique = true)
    private Long oportunidadId;

    @Column(name = "vehiculo_id", nullable = false)
    private Long vehiculoId;

    @Column(name = "comprador_id", nullable = false)
    private Long compradorId;

    @Column(name = "asesor_id", nullable = false)
    private Long asesorId;

    @Column(name = "precio_final", nullable = false, precision = 15, scale = 2)
    private BigDecimal precioFinal;

    @Column(name = "forma_pago", length = 10, nullable = false)
    private String formaPago;

    @Column(name = "solicitud_credito_id")
    private Long solicitudCreditoId;

    @Column(name = "estado", length = 20, nullable = false)
    private String estado;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
}