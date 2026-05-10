
package com.gestionDonaton.gestion_logistica.service;

import com.gestionDonaton.gestion_logistica.client.DonacionClient;
import com.gestionDonaton.gestion_logistica.dto.DonacionDTO;
import com.gestionDonaton.gestion_logistica.dto.EnvioResponseDTO;
import com.gestionDonaton.gestion_logistica.factory.EnvioFactory;
import com.gestionDonaton.gestion_logistica.entity.Envio; // Asegúrate que sea .entity
import com.gestionDonaton.gestion_logistica.repository.EnvioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EnvioServiceTest {

    private EnvioRepository envioRepository;
    private DonacionClient donacionClient;
    private EnvioFactory envioFactory;
    private EnvioService envioService;

    private DonacionDTO donacionMock;

    @BeforeEach
    void setUp() {

        envioRepository = mock(EnvioRepository.class);
        donacionClient = mock(DonacionClient.class);
        envioFactory = new EnvioFactory();
        envioService = new EnvioService(envioRepository, envioFactory, donacionClient);
        donacionMock = new DonacionDTO();
        donacionMock.setId(1L);
        donacionMock.setNombreObjeto("Medicamento Ibuprofeno");
    }

    @Test
    void testProcesarDonaciones_Aereo() {
        when(donacionClient.buscarPorPalabra("medicamento")).thenReturn(List.of(donacionMock));
        when(envioRepository.save(any(Envio.class))).thenAnswer(invocation -> invocation.getArgument(0));
        List<EnvioResponseDTO> resultado = envioService.procesarDonacionesAutomaticas("medicamento");
        assertFalse(resultado.isEmpty());
        assertEquals("AEREO", resultado.get(0).getTipoTransporte());
        verify(envioRepository, times(1)).save(any());
    }

    @Test
    void testActualizarEstado_Entregado() {
        Envio envioExistente = Envio.builder()
                .id(10L)
                .donacionId(1L)
                .estado("EN_CAMINO")
                .build();

        when(envioRepository.findById(10L)).thenReturn(Optional.of(envioExistente));
        when(envioRepository.save(any(Envio.class))).thenAnswer(i -> i.getArgument(0));

        envioService.actualizarEstado(10L, "ENTREGADO");

        verify(donacionClient, times(1)).completarDonacion(1L);
        assertEquals("ENTREGADO", envioExistente.getEstado());
    }
}