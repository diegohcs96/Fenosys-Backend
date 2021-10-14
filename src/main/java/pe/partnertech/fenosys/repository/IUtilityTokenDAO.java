/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.partnertech.fenosys.model.UtilityToken;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IUtilityTokenDAO extends JpaRepository<UtilityToken, Long> {

    Optional<UtilityToken> findByTokenUtilityToken(String token_utilitytoken);

    @Query(value = "DELETE " +
            "FROM utilitytokens_usuario utu " +
            "WHERE utu.id_utilitytoken = ?1", nativeQuery = true)
    @Modifying
    void deleteUtilityTokenFromMiddleTable(Long id_utilitytoken);

    @Query("SELECT ut FROM UtilityToken ut WHERE ut.expiracionUtilityToken < ?1 AND ut.razonUtilityToken LIKE ?2")
    Set<UtilityToken> findUtilityTokenByExpiracionAndRazon(LocalDateTime expiracion_utilitytoken, String razon_utilitytoken);

    @Query(value = "SELECT utu.*, ut.*, u.* " +
            "FROM utilitytokens_usuario utu " +
            "JOIN utilitytoken ut ON utu.id_utilitytoken = ut.id_utilitytoken " +
            "JOIN usuario u ON utu.id_usuario = u.id_usuario " +
            "WHERE u.id_usuario = ?1 AND ut.razon_utilitytoken LIKE ?2", nativeQuery = true)
    Set<UtilityToken> findUtilityTokensByIDUsuarioAndRazon(Long id_usuario, String razon_utilitytoken);
}
