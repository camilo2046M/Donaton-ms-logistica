package com.gestionDonaton.gestion_logistica.controller;


import com.gestionDonaton.gestion_logistica.dto.EnvioRequestDTO;
import com.gestionDonaton.gestion_logistica.dto.EnvioResponseDTO;
import com.gestionDonaton.gestion_logistica.service.EnvioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/envios")
@RequiredArgsConstructor
public class    EnvioController {

    private final EnvioService envioService;

    @PostMapping
    public ResponseEntity<EnvioResponseDTO> planificarEnvio(@RequestBody EnvioRequestDTO request) {
        EnvioResponseDTO response = envioService.planificarEnvio(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EnvioResponseDTO>> listarEnvios() {
        return ResponseEntity.ok(envioService.listarEnvios());
    }
}
