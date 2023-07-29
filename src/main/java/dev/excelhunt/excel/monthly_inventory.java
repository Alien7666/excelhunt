package dev.excelhunt.excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "月庫存")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class monthly_inventory {
    @Id
    private ObjectId id;

    private int 編號;

    private String 貨品編號;

    private String 貨品名稱;

    private String Unnamed_3;

    private String Unnamed_4;

    private String 期初數量;

    private String 本期增加;

    private String 本期減少;

    private String 期末數量;

    private String 龜山倉庫;

    private String 庫存數量;

    private String 進貨單價;

    private String 進貨數量;
}
