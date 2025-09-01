package co.edu.uniquindio.serviautosbackend.service;

import co.edu.uniquindio.serviautosbackend.dto.VehicleCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.VehicleDTO;

import java.util.List;

public interface VehicleService {

    VehicleDTO create(VehicleCreationDTO dto);

    VehicleDTO getById(String id);

    List<VehicleDTO> getAll();

    VehicleDTO update(String id, VehicleCreationDTO dto);

    void delete(String id);

    List<VehicleDTO> getByClientId(String clientId);
}
