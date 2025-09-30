package com.exe.loginyregister.Repository;

import com.exe.loginyregister.Entity.TokenRecuperacion;
import com.exe.loginyregister.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TokenRecuperacionRepository extends JpaRepository<TokenRecuperacion, Long> {
    Optional<TokenRecuperacion> findByToken(String token);
    Optional<TokenRecuperacion> findByUsuarioAndUtilizadoFalse(Usuario usuario);

    @Modifying
    @Transactional
    @Query("UPDATE TokenRecuperacion t SET t.utilizado = true WHERE t.usuario = ?1 AND t.utilizado = false")
    void invalidarTokensAnteriores(Usuario usuario);

    @Query("SELECT t FROM TokenRecuperacion t WHERE t.token = ?1 AND t.utilizado = false")
    Optional<TokenRecuperacion> findTokenValido(String token);
}
