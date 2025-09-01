package co.edu.uniquindio.serviautosbackend.repository;

import co.edu.uniquindio.serviautosbackend.domain.models.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends MongoRepository<Vehicle, String> {
    List<Vehicle> findByClientId(String clientId);
}
