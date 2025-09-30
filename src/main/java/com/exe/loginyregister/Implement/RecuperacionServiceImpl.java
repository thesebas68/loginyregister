package com.exe.loginyregister.Implement;

import com.exe.loginyregister.Entity.TokenRecuperacion;
import com.exe.loginyregister.Entity.Usuario;
import com.exe.loginyregister.Repository.TokenRecuperacionRepository;
import com.exe.loginyregister.Repository.UsuarioRepository;
import com.exe.loginyregister.Service.EmailService;
import com.exe.loginyregister.Service.RecuperacionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecuperacionServiceImpl implements RecuperacionService {
    private final UsuarioRepository usuarioRepository;
    private final TokenRecuperacionRepository tokenRecuperacionRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    private static final int TOKEN_EXPIRATION_HOURS = 1;

    @Override
    @Transactional
    public void solicitarRecuperacion(String email) {
        log.info("Solicitando recuperación para email: {}", email);

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoElectronico(email);

        // Por seguridad, no revelamos si el email existe o no
        if (usuarioOpt.isEmpty()) {
            log.info("Email no encontrado: {}, pero no se revelará al usuario", email);
            return;
        }

        Usuario usuario = usuarioOpt.get();

        // Invalidar tokens anteriores no utilizados
        tokenRecuperacionRepository.invalidarTokensAnteriores(usuario);

        // Generar nuevo token
        String token = generarToken();
        TokenRecuperacion tokenRecuperacion = new TokenRecuperacion();
        tokenRecuperacion.setToken(token);
        tokenRecuperacion.setUsuario(usuario);
        tokenRecuperacion.setFechaExpiracion(LocalDateTime.now().plusHours(TOKEN_EXPIRATION_HOURS));
        tokenRecuperacion.setUtilizado(false);

        tokenRecuperacionRepository.save(tokenRecuperacion);
        log.info("Token de recuperación creado para usuario: {}", usuario.getCorreoElectronico());

        // Enviar email
        try {
            emailService.enviarEmailRecuperacion(email, token);
        } catch (Exception e) {
            log.error("Error al enviar email de recuperación: {}", e.getMessage());
            throw new RuntimeException("Error al enviar el correo de recuperación");
        }
    }

    @Override
    public boolean validarToken(String token) {
        log.info("Validando token: {}", token);

        Optional<TokenRecuperacion> tokenOpt = tokenRecuperacionRepository.findTokenValido(token);

        if (tokenOpt.isEmpty()) {
            log.warn("Token no encontrado o ya utilizado: {}", token);
            return false;
        }

        TokenRecuperacion tokenRecuperacion = tokenOpt.get();

        if (tokenRecuperacion.haExpirado()) {
            log.warn("Token expirado: {}", token);
            return false;
        }

        log.info("Token válido para usuario: {}", tokenRecuperacion.getUsuario().getCorreoElectronico());
        return true;
    }

    @Override
    @Transactional
    public void restablecerPassword(String token, String nuevaPassword) {
        log.info("Restableciendo contraseña con token: {}", token);

        Optional<TokenRecuperacion> tokenOpt = tokenRecuperacionRepository.findTokenValido(token);

        if (tokenOpt.isEmpty()) {
            throw new RuntimeException("El token de recuperación es inválido o ha expirado");
        }

        TokenRecuperacion tokenRecuperacion = tokenOpt.get();

        if (tokenRecuperacion.haExpirado()) {
            throw new RuntimeException("El token de recuperación ha expirado");
        }

        Usuario usuario = tokenRecuperacion.getUsuario();

        // Actualizar contraseña
        log.info("Actualizando contraseña para usuario: {}", usuario.getCorreoElectronico());
        usuario.setClaveHash(passwordEncoder.encode(nuevaPassword));

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        log.info("Contraseña actualizada exitosamente para usuario ID: {}", usuarioActualizado.getIdUsuario());

        // Marcar token como utilizado
        tokenRecuperacion.setUtilizado(true);
        tokenRecuperacionRepository.save(tokenRecuperacion);

        log.info("Token marcado como utilizado para usuario: {}", usuario.getCorreoElectronico());
    }

    @Override
    public String generarToken() {
        return UUID.randomUUID().toString();
    }




}
