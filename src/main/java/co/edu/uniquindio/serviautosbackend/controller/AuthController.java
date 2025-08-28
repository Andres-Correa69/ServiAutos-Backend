package co.edu.uniquindio.serviautosbackend.controller;

import co.edu.uniquindio.serviautosbackend.domain.models.User;
import co.edu.uniquindio.serviautosbackend.dto.AuthResponseDTO;
import co.edu.uniquindio.serviautosbackend.dto.ResponseDTO;
import co.edu.uniquindio.serviautosbackend.dto.LoginDTO;
import co.edu.uniquindio.serviautosbackend.dto.UserCreationDTO;
import co.edu.uniquindio.serviautosbackend.service.AuthService;
import co.edu.uniquindio.serviautosbackend.service.JwtService;
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

    @Autowired
    private JwtService jwtService;



    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        try {
            User user = authService.validateLogin(loginDTO.email(), loginDTO.password());
            String token = jwtService.generateToken(user.getEmail());
            AuthResponseDTO response = new AuthResponseDTO(token, "Login successful");
            return ResponseEntity.ok(new ResponseDTO<>(false, "Login successful", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO<>(true, e.getMessage(), null));
        }
    }


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
