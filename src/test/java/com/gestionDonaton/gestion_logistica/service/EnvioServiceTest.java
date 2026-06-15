package com.gestionDonaton.gestion_logistica.service;

import com.gestionDonaton.gestion_logistica.client.DonacionClient;
import com.gestionDonaton.gestion_logistica.dto.DonacionDTO;
import com.gestionDonaton.gestion_logistica.dto.EnvioRequestDTO;
import com.gestionDonaton.gestion_logistica.dto.EnvioResponseDTO;
import com.gestionDonaton.gestion_logistica.entity.Envio;
import com.gestionDonaton.gestion_logistica.factory.EnvioFactory;
import com.gestionDonaton.gestion_logistica.repository.EnvioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private EnvioFactory envioFactory;

    @Mock
    private DonacionClient donacionClient;

    @InjectMocks
    private EnvioService envioService;

    @Test
    void planificarEnvio_DeberiaGuardarYRetornarResponse() {
        // Arrange
        EnvioRequestDTO request = EnvioRequestDTO.builder().donacionId(1L).build();
        Envio mockEnvio = Envio.builder()
                .id(10L).donacionId(1L).centroAcopioOrigen("Origen").destino("Destino")
                .tipoTransporte("TERRESTRE").estado("EN_PREPARACION").fechaCreacion(LocalDateTime.now())
                .build();

        when(envioFactory.crearEnvio(any(EnvioRequestDTO.class))).thenReturn(mockEnvio);
        when(envioRepository.save(any(Envio.class))).thenReturn(mockEnvio);

        // Act
        EnvioResponseDTO response = envioService.planificarEnvio(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getEstado()).isEqualTo("EN_PREPARACION");
        verify(envioFactory, times(1)).crearEnvio(request);
        verify(envioRepository, times(1)).save(mockEnvio);
    }

    @Test
    void procesarDonacionesAutomaticas_DeberiaDeterminarTransporteAereoParaMedicamentos() {
        // Arrange
        String palabraClave = "medicamento";
        DonacionDTO donacionMedicamento = DonacionDTO.builder()
                .id(1L)
                .nombreObjeto("Caja de Medicamentos paracetamol")
                .build();

        Envio mockEnvioAereo = Envio.builder()
                .id(50L).donacionId(1L).centroAcopioOrigen("Farmacia Central / Hospital")
                .destino("Zona de Catástrofe - Centro Médico").tipoTransporte("AEREO").estado("EN_PREPARACION")
                .build();

        when(donacionClient.buscarPorPalabra(palabraClave)).thenReturn(List.of(donacionMedicamento));
        when(envioFactory.crearEnvio(any(EnvioRequestDTO.class))).thenReturn(mockEnvioAereo);
        when(envioRepository.save(any(Envio.class))).thenReturn(mockEnvioAereo);

        // Act
        List<EnvioResponseDTO> resultados = envioService.procesarDonacionesAutomaticas(palabraClave);

        // Assert
        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getTipoTransporte()).isEqualTo("AEREO");
        assertThat(resultados.get(0).getCentroAcopioOrigen()).isEqualTo("Farmacia Central / Hospital");
        verify(donacionClient, times(1)).buscarPorPalabra(palabraClave);
    }

    @Test
    void actualizarEstado_AEntregado_DeberiaNotificarAlMicroservicioDonaciones() {
        // Arrange
        Long envioId = 1L;
        Long donacionId = 99L;
        Envio mockEnvioExistente = Envio.builder()
                .id(envioId).donacionId(donacionId).estado("EN_TRANSITO")
                .build();
        Envio mockEnvioActualizado = Envio.builder()
                .id(envioId).donacionId(donacionId).estado("ENTREGADO")
                .build();

        when(envioRepository.findById(envioId)).thenReturn(Optional.of(mockEnvioExistente));
        when(envioRepository.save(any(Envio.class))).thenReturn(mockEnvioActualizado);

        // Act
        EnvioResponseDTO response = envioService.actualizarEstado(envioId, "ENTREGADO");

        // Assert
        assertThat(response.getEstado()).isEqualTo("ENTREGADO");
        // Verificamos que al pasar a ENTREGADO se intente sincronizar mediante Feign
        verify(donacionClient, times(1)).completarDonacion(donacionId);
        verify(envioRepository, times(1)).save(mockEnvioExistente);
    }

    @Test
    void actualizarEstado_DeberiaLanzarException_CuandoEnvioNoExiste() {
        // Arrange
        Long idInexistente = 999L;
        when(envioRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> envioService.actualizarEstado(idInexistente, "ENTREGADO"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Envío no encontrado");

        // Nos aseguramos que nunca intente llamar al cliente Feign ni guardar nada
        verify(donacionClient, never()).completarDonacion(anyLong());
        verify(envioRepository, never()).save(any(Envio.class));
    }
}