/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.partnertech.fenosys.model.Usuario;
import pe.partnertech.fenosys.model.UtilityToken;

import java.util.Optional;

@Repository
public interface IUsuarioDAO extends JpaRepository<Usuario, Long> {

    //UTU: Utility Tokens de Usuario

    Optional<Usuario> findByUsernameUsuario(String username_usuario);

    Optional<Usuario> findByEmailUsuario(String email_usuario);

    @Query("SELECT u FROM Usuario u WHERE u.usernameUsuario LIKE ?1 OR u.emailUsuario LIKE ?1")
    Optional<Usuario> findByUsernameOrEmail(String username_or_email);

    Optional<Usuario> findByUtilitytokenUsuario(UtilityToken utilitytoken);

    boolean existsByUsernameUsuario(String username_usuario);

    @Query(value = "DELETE " +
            "FROM utilitytokens_usuario utu " +
            "WHERE utu.id_usuario = ?1", nativeQuery = true)
    @Modifying
    void deleteUsuarioFromUTUMiddleTable(Long id_usuario);
}
