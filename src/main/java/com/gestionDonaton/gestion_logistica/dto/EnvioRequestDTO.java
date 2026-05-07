package com.gestionDonaton.gestion_logistica.dto;

import lombok.Data;

@Data
public class EnvioRequestDTO {
    private String centroAcopioOrigen;
    private String destino;
    private String tipoTransporte;
}