package co.edu.uniquindio.serviautosbackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordResetDTO {
    private String email;
    private String code;
    private String newPassword;
}
