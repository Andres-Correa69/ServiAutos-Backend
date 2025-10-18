package co.edu.uniquindio.serviautosbackend.repository;

import co.edu.uniquindio.serviautosbackend.domain.models.Technician;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TechnicianRepository extends MongoRepository<Technician, String> {
    List<Technician> findByIsActiveTrue();
    List<Technician> findBySpecialization(String specialization);
}
