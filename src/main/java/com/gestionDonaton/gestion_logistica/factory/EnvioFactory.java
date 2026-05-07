package com.gestionDonaton.gestion_logistica.factory;


import com.gestionDonaton.gestion_logistica.dto.EnvioRequestDTO;
import com.gestionDonaton.gestion_logistica.entity.Envio;
import org.springframework.stereotype.Component;

@Component
public class EnvioFactory {

    // Patrón Factory Method: Centraliza y encapsula la lógica de creación
    public Envio crearEnvio(EnvioRequestDTO request) {

        // Aquí podríamos tener lógica compleja (ej. calcular costos, validar rutas)
        // antes de crear la instancia, lo cual justifica el uso del patrón.

        return Envio.builder()
                .centroAcopioOrigen(request.getCentroAcopioOrigen())
                .destino(request.getDestino())
                .tipoTransporte(validarTipoTransporte(request.getTipoTransporte()))
                .build();
    }

    private String validarTipoTransporte(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return "TERRESTRE"; // Transporte por defecto
        }
        return tipo.toUpperCase();
    }
}