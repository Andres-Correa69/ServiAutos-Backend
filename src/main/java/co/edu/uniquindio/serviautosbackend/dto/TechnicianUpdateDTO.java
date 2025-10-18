package co.edu.uniquindio.serviautosbackend.dto;

public record TechnicianUpdateDTO(
        String name,
        String email,
        String phone,
        String specialization,
        Boolean isActive
) {}
