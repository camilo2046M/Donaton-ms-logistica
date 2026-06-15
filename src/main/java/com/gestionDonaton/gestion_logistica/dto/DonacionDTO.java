package com.gestionDonaton.gestion_logistica.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class DonacionDTO {
    private Long id;
    private String donanteNombre;
    private String nombreObjeto; 
    private Double monto;
}