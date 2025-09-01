package co.edu.uniquindio.serviautosbackend.dto;


public record ClientDTO(
        String id,
        String name,
        String lastName,
        String document,
        String email,
        String address,
        String phone
) {}
