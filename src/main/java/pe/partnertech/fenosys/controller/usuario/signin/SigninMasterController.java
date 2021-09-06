/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.controller.usuario.signin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pe.partnertech.fenosys.dto.request.usuario.SigninRequest;
import pe.partnertech.fenosys.dto.response.general.MessageResponse;
import pe.partnertech.fenosys.model.Usuario;
import pe.partnertech.fenosys.security.dto.JwtResponse;
import pe.partnertech.fenosys.security.jwt.JwtProvider;
import pe.partnertech.fenosys.service.IUsuarioService;
import pe.partnertech.fenosys.serviceimpl.UserDetailsImpl;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class SigninMasterController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    IUsuarioService usuarioService;

    @PostMapping("/master/signin")
    public ResponseEntity<?> SignInMaster(@RequestBody SigninRequest signInRequest) {

        Optional<Usuario> master_data = usuarioService.BuscarUsuario_Signin(signInRequest.getUsernameUsuario());

        if (master_data.isPresent()) {
            Usuario master = master_data.get();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(master.getUsernameUsuario(), signInRequest.getPasswordUsuario())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtProvider.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_FENOSIS"))) {
                return new ResponseEntity<>(new JwtResponse(
                        jwt,
                        userDetails.getIdUsuario(),
                        userDetails.getUsername(),
                        userDetails.getAuthorities()
                ), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse("No cumple con los permisos para acceder al sistema."), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Usuario no encontrado en el sistema."), HttpStatus.NOT_FOUND);
        }
    }
}
