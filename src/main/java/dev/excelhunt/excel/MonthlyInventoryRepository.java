package dev.excelhunt.excel;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
public interface MonthlyInventoryRepository extends MongoRepository<monthly_inventory, String> {
    @Query("{ '貨品編號' : { $regex: ?0, $options: 'i' } }")
    List<monthly_inventory> searchBy貨品編號(String 貨品編號);

    @Query("{ '貨品名稱' : { $regex: ?0, $options: 'i' } }")
    List<monthly_inventory> searchBy貨品名稱(String 貨品名稱);
}
