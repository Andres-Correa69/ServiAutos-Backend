package co.edu.uniquindio.serviautosbackend.repository;

import co.edu.uniquindio.serviautosbackend.domain.models.SparePart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SparePartRepository extends MongoRepository<SparePart, String> {
}
