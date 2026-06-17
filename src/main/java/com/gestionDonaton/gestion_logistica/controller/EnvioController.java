package com.gestionDonaton.gestion_logistica.controller;

import com.gestionDonaton.gestion_logistica.client.DonacionClient;
import com.gestionDonaton.gestion_logistica.dto.EnvioRequestDTO;
import com.gestionDonaton.gestion_logistica.dto.EnvioResponseDTO;
import com.gestionDonaton.gestion_logistica.service.EnvioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/envios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Gestión de Envíos", description = "Endpoints para la planificación, seguimiento y procesamiento automático de envíos de donaciones")
public class EnvioController {

    private final EnvioService envioService;
    private final DonacionClient donacionClient;

    @PostMapping
    @Operation(
            summary = "Planificar un nuevo envío",
            description = "Crea y registra la planificación de un envío a partir de los datos provistos en el cuerpo de la solicitud."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Envio planificado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos o inconsistentes")
    })
    public ResponseEntity<EnvioResponseDTO> planificarEnvio(@RequestBody EnvioRequestDTO request) {
        EnvioResponseDTO response = envioService.planificarEnvio(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/estado")
    @Operation(
            summary = "Actualizar el estado de un envío",
            description = "Modifica parcialmente el estado de un envío existente (ej. EN_CAMINO, ENTREGADO) usando su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado del envío actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró ningún envío con el ID proporcionado")
    })
    public ResponseEntity<EnvioResponseDTO> actualizarEstado(
            @Parameter(description = "ID único del envío a modificar", required = true)
            @PathVariable Long id,

            @Parameter(description = "Nuevo estado que se le asignará al envío", required = true)
            @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(envioService.actualizarEstado(id, nuevoEstado));
    }

    @PostMapping("/procesar/{palabra}")
    @Operation(
            summary = "Procesamiento automático de donaciones",
            description = "Busca donaciones mediante una palabra clave y genera de manera automatizada los envíos correspondientes."
    )
    @ApiResponse(responseCode = "200", description = "Procesamiento completado y lista de envíos generados devuelta")
    public ResponseEntity<List<EnvioResponseDTO>> procesarAutomatico(
            @Parameter(description = "Palabra clave para filtrar las donaciones a procesar", required = true)
            @PathVariable String palabra) {
        List<EnvioResponseDTO> respuestas = envioService.procesarDonacionesAutomaticas(palabra);
        return ResponseEntity.ok(respuestas);
    }

    @GetMapping
    @Operation(
            summary = "Listar todos los envíos",
            description = "Recupera un listado con el historial de todos los envíos registrados en el sistema."
    )
    @ApiResponse(responseCode = "200", description = "Listado de envíos obtenido exitosamente")
    public ResponseEntity<List<EnvioResponseDTO>> listarEnvios() {
        return ResponseEntity.ok(envioService.listarEnvios());
    }

    @GetMapping("/test-conexion/{palabra}")
    @Operation(
            summary = "Probar conexión con Microservicio de Donaciones (Feign/WebClient)",
            description = "Endpoint de diagnóstico técnico para verificar la comunicación directa con el microservicio de Donaciones a través de su cliente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conexión exitosa. Devuelve los resultados obtenidos del otro microservicio"),
            @ApiResponse(responseCode = "500", description = "Error de comunicación o timeout con el microservicio de Donaciones")
    })
    public ResponseEntity<?> probarConexion(
            @Parameter(description = "Término de búsqueda para probar el puente de comunicación", required = true)
            @PathVariable String palabra) {
        try {
            return ResponseEntity.ok(donacionClient.buscarPorPalabra(palabra));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error conectando a Donaciones: " + e.getMessage());
        }
    }
}