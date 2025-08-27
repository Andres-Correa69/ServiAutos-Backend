package co.edu.uniquindio.serviautosbackend.repository;

import co.edu.uniquindio.serviautosbackend.domain.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
