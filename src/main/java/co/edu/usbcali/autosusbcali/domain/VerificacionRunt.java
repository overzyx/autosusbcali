package co.edu.usbcali.autosusbcali.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "verificaciones_runt")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerificacionRunt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehiculo_id", nullable = false)
    private Long vehiculoId;

    @Column(name = "placa", length = 10, nullable = false)
    private String placa;

    @Column(name = "tiene_prendas")
    private Boolean tienePrendas;

    @Column(name = "tiene_embargos")
    private Boolean tieneEmbargos;

    @Column(name = "reporte_hurto")
    private Boolean reporteHurto;

    @Column(name = "estado_legal", length = 50)
    private String estadoLegal;

    @Column(name = "respuesta_raw", columnDefinition = "TEXT")
    private String respuestaRaw;

    @Column(name = "usuario_consulta_id", nullable = false)
    private Long usuarioConsultaId;

    @Column(name = "fecha_consulta", nullable = false)
    private LocalDateTime fechaConsulta;
}