package com.gestionDonaton.gestion_logistica.service;

import com.gestionDonaton.gestion_logistica.client.DonacionClient;
import com.gestionDonaton.gestion_logistica.dto.DonacionDTO;
import com.gestionDonaton.gestion_logistica.dto.EnvioResponseDTO;
import com.gestionDonaton.gestion_logistica.factory.EnvioFactory;
import com.gestionDonaton.gestion_logistica.entity.Envio;
import com.gestionDonaton.gestion_logistica.repository.EnvioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private DonacionClient donacionClient;

    @Spy
    private EnvioFactory envioFactory;

    @InjectMocks
    private EnvioService envioService;

    private DonacionDTO donacionMock;

    @BeforeEach
    void setUp() {
        donacionMock = new DonacionDTO();
        donacionMock.setId(1L);
        donacionMock.setNombreObjeto("Medicamentos vencimiento proximo");
    }

    @Test
    void testProcesarDonacionesAutomaticas_Aereo() {

        when(donacionClient.buscarPorPalabra("medicamento")).thenReturn(List.of(donacionMock));
        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> i.getArguments()[0]);


        List<EnvioResponseDTO> resultado = envioService.procesarDonacionesAutomaticas("medicamento");


        assertFalse(resultado.isEmpty());
        assertEquals("AEREO", resultado.get(0).getTipoTransporte());
        assertEquals("Farmacia Central / Hospital", resultado.get(0).getCentroAcopioOrigen());
        verify(envioRepository, times(1)).save(any());
    }

    @Test
    void testProcesarDonacionesAutomaticas_Terrestre() {
        donacionMock.setNombreObjeto("Ropa de invierno");
        when(donacionClient.buscarPorPalabra("ropa")).thenReturn(List.of(donacionMock));
        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> i.getArguments()[0]);

        List<EnvioResponseDTO> resultado = envioService.procesarDonacionesAutomaticas("ropa");

        assertEquals("TERRESTRE", resultado.get(0).getTipoTransporte());
        assertEquals("Bodega Textil", resultado.get(0).getCentroAcopioOrigen());
    }

    @Test
    void testActualizarEstado_EntregadoAvisaADonaciones() {

        Envio envioExistente = Envio.builder()
                .id(10L)
                .donacionId(1L)
                .estado("EN_CAMINO")
                .build();

        when(envioRepository.findById(10L)).thenReturn(Optional.of(envioExistente));
        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        envioService.actualizarEstado(10L, "ENTREGADO");

        // Assert
        verify(donacionClient, times(1)).completarDonacion(1L);
        assertEquals("ENTREGADO", envioExistente.getEstado());
    }

    @Test
    void testListarEnvios() {
        Envio e1 = Envio.builder().id(1L).estado("PENDIENTE").build();
        when(envioRepository.findAll()).thenReturn(List.of(e1));

        List<EnvioResponseDTO> lista = envioService.listarEnvios();

        assertNotNull(lista);
        assertEquals(1, lista.size());
    }
}