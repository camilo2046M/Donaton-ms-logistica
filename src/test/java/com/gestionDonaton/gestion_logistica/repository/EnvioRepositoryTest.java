package com.gestionDonaton.gestion_logistica.repository;

import com.gestionDonaton.gestion_logistica.entity.Envio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testlogisticadb;DATABASE_TO_UPPER=FALSE;ANALYZE_AUTO=0",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class EnvioRepositoryTest {

    @Autowired
    private EnvioRepository envioRepository;

    @Test
    void guardarEnvio_DeberiaAplicarPrePersist() {
        // 1. Arrange (Preparar datos usando el Builder de Lombok)
        Envio nuevoEnvio = Envio.builder()
                .donacionId(100L)
                .centroAcopioOrigen("Centro Norte")
                .destino("Comunidad Sur")
                .tipoTransporte("Camión")
                // Dejamos 'estado' nulo para probar el @PrePersist
                .build();

        // 2. Act (Ejecutar la acción)
        Envio envioGuardado = envioRepository.save(nuevoEnvio);

        // 3. Assert (Verificar resultados)
        assertThat(envioGuardado.getId()).isNotNull();
        assertThat(envioGuardado.getEstado()).isEqualTo("EN_PREPARACION"); // Verifica @PrePersist de estado
        assertThat(envioGuardado.getFechaCreacion()).isNotNull();          // Verifica @PrePersist de fecha
    }

    @Test
    void findByEstado_DeberiaRetornarEnviosFiltrados() {
        // Arrange
        Envio envio1 = Envio.builder().donacionId(1L).centroAcopioOrigen("A").destino("B").tipoTransporte("A").estado("EN_TRANSITO").build();
        Envio envio2 = Envio.builder().donacionId(2L).centroAcopioOrigen("A").destino("B").tipoTransporte("A").estado("ENTREGADO").build();
        envioRepository.save(envio1);
        envioRepository.save(envio2);

        // Act
        List<Envio> enTransito = envioRepository.findByEstado("EN_TRANSITO");

        // Assert
        assertThat(enTransito).hasSize(1);
        assertThat(enTransito.get(0).getEstado()).isEqualTo("EN_TRANSITO");
    }

    @Test
    void existsByDonacionId_DeberiaRetornarTrueSiExisteYFalseSiNo() {
        // Arrange
        Long donacionIdExistente = 55L;
        Long donacionIdInexistente = 99L;
        Envio envio = Envio.builder().donacionId(donacionIdExistente).centroAcopioOrigen("A").destino("B").tipoTransporte("A").build();
        envioRepository.save(envio);

        // Act
        boolean existe = envioRepository.existsByDonacionId(donacionIdExistente);
        boolean noExiste = envioRepository.existsByDonacionId(donacionIdInexistente);

        // Assert
        assertThat(existe).isTrue();
        assertThat(noExiste).isFalse();
    }
}