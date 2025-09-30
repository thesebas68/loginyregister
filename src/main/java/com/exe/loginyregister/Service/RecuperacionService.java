package com.exe.loginyregister.Service;

import org.springframework.stereotype.Service;

@Service
public interface RecuperacionService {
    void solicitarRecuperacion(String email);
    boolean validarToken(String token);
    void restablecerPassword(String token, String nuevaPassword);
    String generarToken();
}
