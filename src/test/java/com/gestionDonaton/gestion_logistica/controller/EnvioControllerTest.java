package com.gestionDonaton.gestion_logistica.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestionDonaton.gestion_logistica.client.DonacionClient;
import com.gestionDonaton.gestion_logistica.dto.DonacionDTO;
import com.gestionDonaton.gestion_logistica.dto.EnvioRequestDTO;
import com.gestionDonaton.gestion_logistica.dto.EnvioResponseDTO;
import com.gestionDonaton.gestion_logistica.service.EnvioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnvioController.class)
class EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EnvioService envioService;

    @MockitoBean
    private DonacionClient donacionClient;

    private EnvioResponseDTO mockResponse;

    @BeforeEach
    void setUp() {
        // Inicializamos un DTO de respuesta genérico para reutilizarlo en los tests
        mockResponse = new EnvioResponseDTO();
        // Setea aquí los atributos reales de tu EnvioResponseDTO, por ejemplo:
        // mockResponse.setXZId(1L);
        // mockResponse.setEstado("PLANIFICADO");
    }

    @Test
    void planificarEnvio_DeberiaRetornar213Created() throws Exception {
        EnvioRequestDTO requestDTO = new EnvioRequestDTO();
        // Rellena aquí los campos requeridos de tu request si es necesario

        when(envioService.planificarEnvio(any(EnvioRequestDTO.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/envios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void actualizarEstado_DeberiaRetornar200Ok() throws Exception {
        Long envioId = 1L;
        String nuevoEstado = "EN_CAMINO";

        when(envioService.actualizarEstado(eq(envioId), eq(nuevoEstado))).thenReturn(mockResponse);

        mockMvc.perform(patch("/api/envios/{id}/estado", envioId)
                        .param("nuevoEstado", nuevoEstado)) // Pasa el @RequestParam
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void procesarAutomatico_DeberiaRetornarListaDeEnvios() throws Exception {
        String palabraClave = "alimentos";
        List<EnvioResponseDTO> listaRespuestas = Arrays.asList(mockResponse);

        when(envioService.procesarDonacionesAutomaticas(palabraClave)).thenReturn(listaRespuestas);

        mockMvc.perform(post("/api/envios/procesar/{palabra}", palabraClave))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void listarEnvios_DeberiaRetornarTodosLosEnvios() throws Exception {
        List<EnvioResponseDTO> todosLosEnvios = Arrays.asList(mockResponse, mockResponse);

        when(envioService.listarEnvios()).thenReturn(todosLosEnvios);

        mockMvc.perform(get("/api/envios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void probarConexion_DeberiaRetornarRespuestaDeDonacionClient() throws Exception {
        String palabra = "test";
        DonacionDTO mockDonacion = DonacionDTO.builder()
                .id(1L)
                // Si necesitas rellenar los otros campos para el test, hazlo así:
                // .nombre("Alimentos")
                // .peso(10.5)
                .build();

        // 2. Metemos el DTO construido en la lista
        List<DonacionDTO> mockDonacionesClientResponse = Arrays.asList(mockDonacion);

        // 3. Simulamos el comportamiento del cliente Feign
        when(donacionClient.buscarPorPalabra(palabra)).thenReturn(mockDonacionesClientResponse);

        // 4. Ejecutamos la petición de prueba
        mockMvc.perform(get("/api/envios/test-conexion/{palabra}", palabra))
                .andExpect(status().isOk());
    }

    @Test
    void probarConexion_DeberiaRetornar500CuandoFallaElCliente() throws Exception {
        String palabra = "test";
        String mensajeError = "Connection timeout";

        // Simulamos que el microservicio de Donaciones está caído
        when(donacionClient.buscarPorPalabra(palabra)).thenThrow(new RuntimeException(mensajeError));

        mockMvc.perform(get("/api/envios/test-conexion/{palabra}", palabra))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error conectando a Donaciones: " + mensajeError));
    }
}