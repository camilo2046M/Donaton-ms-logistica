package com.gestionDonaton.gestion_logistica.factory;

import com.gestionDonaton.gestion_logistica.dto.EnvioRequestDTO;
import com.gestionDonaton.gestion_logistica.entity.Envio;
import org.springframework.stereotype.Component;
import com.gestionDonaton.gestion_logistica.dto.DonacionDTO;


@Component
public class EnvioFactory {


    public Envio crearEnvio(EnvioRequestDTO request) {
        return Envio.builder()
                .donacionId(request.getDonacionId())
                .centroAcopioOrigen(request.getCentroAcopioOrigen())
                .destino(request.getDestino())
                .tipoTransporte(validarTipoTransporte(request.getTipoTransporte()))
                .estado("EN_PREPARACION")
                .build();
    }

    public Envio crearDesdeDonacion(DonacionDTO donacion) {
        return Envio.builder()
                .donacionId(donacion.getId())
                .centroAcopioOrigen("CENTRO_CENTRAL_GENERICO") // Valor por defecto
                .destino("DESTINO_POR_DEFINIR")             // Valor por defecto
                .tipoTransporte("TERRESTRE")
                .estado("EN_PREPARACION")
                .build();
    }

    private String validarTipoTransporte(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return "TERRESTRE";
        }
        String tipoUpper = tipo.toUpperCase();
        if (tipoUpper.equals("AEREO") || tipoUpper.equals("MARITIMO") || tipoUpper.equals("TERRESTRE")) {
            return tipoUpper;
        }
        return "TERRESTRE";
    }
}