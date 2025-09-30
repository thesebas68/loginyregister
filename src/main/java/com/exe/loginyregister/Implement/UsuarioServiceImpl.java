package com.exe.loginyregister.Implement;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import com.exe.loginyregister.Dto.UsuarioDto;
import com.exe.loginyregister.Entity.Rol;
import com.exe.loginyregister.Entity.Usuario;
import com.exe.loginyregister.Enum.EstadoUsuarioEnum;
import com.exe.loginyregister.Repository.RolRepository;
import com.exe.loginyregister.Repository.UsuarioRepository;
import com.exe.loginyregister.Service.UsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Usuario registrarUsuario(UsuarioDto usuarioDto) {
        log.info("Iniciando registro de usuario: {}", usuarioDto.getCorreoElectronico());

        if (existeUsuarioPorCorreo(usuarioDto.getCorreoElectronico())) {
            throw new RuntimeException("El correo electrónico ya está registrado");
        }

        if (existeUsuarioPorIdentificacion(usuarioDto.getNumeroIdentificacion())) {
            throw new RuntimeException("El número de identificación ya está registrado");
        }

        Rol rolUsuario = rolRepository.findByNombreRol("USUARIO")
                .orElseThrow(() -> new RuntimeException("Rol por defecto no encontrado"));

        Usuario usuario = modelMapper.map(usuarioDto, Usuario.class);

        usuario.setClaveHash(passwordEncoder.encode(usuarioDto.getClaveHash()));
        usuario.setEstadoUsuario(EstadoUsuarioEnum.A);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setRol(rolUsuario);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        log.info("Usuario guardado exitosamente con ID: {}", usuarioGuardado.getIdUsuario());

        return usuarioGuardado;
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorCorreo(String correo) {
        return usuarioRepository.findByCorreoElectronico(correo);
    }

    @Override
    public boolean existeUsuarioPorCorreo(String correo) {
        return usuarioRepository.existsByCorreoElectronico(correo);
    }

    @Override
    public boolean existeUsuarioPorIdentificacion(String identificacion) {
        return usuarioRepository.existsByNumeroIdentificacion(identificacion);
    }

}
