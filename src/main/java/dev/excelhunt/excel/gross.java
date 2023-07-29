package dev.excelhunt.excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "毛量")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class gross {
    @Id
    private ObjectId id;

    private String 流水編號;

    private String 類別編號;

    private String 類別名稱;

    private String 貨品編號;

    private String 貨品名稱;

    private String 貨品條碼;

    private String 貨品廠牌;

    private String 備註二;
}
