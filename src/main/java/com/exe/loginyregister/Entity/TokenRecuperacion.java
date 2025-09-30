package com.exe.loginyregister.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tokens_recuperacion")
public class TokenRecuperacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(nullable = false)
    private Boolean utilizado = false;

    // Método para verificar si el token ha expirado
    public boolean haExpirado() {
        return LocalDateTime.now().isAfter(fechaExpiracion);
    }

    // Método para verificar si el token es válido
    public boolean esValido() {
        return !utilizado && !haExpirado();
    }
}
