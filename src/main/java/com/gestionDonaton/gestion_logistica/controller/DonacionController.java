package com.gestionDonaton.gestion_logistica.controller;


import com.gestionDonaton.gestion_logistica.dto.DonacionDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donaciones") // Debe coincidir con la URL del FeignClient
public class DonacionController {

    // Debe coincidir con /buscar/{palabra}
    @GetMapping("/buscar/{palabra}")
    public List<DonacionDTO> buscarPorPalabra(@PathVariable String palabra) {
        // Tu lógica de búsqueda
    }

    // Debe coincidir con /{id}/completar
    @PatchMapping("/{id}/completar")
    public void completarDonacion(@PathVariable Long id) {
        // Tu lógica para marcar como completada
    }
}