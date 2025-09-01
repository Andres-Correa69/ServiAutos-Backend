package co.edu.uniquindio.serviautosbackend.service.impl;

import co.edu.uniquindio.serviautosbackend.domain.models.Vehicle;
import co.edu.uniquindio.serviautosbackend.dto.VehicleCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.VehicleDTO;
import co.edu.uniquindio.serviautosbackend.repository.VehicleRepository;
import co.edu.uniquindio.serviautosbackend.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public VehicleDTO create(VehicleCreationDTO dto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicencePlate(dto.licencePlate());
        vehicle.setBrand(dto.brand());
        vehicle.setModel(dto.model());
        vehicle.setClientId(dto.clientId()); // ✅ asociación con cliente

        return mapToDTO(vehicleRepository.save(vehicle));
    }


    @Override
    public VehicleDTO getById(String id) {
        return vehicleRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
    }

    @Override
    public List<VehicleDTO> getAll() {
        return vehicleRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public VehicleDTO update(String id, VehicleCreationDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        vehicle.setLicencePlate(dto.licencePlate());
        vehicle.setBrand(dto.brand());
        vehicle.setModel(dto.model());
        vehicle.setClientId(dto.clientId());

        return mapToDTO(vehicleRepository.save(vehicle));
    }
    @Override
    public void delete(String id) {
        vehicleRepository.deleteById(id);
    }

    @Override
    public List<VehicleDTO> getByClientId(String clientId) {
        return vehicleRepository.findByClientId(clientId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }


    private VehicleDTO mapToDTO(Vehicle vehicle) {
        return new VehicleDTO(
                vehicle.getId(),
                vehicle.getLicencePlate(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getClientId()
        );
    }
}
