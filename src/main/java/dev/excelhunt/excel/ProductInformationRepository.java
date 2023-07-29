package dev.excelhunt.excel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
public interface ProductInformationRepository extends MongoRepository<product_information, String> {
    @Query("{ '貨品編號' : { $regex: ?0, $options: 'i' } }")
    List<product_information> searchBy貨品編號(String 貨品編號);

    @Query("{ '貨品名稱' : { $regex: ?0, $options: 'i' } }")
    List<product_information> searchBy貨品名稱(String 貨品名稱);
}