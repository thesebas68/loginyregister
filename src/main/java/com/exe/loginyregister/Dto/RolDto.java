package com.exe.loginyregister.Dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class RolDto {

        private Long idRol;
        private String nombreRol;
        private String descripcion;
        private String estadoRol;
        private LocalDateTime fechaCreacion;
        private LocalDateTime fechaActualizacion;
        private String usuarioCreacion;
        private String usuarioActualizacion;

}

