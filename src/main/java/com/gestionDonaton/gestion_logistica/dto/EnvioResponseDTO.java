package com.gestionDonaton.gestion_logistica.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class EnvioResponseDTO {
    private Long id;
    private String centroAcopioOrigen;
    private Long donacionId;
    private String destino;
    private String tipoTransporte;
    private String estado;
    private LocalDateTime fechaCreacion;
}