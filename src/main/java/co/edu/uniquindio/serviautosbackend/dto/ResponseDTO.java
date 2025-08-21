package co.edu.uniquindio.serviautosbackend.dto;

public record ResponseDTO<T>(boolean error, String message, T data) {
}
