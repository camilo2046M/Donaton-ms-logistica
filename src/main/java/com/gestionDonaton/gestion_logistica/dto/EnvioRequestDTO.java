package com.gestionDonaton.gestion_logistica.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvioRequestDTO {
    private Long donacionId;
    private String centroAcopioOrigen;
    private String destino;
    private String tipoTransporte;
}