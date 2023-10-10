package dev.excelhunt.excel;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//改變DATABASE的名稱

@Document(collection = "link")
public class link {
    @Getter
    @Id
    private String id;

    private String link;


    public void setId(String id) {
        this.id = id;
    }

    public void setLink(String link) {
        this.link = link;
    }
    public String getLink() {
        return link;
    }
}
