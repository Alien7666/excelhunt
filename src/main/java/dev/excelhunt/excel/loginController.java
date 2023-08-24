package dev.excelhunt.excel;

import com.mongodb.client.*;
import jakarta.servlet.http.HttpSession;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.mongodb.client.model.Filters.eq;

@Controller
public class loginController {

    @Autowired
    private UserRepository userRepository;

    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> collection;


    public void init() {
        // 建立MongoDB連線
        client = MongoClients.create();
        database = client.getDatabase("login-pwd");
        collection = database.getCollection("user");
    }


    public void destroy() {
        // 關閉資料庫連接
        client.close();
    }

    @GetMapping("/login")
    public String Showloginform(){
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        User user = userRepository.findByUsername(username);
        if (user != null &&user.getPassword().equals(password)) {
            session.setAttribute("username", username);
            System.out.println("登入成功");
            return "redirect:/control";
        }
        model.addAttribute("errorMessage", "帳號或密碼錯誤!");
        return "login/login";
    }
}
