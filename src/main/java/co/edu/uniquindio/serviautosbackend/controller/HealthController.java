package co.edu.uniquindio.serviautosbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, String>> health() {
        // Health check simple y rápido para Railway
        // Responde inmediatamente sin verificar nada
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}

