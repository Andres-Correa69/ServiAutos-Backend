package co.edu.uniquindio.serviautosbackend.controller;

import co.edu.uniquindio.serviautosbackend.dto.TechnicianCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.TechnicianDTO;
import co.edu.uniquindio.serviautosbackend.dto.TechnicianUpdateDTO;
import co.edu.uniquindio.serviautosbackend.service.TechnicianService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/technicians")
@RequiredArgsConstructor
public class TechnicianController {

    @Autowired
    private TechnicianService technicianService;

    @PostMapping
    public ResponseEntity<TechnicianDTO> create(@RequestBody TechnicianCreationDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(technicianService.createTechnician(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TechnicianDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(technicianService.getTechnicianById(id));
    }

    @GetMapping
    public ResponseEntity<List<TechnicianDTO>> getAll() {
        return ResponseEntity.ok(technicianService.getAllTechnicians());
    }

    @GetMapping("/active")
    public ResponseEntity<List<TechnicianDTO>> getActive() {
        return ResponseEntity.ok(technicianService.getActiveTechnicians());
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<TechnicianDTO>> getBySpecialization(@PathVariable String specialization) {
        return ResponseEntity.ok(technicianService.getTechniciansBySpecialization(specialization));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TechnicianDTO> update(@PathVariable String id, @RequestBody TechnicianUpdateDTO dto) {
        return ResponseEntity.ok(technicianService.updateTechnician(id, dto));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<TechnicianDTO> activate(@PathVariable String id) {
        return ResponseEntity.ok(technicianService.activateTechnician(id));
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<TechnicianDTO> deactivate(@PathVariable String id) {
        return ResponseEntity.ok(technicianService.deactivateTechnician(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        technicianService.deleteTechnician(id);
        return ResponseEntity.noContent().build();
    }
}
