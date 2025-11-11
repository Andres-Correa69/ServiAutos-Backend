package co.edu.uniquindio.serviautosbackend.repository;

import co.edu.uniquindio.serviautosbackend.domain.models.Warranty;
import co.edu.uniquindio.serviautosbackend.domain.models.WarrantyStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WarrantyRepository extends MongoRepository<Warranty, String> {
    
    Optional<Warranty> findByServiceOrderId(String serviceOrderId);
    
    List<Warranty> findByClientId(String clientId);
    
    List<Warranty> findByStatus(WarrantyStatus status);
    
    List<Warranty> findByVehicleId(String vehicleId);
    
    List<Warranty> findByStatusAndEndDateAfter(WarrantyStatus status, LocalDateTime date);
    
    List<Warranty> findByEndDateBeforeAndStatusNot(LocalDateTime date, WarrantyStatus status);
}

