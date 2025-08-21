package co.edu.uniquindio.serviautosbackend.controller;

import co.edu.uniquindio.serviautosbackend.domain.models.User;
import co.edu.uniquindio.serviautosbackend.dto.ResponseDTO;
import co.edu.uniquindio.serviautosbackend.dto.UserCreationDTO;
import co.edu.uniquindio.serviautosbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO> signup(@Valid @RequestBody UserCreationDTO userCreationDTO) {
        try {
            User createdUser = authService.createUser(userCreationDTO);
            return new ResponseEntity<>(new ResponseDTO<>(false, "User Created", createdUser), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body( new ResponseDTO<>(true, e.getMessage(), null) );
        }
    }

}
