package com.exe.loginyregister.Service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {

    void enviarEmail(String to, String subject, String text);
    void enviarEmailRecuperacion(String to, String token);
}
