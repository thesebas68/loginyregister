package com.exe.loginyregister.Controller;

import com.exe.loginyregister.Enum.EstadoUsuarioEnum;
import com.exe.loginyregister.Repository.UsuarioRepository;
import com.exe.loginyregister.Service.RecuperacionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import com.exe.loginyregister.Dto.UsuarioDto;
import com.exe.loginyregister.Entity.Usuario;
import com.exe.loginyregister.Service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UsuarioInicoSesionController {
    private final UsuarioService usuarioService;
    private final RecuperacionService recuperacionService;
    @GetMapping("/login")
    public String mostrarLogin(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               Model model) {

        if (error != null) {
            model.addAttribute("error", "Correo electrónico o contraseña incorrectos");
        }

        if (logout != null) {
            model.addAttribute("message", "Ha cerrado sesión exitosamente");
        }

        return "login";
    }

    @GetMapping("/register")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuarioDto", new UsuarioDto());
        return "register";
    }

    @PostMapping("/register")
    public String registrarUsuario(@Valid @ModelAttribute UsuarioDto usuarioDto,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        log.info("Recibiendo solicitud de registro para: {}", usuarioDto.getCorreoElectronico());

        if (result.hasErrors()) {
            log.error("Errores de validación en el registro: {}", result.getAllErrors());
            return "register";
        }

        try {
            usuarioService.registrarUsuario(usuarioDto);
            redirectAttributes.addFlashAttribute("success", "Usuario registrado exitosamente. Puede iniciar sesión.");
            return "redirect:/login";

        } catch (RuntimeException e) {
            log.error("Error al registrar usuario: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    // Metodos para recuperar la contraseña
    @GetMapping("/recuperar")
    public String mostrarRecuperacion() {
        return "recuperar";
    }

    @PostMapping("/recuperar")
    public String procesarRecuperacion(@RequestParam String correo,
                                       RedirectAttributes redirectAttributes) {
        try {
            recuperacionService.solicitarRecuperacion(correo);
            redirectAttributes.addFlashAttribute("success",
                    "Si el correo existe en nuestro sistema, recibirás instrucciones para restablecer tu contraseña.");
        } catch (Exception e) {
            log.error("Error en recuperación: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error",
                    "Error al procesar la solicitud. Por favor, intenta nuevamente.");
        }

        return "redirect:/recuperar";
    }


    @PostMapping("/reset-password")
    public String procesarResetPassword(@RequestParam String token,
                                        @RequestParam String password,
                                        @RequestParam String confirmPassword,
                                        RedirectAttributes redirectAttributes) {
        try {
            System.out.println("=== INICIANDO PROCESO DE RESET PASSWORD ===");
            System.out.println("=== Token recibido: " + token + " ===");
            System.out.println("=== Longitud de password: " + password.length() + " ===");

            if (!password.equals(confirmPassword)) {
                System.out.println("=== ERROR: Las contraseñas no coinciden ===");
                redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden.");
                return "redirect:/reset-password?token=" + token;
            }

            if (password.length() < 6) {
                System.out.println("=== ERROR: Contraseña demasiado corta ===");
                redirectAttributes.addFlashAttribute("error", "La contraseña debe tener al menos 6 caracteres.");
                return "redirect:/reset-password?token=" + token;
            }

            System.out.println("=== LLAMANDO AL SERVICIO DE RECUPERACIÓN ===");
            recuperacionService.restablecerPassword(token, password);
            System.out.println("=== CONTRASEÑA RESTABLECIDA EXITOSAMENTE ===");

            redirectAttributes.addFlashAttribute("success",
                    "Contraseña restablecida exitosamente. Ahora puedes iniciar sesión.");
            return "redirect:/login";

        } catch (RuntimeException e) {
            System.out.println("=== ERROR AL RESTABLECER CONTRASEÑA: " + e.getMessage() + " ===");
            log.error("Error al restablecer contraseña: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reset-password?token=" + token;
        }
    }

    @GetMapping("/check-email")
    @ResponseBody
    public boolean verificarCorreo(@RequestParam String correo) {
        return !usuarioService.existeUsuarioPorCorreo(correo);
    }

}
