package com.gestionDonaton.gestion_logistica.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvioResponseDTO {
    private Long id;
    private String centroAcopioOrigen;
    private Long donacionId;
    private String destino;
    private String tipoTransporte;
    private String estado;
    private LocalDateTime fechaCreacion;
}