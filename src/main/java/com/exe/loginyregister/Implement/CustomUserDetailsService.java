package com.exe.loginyregister.Implement;

import com.exe.loginyregister.Entity.Usuario;
import com.exe.loginyregister.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {


    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        log.info("Buscando usuario por correo: {}", correo);

        Usuario usuario = usuarioRepository.findByCorreoElectronico(correo)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con correo: {}", correo);
                    return new UsernameNotFoundException("Usuario no encontrado: " + correo);
                });

        log.info("Usuario encontrado: {}, Estado: {}", usuario.getCorreoElectronico(), usuario.getEstadoUsuario());

        // Convertir el rol del usuario a authorities de Spring Security
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombreRol())
        );

        return new User(
                usuario.getCorreoElectronico(),
                usuario.getClaveHash(),
                usuario.getEstadoUsuario().name().equals("A"), // enabled
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities
        );
    }

}
