package co.edu.uniquindio.serviautosbackend.dto;

import java.time.LocalDateTime;

public record UserCreationDTO(
        String name,
        String lastName,
        String phone,
        String address,
        String email,
        String password,
        LocalDateTime registerDate
) {}
