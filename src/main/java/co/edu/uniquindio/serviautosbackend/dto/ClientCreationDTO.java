package co.edu.uniquindio.serviautosbackend.dto;


public record ClientCreationDTO(
        String name,
        String lastName,
        String document,
        String email,
        String address,
        String phone
) {}
