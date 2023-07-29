package dev.excelhunt.excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "產品資料")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class product_information {
    @Id
    private ObjectId id;

    private String Unnamed_0;

    private String 貨品編號;

    private String 貨品名稱;

    private String Unnamed_3;

    private String 期末數量;
}
