/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.partnertech.fenosys.model.RestoreToken;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IRestoreTokenDAO extends JpaRepository<RestoreToken, Long> {

    Optional<RestoreToken> findByTokenRestoreToken(String token);

    @Query(value = "DELETE " +
            "FROM restoretokens_usuario rtu " +
            "WHERE rtu.id_restoretoken = ?1", nativeQuery = true)
    @Modifying
    void deleteRestoreTokenFromMiddleTable(Long id);

    @Query("SELECT rt FROM RestoreToken rt WHERE rt.expiracionRestoreToken < ?1 AND rt.razonRestoreToken LIKE ?2")
    Set<RestoreToken> findRestoreTokenByExpiracionAndRazon(LocalDateTime expiracion, String razon);
}
