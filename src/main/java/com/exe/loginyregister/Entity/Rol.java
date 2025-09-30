package com.exe.loginyregister.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "roles")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;

    private String nombreRol;
    private String descripcion;
    private String estadoRol;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private String usuarioCreacion;
    private String usuarioActualizacion;

}
