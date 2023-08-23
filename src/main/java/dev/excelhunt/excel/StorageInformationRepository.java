package dev.excelhunt.excel;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;


public interface StorageInformationRepository extends MongoRepository<storage_information, String> {
    @Query("{ '產品編號' : { $regex: ?0, $options: 'i' } }")
    List<storage_information> searchByPW(String PW);

    @Query("{ '品名規格' : { $regex: ?0, $options: 'i' } }")
    List<storage_information> searchByname(String name);

    @Query("{ '標示名稱' : { $regex: ?0, $options: 'i' } }")
    List<storage_information> searchBytypename(String typename);

    @Query("{ '儲位資料' : { $regex: ?0, $options: 'i' } }")
    List<storage_information> searchBystorid(String storid);

    @Query("{ '產品編號_1' : { $regex: ?0, $options: 'i' } }")
    List<storage_information> searchBy產品編號_1(String 產品編號_1);
}
