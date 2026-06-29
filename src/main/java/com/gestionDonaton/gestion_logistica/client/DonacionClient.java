package com.gestionDonaton.gestion_logistica.client;

import com.gestionDonaton.gestion_logistica.dto.DonacionDTO; // Asegúrate de tener este DTO
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;

@FeignClient(name = "ms-donaciones", url = "${donaton.ms.donaciones.url}")
public interface DonacionClient {

    @GetMapping("/api/v1/donaciones/buscar/{palabra}")
    List<DonacionDTO> buscarPorPalabra(@PathVariable("palabra") String palabra);

    @PatchMapping("/{id}/completar")
    void completarDonacion(@PathVariable("id") Long id);
}
