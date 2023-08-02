package dev.excelhunt.excel;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//改變DATABASE的名稱

@Document(collection = "user")
public class User {
    @Id
    private String id;
    private String username;
    @Getter
    private String pwd;

    private String Role;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return pwd;
    }

    public void setPassword(String password) {
        this.pwd = password;
    }
}
