package co.edu.uniquindio.serviautosbackend.service;

import co.edu.uniquindio.serviautosbackend.dto.TechnicianCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.TechnicianDTO;
import co.edu.uniquindio.serviautosbackend.dto.TechnicianUpdateDTO;

import java.util.List;

public interface TechnicianService {

    TechnicianDTO createTechnician(TechnicianCreationDTO dto);

    TechnicianDTO getTechnicianById(String id);

    List<TechnicianDTO> getAllTechnicians();

    List<TechnicianDTO> getActiveTechnicians();

    List<TechnicianDTO> getTechniciansBySpecialization(String specialization);

    TechnicianDTO updateTechnician(String id, TechnicianUpdateDTO dto);

    void deleteTechnician(String id);

    TechnicianDTO activateTechnician(String id);

    TechnicianDTO deactivateTechnician(String id);
}
