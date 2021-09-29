/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.controller.profile.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.partnertech.fenosys.dto.response.general.ImagenResponse;
import pe.partnertech.fenosys.dto.response.general.MessageResponse;
import pe.partnertech.fenosys.dto.response.profile.BasicInfoUserResponse;
import pe.partnertech.fenosys.model.*;
import pe.partnertech.fenosys.service.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class BasicInfoMasterController {

    @Autowired
    IUsuarioService usuarioService;

    @Autowired
    IImagenService imagenService;

    @Autowired
    IPaisService paisService;

    @Autowired
    IDepartamentoService departamentoService;

    @Autowired
    IProvinciaService provinciaService;

    @GetMapping("/master/{id}/basicinfo")
    @PreAuthorize("hasRole('ROLE_FENOSIS')")
    public ResponseEntity<?> MasterProfile(@PathVariable("id") Long id) {

        Optional<Usuario> master_data = usuarioService.BuscarUsuario_ID(id);

        if (master_data.isPresent()) {
            Usuario master = master_data.get();

            Distrito distrito = master.getDistritoUsuario();
            Provincia provincia = BuscarProvincia_Distrito(distrito);
            Departamento departamento = BuscarDepartamento_Provincia(provincia);
            Pais pais = BuscarPais_Departamento(departamento);

            return new ResponseEntity<>(new BasicInfoUserResponse(
                    master.getNombreUsuario(),
                    master.getApellidoUsuario(),
                    pais.getNombrePais(),
                    departamento.getNombreDepartamento(),
                    provincia.getNombreProvincia(),
                    distrito.getNombreDistrito(),
                    master.getEmailUsuario(),
                    new ImagenResponse(
                            master.getImagenUsuario().getNombreImagen(),
                            master.getImagenUsuario().getUrlImagen())
            ), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("No se encuentra información del perfil del usuario."), HttpStatus.NOT_FOUND);
        }
    }

    Provincia BuscarProvincia_Distrito(Distrito distrito) {
        Optional<Provincia> provincia_data = provinciaService.BuscarProvincia_IDDistrito(distrito.getIdDistrito());

        return provincia_data.orElse(null);
    }

    Departamento BuscarDepartamento_Provincia(Provincia provincia) {
        Optional<Departamento> departamento_data = departamentoService.BuscarDepartamento_IDProvincia(provincia.getIdProvincia());

        return departamento_data.orElse(null);
    }

    Pais BuscarPais_Departamento(Departamento departamento) {
        Optional<Pais> pais_data = paisService.BuscarPais_IDDepartamento(departamento.getIdDepartamento());

        return pais_data.orElse(null);
    }
}
