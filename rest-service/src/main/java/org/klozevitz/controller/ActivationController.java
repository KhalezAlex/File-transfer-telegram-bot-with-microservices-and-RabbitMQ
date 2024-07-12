package org.klozevitz.controller;

import lombok.RequiredArgsConstructor;
import org.klozevitz.service.interfaces.UserActivationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class ActivationController {
    private final UserActivationService userActivationService;

    /**
     * actually user can type ghost id and in this case whe should return BAD_REQUEST,
     * but we are trying to implement MVP and will do it later
     * */

    @RequestMapping(method = RequestMethod.GET, value = "/activation")
    public ResponseEntity<?> activation(@RequestParam("id") String id) {
        return userActivationService.activation(id)
                ? ResponseEntity.ok().body("Activation completed")
                : ResponseEntity.internalServerError().build();

//        var result = userActivationService.activation(id);
//        if (result) {
//            return ResponseEntity.ok().body("Activation completed.");
//        }
//        return ResponseEntity.internalServerError().build();
    }
}
