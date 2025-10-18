package co.edu.uniquindio.serviautosbackend.repository;

import co.edu.uniquindio.serviautosbackend.domain.models.SparePart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SparePartRepository extends MongoRepository<SparePart, String> {
    List<SparePart> findByCategory(String category);
    List<SparePart> findByBrand(String brand);
    List<SparePart> findByAvailableStockLessThan(Integer stock);
    List<SparePart> findByNameContainingIgnoreCase(String name);
}
