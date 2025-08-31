package co.edu.uniquindio.serviautosbackend.controller;

import co.edu.uniquindio.serviautosbackend.domain.models.User;
import co.edu.uniquindio.serviautosbackend.dto.*;
import co.edu.uniquindio.serviautosbackend.service.AuthService;
import co.edu.uniquindio.serviautosbackend.service.EmailService;
import co.edu.uniquindio.serviautosbackend.service.JwtService;
import co.edu.uniquindio.serviautosbackend.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeService verificationService;

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



    @PostMapping("/signup/request")
    public ResponseEntity<ResponseDTO> requestSignup(@Valid @RequestBody UserCreationDTO userCreationDTO) {
        try {
            if (authService.existsByEmail(userCreationDTO.email())) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDTO<>(true, "Email ya registrado", null));
            }

            String code = verificationService.generateCode(userCreationDTO.email(), userCreationDTO);
            // 🔑 Guardar el usuario pendiente junto al email
            verificationService.savePendingUser(userCreationDTO.email(), userCreationDTO);

            EmailDTO emailDTO = EmailDTO.builder()
                    .to(verificationService.getAdminEmail())
                    .subject("Verificación nuevo usuario - ServiAutos")
                    .body("Código de verificación para nuevo usuario (" +
                            userCreationDTO.email() + "): " + code)
                    .build();
            emailService.sendEmail(emailDTO);

            return ResponseEntity.ok(new ResponseDTO<>(false,
                    "Solicitud enviada al administrador", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, e.getMessage(), null));
        }
    }


    @PostMapping("/signup/verify")
    public ResponseEntity<ResponseDTO> verifySignup(
            @RequestParam String email,
            @RequestParam String code) {
        try {
            if (!verificationService.validateCode(email, code)) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDTO<>(true, "Código inválido o expirado", null));
            }

            UserCreationDTO pendingUser = verificationService.getPendingUser(email);
            User createdUser = authService.createUser(pendingUser);

            verificationService.removePendingUser(email);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO<>(false, "Usuario creado exitosamente", createdUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, e.getMessage(), null));
        }


    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDTO> forgotPassword(@RequestParam String email) {
        try {
            if (!authService.existsByEmail(email)) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDTO<>(true, "Email no encontrado", null));
            }

            String code = verificationService.generatePasswordResetCode(email);

            EmailDTO emailDTO = EmailDTO.builder()
                    .to(email)
                    .subject("Recuperación de contraseña - ServiAutos")
                    .body("Tu código de recuperación es: " + code)
                    .build();
            emailService.sendEmail(emailDTO);

            return ResponseEntity.ok(new ResponseDTO<>(false,
                    "Código enviado al correo", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, e.getMessage(), null));
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestBody PasswordResetDTO resetDTO) {
        try {
            if (!verificationService.validatePasswordResetCode(
                    resetDTO.getEmail(), resetDTO.getCode())) {
                return ResponseEntity.badRequest()
                        .body(new ResponseDTO<>(true, "Código inválido o expirado", null));
            }

            authService.updatePassword(resetDTO.getEmail(), resetDTO.getNewPassword());
            return ResponseEntity.ok(new ResponseDTO<>(false,
                    "Contraseña actualizada exitosamente", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO<>(true, e.getMessage(), null));
        }
    }




}
