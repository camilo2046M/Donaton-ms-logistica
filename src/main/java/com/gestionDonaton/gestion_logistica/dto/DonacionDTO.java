package com.gestionDonaton.gestion_logistica.dto;

import lombok.Data;

@Data
public class DonacionDTO {
    private Long id;
    private String donanteNombre;
    private String nombreObjeto;
    private Double monto;
}