package dev.excelhunt.excel;

import com.mongodb.client.*;
import jakarta.servlet.http.HttpSession;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Controller;
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
        return "/login/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        User user = userRepository.findByUsername(username);
        if (user != null &&user.getPassword().equals(password)) {
            session.setAttribute("username", username);
            System.out.println("登入成功");
            return "redirect:/";

        }
        return "/login/login";
    }



//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // 顯示登入頁面
//        request.getRequestDispatcher("/Showloginform/Showloginform.html").forward(request, response);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        // 取得表單資料
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//
//        // 查詢資料庫
//        Bson filter = eq("username", username);
//        Document document = collection.find(filter).first();
//        if (document != null && document.getString("pwd").equals(password)) {
//            // 登入成功，導向首頁
//            response.sendRedirect("//todo 跳轉到登入畫面");
//            return;
//        }
//
//        // 登入失敗，顯示錯誤訊息
//        request.setAttribute("error", "帳戶密碼錯誤");
//        request.getRequestDispatcher("/template/Showloginform/Showloginform.jsp").forward(request, response);
//    }
}
