package com.gestionDonaton.gestion_logistica.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "donacion_id", nullable = false)
    private Long donacionId;

    @Column(name = "centro_acopio_origen", nullable = false)
    private String centroAcopioOrigen;

    @Column(nullable = false)
    private String destino;

    @Column(name = "tipo_transporte", nullable = false)
    private String tipoTransporte;

    @Column(nullable = false)
    private String estado;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = "EN_PREPARACION";
        }
    }
}