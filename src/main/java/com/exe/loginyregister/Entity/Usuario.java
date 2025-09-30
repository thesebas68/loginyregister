package com.exe.loginyregister.Entity;

import com.exe.loginyregister.Enum.EstadoUsuarioEnum;
import com.exe.loginyregister.Enum.GeneroEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    private String tipoIdentificacion;
    @Column(unique = true)
    private String numeroIdentificacion;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String lugarNacimiento;
    private String nacionalidad;
    private String direccion;
    private String ciudad;
    private String departamento;
    private String telefono;

    @Column(unique = true)
    private String correoElectronico;

    @Enumerated(EnumType.STRING)
    private GeneroEnum genero;

    private String claveHash;

    @Enumerated(EnumType.STRING)
    private EstadoUsuarioEnum estadoUsuario;

    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idRol")
    private Rol rol;
}
