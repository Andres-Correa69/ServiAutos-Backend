package co.edu.uniquindio.serviautosbackend.dto;

public record TechnicianDTO(
        String id,
        String name,
        String email,
        String phone,
        String specialization,
        Boolean isActive
) {}
