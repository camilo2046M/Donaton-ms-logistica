package com.gestionDonaton.gestion_logistica.controller;

import com.gestionDonaton.gestion_logistica.client.DonacionClient;
import com.gestionDonaton.gestion_logistica.dto.EnvioRequestDTO;
import com.gestionDonaton.gestion_logistica.dto.EnvioResponseDTO;
import com.gestionDonaton.gestion_logistica.service.EnvioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EnvioController {

    private final EnvioService envioService;
    private final DonacionClient donacionClient;

    @PostMapping
    public ResponseEntity<EnvioResponseDTO> planificarEnvio(@RequestBody EnvioRequestDTO request) {
        EnvioResponseDTO response = envioService.planificarEnvio(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<EnvioResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(envioService.actualizarEstado(id, nuevoEstado));
    }

    @PostMapping("/procesar/{palabra}")
    public ResponseEntity<List<EnvioResponseDTO>> procesarAutomatico(@PathVariable String palabra) {
        List<EnvioResponseDTO> respuestas = envioService.procesarDonacionesAutomaticas(palabra);
        return ResponseEntity.ok(respuestas);
    }

    @GetMapping
    public ResponseEntity<List<EnvioResponseDTO>> listarEnvios() {
        return ResponseEntity.ok(envioService.listarEnvios());
    }

    @GetMapping("/test-conexion/{palabra}")
    public ResponseEntity<?> probarConexion(@PathVariable String palabra) {
        try {
            return ResponseEntity.ok(donacionClient.buscarPorPalabra(palabra));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error conectando a Donaciones: " + e.getMessage());
        }
    }
}