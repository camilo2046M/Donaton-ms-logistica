package com.gestionDonaton.gestion_logistica.controller;

import com.gestionDonaton.gestion_logistica.dto.DonacionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;



@RestController
@RequestMapping("/api/donaciones")
@Tag(name = "Consultas y Estados de Donaciones", description = "Endpoints optimizados para la búsqueda y actualización de flujos de donaciones")
public class DonacionController {

    @GetMapping("/buscar/{palabra}")
    @Operation(
            summary = "Buscar donaciones por palabra clave",
            description = "Filtra y recupera una lista de donaciones simplificadas (DTO) que coincidan con el criterio de búsqueda."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Búsqueda realizada con éxito (puede retornar una lista vacía si no hay coincidencias)"
    )
    public ResponseEntity<List<DonacionDTO>> buscarPorPalabra(
            @Parameter(description = "Término o palabra clave para filtrar las donaciones", required = true)
            @PathVariable String palabra) {
        List<DonacionDTO> listaSimulada = new ArrayList<>();
        return ResponseEntity.ok(listaSimulada);
    }

    @PatchMapping("/{id}/completar")
    @Operation(
            summary = "Marcar donación como completada",
            description = "Actualiza el ciclo de vida de una donación específica a estado completado usando su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Donación actualizada con éxito. No se devuelve contenido en el cuerpo"),
            @ApiResponse(responseCode = "404", description = "El ID de la donación no existe en el sistema")
    })
    public ResponseEntity<Void> completarDonacion(
            @Parameter(description = "ID único de la donación a completar", required = true)
            @PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}