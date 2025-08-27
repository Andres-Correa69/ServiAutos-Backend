package co.edu.uniquindio.serviautosbackend.repository;

import co.edu.uniquindio.serviautosbackend.domain.models.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends MongoRepository<Client, String> {
}
