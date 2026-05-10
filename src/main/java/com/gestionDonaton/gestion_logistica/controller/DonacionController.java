package com.gestionDonaton.gestion_logistica.controller;

import com.gestionDonaton.gestion_logistica.dto.DonacionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList; // Para el ejemplo de lista vacía

@RestController
@RequestMapping("/api/donaciones")
public class DonacionController {

    @GetMapping("/buscar/{palabra}")
    public ResponseEntity<List<DonacionDTO>> buscarPorPalabra(@PathVariable String palabra) {
        List<DonacionDTO> listaSimulada = new ArrayList<>();
        return ResponseEntity.ok(listaSimulada);
    }

    @PatchMapping("/{id}/completar")
    public ResponseEntity<Void> completarDonacion(@PathVariable Long id) {
        // Lógica para completar
        return ResponseEntity.noContent().build();
    }
}