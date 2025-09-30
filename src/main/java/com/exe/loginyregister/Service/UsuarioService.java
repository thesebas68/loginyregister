package com.exe.loginyregister.Service;

import com.exe.loginyregister.Dto.UsuarioDto;
import com.exe.loginyregister.Entity.Usuario;

import java.util.Optional;


public interface UsuarioService {

    Usuario registrarUsuario(UsuarioDto usuarioDto);
    Optional<Usuario> obtenerUsuarioPorCorreo(String correo);
    boolean existeUsuarioPorCorreo(String correo);
    boolean existeUsuarioPorIdentificacion(String identificacion);
}
