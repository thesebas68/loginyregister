package com.exe.loginyregister.Controller;

import com.exe.loginyregister.Enum.EstadoUsuarioEnum;
import com.exe.loginyregister.Repository.UsuarioRepository;
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

    @GetMapping("/recuperar")
    public String mostrarRecuperacion() {
        return "recuperar";
    }

    @PostMapping("/recuperar")
    public String procesarRecuperacion(@RequestParam String correo,
                                       RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorCorreo(correo);

        if (usuario.isPresent()) {
            redirectAttributes.addFlashAttribute("success", "Se han enviado instrucciones a su correo electrónico");
        } else {
            redirectAttributes.addFlashAttribute("error", "Correo electrónico no encontrado");
        }

        return "redirect:/recuperar";
    }

    @GetMapping("/check-email")
    @ResponseBody
    public boolean verificarCorreo(@RequestParam String correo) {
        return !usuarioService.existeUsuarioPorCorreo(correo);
    }

}
