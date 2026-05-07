package com.gestionDonaton.gestion_logistica.service;

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
    private final EnvioFactory envioFactory; // Inyectamos la fábrica

    public EnvioResponseDTO planificarEnvio(EnvioRequestDTO request) {
        // 1. Usamos el Factory Method para crear la instancia
        Envio nuevoEnvio = envioFactory.crearEnvio(request);

        // 2. Guardamos en base de datos (Repository Pattern)
        Envio envioGuardado = envioRepository.save(nuevoEnvio);

        // 3. Mapeamos a DTO
        return mapToResponseDTO(envioGuardado);
    }

    public List<EnvioResponseDTO> listarEnvios() {
        return envioRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private EnvioResponseDTO mapToResponseDTO(Envio envio) {
        return EnvioResponseDTO.builder()
                .id(envio.getId())
                .centroAcopioOrigen(envio.getCentroAcopioOrigen())
                .destino(envio.getDestino())
                .tipoTransporte(envio.getTipoTransporte())
                .estado(envio.getEstado())
                .fechaCreacion(envio.getFechaCreacion())
                .build();
    }
}
