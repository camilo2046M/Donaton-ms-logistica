package com.gestionDonaton.gestion_logistica.client;

import com.gestionDonaton.gestion_logistica.dto.DonacionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ms-donaciones", url = "http://localhost:8081/api/donaciones")
public interface DonacionClient {

    @GetMapping("/buscar/{palabra}")
    List<DonacionDTO> buscarPorPalabra(@PathVariable("palabra") String palabra);

    @PatchMapping("/{id}/completar")
    void completarDonacion(@PathVariable("id") Long id);
}
