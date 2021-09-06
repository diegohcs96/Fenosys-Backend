/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.partnertech.fenosys.model.RestoreToken;
import pe.partnertech.fenosys.model.Usuario;

import java.util.Optional;

@Repository
public interface IUsuarioDAO extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsernameUsuario(String username);

    Optional<Usuario> findByEmailUsuario(String email);

    @Query("SELECT u FROM Usuario u WHERE u.usernameUsuario LIKE ?1 OR u.emailUsuario LIKE ?1")
    Optional<Usuario> findByUsernameOrEmail(String username_email);

    Optional<Usuario> findByRestoretokenUsuario(RestoreToken restoreToken);

    boolean existsByUsernameUsuario(String username);

    boolean existsByEmailUsuario(String email);

    @Query(value = "DELETE " +
            "FROM restoretokens_usuario rtu " +
            "WHERE rtu.id_usuario = ?1", nativeQuery = true)
    @Modifying
    void deleteUsuarioFromRTUMiddleTable(Long id);
}
