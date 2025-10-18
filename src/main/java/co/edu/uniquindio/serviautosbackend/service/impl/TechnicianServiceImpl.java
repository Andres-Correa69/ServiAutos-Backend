package co.edu.uniquindio.serviautosbackend.service.impl;

import co.edu.uniquindio.serviautosbackend.domain.models.Technician;
import co.edu.uniquindio.serviautosbackend.dto.TechnicianCreationDTO;
import co.edu.uniquindio.serviautosbackend.dto.TechnicianDTO;
import co.edu.uniquindio.serviautosbackend.dto.TechnicianUpdateDTO;
import co.edu.uniquindio.serviautosbackend.repository.TechnicianRepository;
import co.edu.uniquindio.serviautosbackend.service.TechnicianService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnicianServiceImpl implements TechnicianService {

    @Autowired
    private TechnicianRepository technicianRepository;

    @Override
    public TechnicianDTO createTechnician(TechnicianCreationDTO dto) {
        Technician technician = new Technician();
        technician.setName(dto.name());
        technician.setEmail(dto.email());
        technician.setPhone(dto.phone());
        technician.setSpecialization(dto.specialization());
        technician.setIsActive(true);

        Technician saved = technicianRepository.save(technician);
        return mapToDTO(saved);
    }

    @Override
    public TechnicianDTO getTechnicianById(String id) {
        Technician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));
        return mapToDTO(technician);
    }

    @Override
    public List<TechnicianDTO> getAllTechnicians() {
        return technicianRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<TechnicianDTO> getActiveTechnicians() {
        return technicianRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<TechnicianDTO> getTechniciansBySpecialization(String specialization) {
        return technicianRepository.findBySpecialization(specialization)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public TechnicianDTO updateTechnician(String id, TechnicianUpdateDTO dto) {
        Technician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));

        technician.setName(dto.name());
        technician.setEmail(dto.email());
        technician.setPhone(dto.phone());
        technician.setSpecialization(dto.specialization());
        technician.setIsActive(dto.isActive());

        Technician updated = technicianRepository.save(technician);
        return mapToDTO(updated);
    }

    @Override
    public void deleteTechnician(String id) {
        technicianRepository.deleteById(id);
    }

    @Override
    public TechnicianDTO activateTechnician(String id) {
        Technician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));
        technician.setIsActive(true);
        Technician updated = technicianRepository.save(technician);
        return mapToDTO(updated);
    }

    @Override
    public TechnicianDTO deactivateTechnician(String id) {
        Technician technician = technicianRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));
        technician.setIsActive(false);
        Technician updated = technicianRepository.save(technician);
        return mapToDTO(updated);
    }

    private TechnicianDTO mapToDTO(Technician technician) {
        return new TechnicianDTO(
                technician.getId(),
                technician.getName(),
                technician.getEmail(),
                technician.getPhone(),
                technician.getSpecialization(),
                technician.getIsActive()
        );
    }
}
