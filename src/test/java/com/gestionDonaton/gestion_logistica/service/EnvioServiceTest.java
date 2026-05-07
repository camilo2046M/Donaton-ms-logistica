package com.gestionDonaton.gestion_logistica.service;


import com.gestionDonaton.gestion_logistica.dto.EnvioRequestDTO;
import com.gestionDonaton.gestion_logistica.dto.EnvioResponseDTO;
import com.gestionDonaton.gestion_logistica.entity.Envio;
import com.gestionDonaton.gestion_logistica.factory.EnvioFactory;
import com.gestionDonaton.gestion_logistica.repository.EnvioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnvioServiceTest {

    // Mockeamos la base de datos
    @Mock
    private EnvioRepository envioRepository;

    // Mockeamos nuestra fábrica de objetos
    @Mock
    private EnvioFactory envioFactory;

    // Inyectamos los mocks en el servicio real que vamos a probar
    @InjectMocks
    private EnvioService envioService;

    private EnvioRequestDTO requestDTO;
    private Envio envioSimulado;

    @BeforeEach
    void setUp() {
        requestDTO = new EnvioRequestDTO();
        requestDTO.setCentroAcopioOrigen("Acopio Central");
        requestDTO.setDestino("Zona Norte");
        requestDTO.setTipoTransporte("TERRESTRE");

        envioSimulado = Envio.builder()
                .id(1L)
                .centroAcopioOrigen("Acopio Central")
                .destino("Zona Norte")
                .tipoTransporte("TERRESTRE")
                .estado("EN_PREPARACION")
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    @Test
    void testPlanificarEnvio_Exito() {
        // Arrange (Preparación)
        // 1. Le decimos a la fábrica falsa qué devolver
        when(envioFactory.crearEnvio(any(EnvioRequestDTO.class))).thenReturn(envioSimulado);
        // 2. Le decimos al repositorio falso qué devolver
        when(envioRepository.save(any(Envio.class))).thenReturn(envioSimulado);

        // Act (Ejecución)
        EnvioResponseDTO response = envioService.planificarEnvio(requestDTO);

        // Assert (Verificación)
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Acopio Central", response.getCentroAcopioOrigen());
        assertEquals("EN_PREPARACION", response.getEstado());

        // Verificamos rigurosamente que nuestros patrones fueron utilizados
        verify(envioFactory, times(1)).crearEnvio(any(EnvioRequestDTO.class));
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
    void testListarEnvios_Exito() {
        // Arrange
        when(envioRepository.findAll()).thenReturn(Arrays.asList(envioSimulado));

        // Act
        List<EnvioResponseDTO> resultados = envioService.listarEnvios();

        // Assert
        assertFalse(resultados.isEmpty());
        assertEquals(1, resultados.size());
        assertEquals("Zona Norte", resultados.get(0).getDestino());

        verify(envioRepository, times(1)).findAll();
    }
}