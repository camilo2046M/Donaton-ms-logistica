package com.gestionDonaton.gestion_logistica.service;

import com.gestionDonaton.gestion_logistica.client.DonacionClient;
import com.gestionDonaton.gestion_logistica.dto.DonacionDTO;
import com.gestionDonaton.gestion_logistica.dto.EnvioRequestDTO;
import com.gestionDonaton.gestion_logistica.dto.EnvioResponseDTO;
import com.gestionDonaton.gestion_logistica.entity.Envio;
import com.gestionDonaton.gestion_logistica.factory.EnvioFactory;
import com.gestionDonaton.gestion_logistica.repository.EnvioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final EnvioFactory envioFactory;
    private final DonacionClient donacionClient;

    
    public EnvioResponseDTO planificarEnvio(EnvioRequestDTO request) {
        Envio nuevoEnvio = envioFactory.crearEnvio(request);
        return mapToResponseDTO(envioRepository.save(nuevoEnvio));
    }

    
    public List<EnvioResponseDTO> procesarDonacionesAutomaticas(String palabraClave) {
        // 1. Buscamos donaciones en el microservicio externo
        return donacionClient.buscarPorPalabra(palabraClave).stream()
                .map(donacion -> {
                   
                    EnvioRequestDTO request = autoDeterminarLogistica(donacion);

                    
                    Envio envio = envioFactory.crearEnvio(request);

                    return mapToResponseDTO(envioRepository.save(envio));
                })
                .collect(Collectors.toList());
    }

    private EnvioRequestDTO autoDeterminarLogistica(DonacionDTO donacion) {
        String objeto = donacion.getNombreObjeto().toLowerCase();

       
        var requestBuilder = EnvioRequestDTO.builder()
                .donacionId(donacion.getId());

        if (objeto.contains("medicamento") || objeto.contains("remedio") || objeto.contains("vacuna")) {
            return requestBuilder
                    .centroAcopioOrigen("Farmacia Central / Hospital")
                    .destino("Zona de Catástrofe - Centro Médico")
                    .tipoTransporte("AEREO")
                    .build();
        } else if (objeto.contains("ropa") || objeto.contains("abrigo") || objeto.contains("calzado")) {
            return requestBuilder
                    .centroAcopioOrigen("Bodega Textil")
                    .destino("Gimnasio Municipal (Albergue)")
                    .tipoTransporte("TERRESTRE")
                    .build();
        }

        
        return requestBuilder
                .centroAcopioOrigen("Bodega General")
                .destino("Punto de Acopio Estándar")
                .tipoTransporte("TERRESTRE")
                .build();
    }

    public List<EnvioResponseDTO> listarEnvios() {
        return envioRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public EnvioResponseDTO actualizarEstado(Long id, String nuevoEstado) {

        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado con ID: " + id));
        envio.setEstado(nuevoEstado.toUpperCase());
        return mapToResponseDTO(envioRepository.save(envio));
    }

    private EnvioResponseDTO mapToResponseDTO(Envio envio) {
        return EnvioResponseDTO.builder()
                .id(envio.getId())
                .donacionId(envio.getDonacionId())
                .centroAcopioOrigen(envio.getCentroAcopioOrigen())
                .destino(envio.getDestino())
                .tipoTransporte(envio.getTipoTransporte())
                .estado(envio.getEstado())
                .fechaCreacion(envio.getFechaCreacion())
                .build();
    }
}