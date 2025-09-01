package co.edu.uniquindio.serviautosbackend.controller;


import co.edu.uniquindio.serviautosbackend.dto.VehicleCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.VehicleDTO;
import co.edu.uniquindio.serviautosbackend.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleDTO> create(@RequestBody VehicleCreationDTO dto) {
        return ResponseEntity.ok(vehicleService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(vehicleService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getAll() {
        return ResponseEntity.ok(vehicleService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> update(@PathVariable String id, @RequestBody VehicleCreationDTO dto) {
        return ResponseEntity.ok(vehicleService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<VehicleDTO>> getByClientId(@PathVariable String clientId) {
        return ResponseEntity.ok(vehicleService.getByClientId(clientId));
    }

}

