/*
 * Copyright (c) 2021. Developed by Diego Campos Sandoval.
 */

package pe.partnertech.fenosys.controller.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import pe.partnertech.fenosys.model.RestoreToken;
import pe.partnertech.fenosys.service.IRestoreTokenService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CheckTokenController {

    @Autowired
    IRestoreTokenService restoreTokenService;
    @Value("${front.baseurl}")
    private String baseurl;

    @PostMapping("/check/token/{token}")
    void RedirectSignupAdminRequest(HttpServletResponse response, @PathVariable(value = "token") String token) throws IOException {

        Optional<RestoreToken> restoretoken_data = restoreTokenService.BuscarRestoreToken_Token(token);

        if (restoretoken_data.isPresent()) {
            response.sendRedirect(baseurl + "/signup/admin");
        } else {
            response.sendRedirect(baseurl);
        }
    }
}
