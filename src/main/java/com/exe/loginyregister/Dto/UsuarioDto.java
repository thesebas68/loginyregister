package com.exe.loginyregister.Dto;

import com.exe.loginyregister.Enum.EstadoUsuarioEnum;
import com.exe.loginyregister.Enum.GeneroEnum;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDto {
    private Long idUsuario;
    private String tipoIdentificacion;
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
    private String correoElectronico;
    private GeneroEnum genero;
    private String claveHash;
    private String confirmPassword;
    private EstadoUsuarioEnum estadoUsuario;
    private LocalDateTime fechaCreacion;
    private Long idRol;
}
