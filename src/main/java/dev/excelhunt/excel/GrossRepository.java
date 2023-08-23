package dev.excelhunt.excel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface GrossRepository extends MongoRepository<gross, String> {
    @Query("{ '貨品編號' : { $regex: ?0, $options: 'i' } }")
    List<gross> searchBy貨品編號(String 貨品編號);

    @Query("{ '貨品名稱' : { $regex: ?0, $options: 'i' } }")
    List<gross> searchBy貨品名稱(String 貨品名稱);

    @Query("{ '流水編號' : { $regex: ?0, $options: 'i' } }")
    List<gross> searchBy流水編號(String 流水編號);

    @Query("{ '貨品條碼' : { $regex: ?0, $options: 'i' } }")
    List<gross> searchBy貨品條碼(String 貨品條碼);

    @Query("{ '貨品廠牌' : { $regex: ?0, $options: 'i' } }")
    List<gross> searchBy貨品廠牌(String 貨品廠牌);

    @Query("{ '類別編號' : { $regex: ?0, $options: 'i' } }")
    List<gross> searchBy類別編號(String 類別編號);

    @Query("{ '類別名稱' : { $regex: ?0, $options: 'i' } }")
    List<gross> searchBy類別名稱(String 類別名稱);
}
