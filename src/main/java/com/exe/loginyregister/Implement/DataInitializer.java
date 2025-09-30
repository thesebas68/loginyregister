package com.exe.loginyregister.Implement;

import com.exe.loginyregister.Entity.Rol;
import com.exe.loginyregister.Repository.RolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final RolRepository rolRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
    }

    private void initializeRoles() {
        log.info("Inicializando roles en la base de datos...");

        List<Rol> rolesPorDefecto = Arrays.asList(
                crearRol("USUARIO", "Rol de usuario estándar del sistema"),
                crearRol("ADMIN", "Rol de administrador del sistema"),
                crearRol("MODERADOR", "Rol de moderador con permisos limitados")
        );

        for (Rol rol : rolesPorDefecto) {
            // Verificar si el rol ya existe
            if (rolRepository.findByNombreRol(rol.getNombreRol()).isEmpty()) {
                Rol rolGuardado = rolRepository.save(rol);
                log.info("Rol creado: {} con ID: {}", rolGuardado.getNombreRol(), rolGuardado.getIdRol());
            } else {
                log.info("El rol {} ya existe, omitiendo creación", rol.getNombreRol());
            }
        }

        log.info("Inicialización de roles completada");
    }

    private Rol crearRol(String nombre, String descripcion) {
        Rol rol = new Rol();
        rol.setNombreRol(nombre);
        rol.setDescripcion(descripcion);
        rol.setEstadoRol("A");
        rol.setFechaCreacion(LocalDateTime.now());
        rol.setFechaActualizacion(LocalDateTime.now());
        rol.setUsuarioCreacion("Sistema");
        rol.setUsuarioActualizacion("Sistema");
        return rol;
    }
}
