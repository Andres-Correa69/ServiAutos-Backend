package co.edu.uniquindio.serviautosbackend.dto;

import lombok.Builder;

@Builder
public record VerificationCodeDataDTO(
        String code,
        long timestamp,
        UserCreationDTO pendingUser
) {
}
