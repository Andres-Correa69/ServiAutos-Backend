package co.edu.uniquindio.serviautosbackend.repository;

import co.edu.uniquindio.serviautosbackend.domain.models.ServiceOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceOrderRepository extends MongoRepository<ServiceOrder, String> {
}
