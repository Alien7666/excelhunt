package dev.excelhunt.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;
@SuppressWarnings("NonAsciiCharacters")
@Document(collection = "儲位資料")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class storage_information {
    @Id
    private ObjectId id;

    private String 編號;

    private String 產品編號;

    private String 品名規格;

    private String 商品編號;

    private String 標示名稱;

    private String 儲位資料;

    private String 產品毛重;

    private String 上月庫存;
}
