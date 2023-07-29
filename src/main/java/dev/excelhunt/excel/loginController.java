package dev.excelhunt.excel;

import com.mongodb.client.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;

import static com.mongodb.client.model.Filters.eq;

@WebServlet("/login")
public class loginController extends HttpServlet {
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @Override
    public void init() {
        // 建立MongoDB連線
        client = MongoClients.create();
        database = client.getDatabase("login-pwd");
        collection = database.getCollection("users");
    }

    @Override
    public void destroy() {
        // 關閉資料庫連接
        client.close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 顯示登入頁面
        request.getRequestDispatcher("WEB-INF/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 取得表單資料
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 查詢資料庫
        Bson filter = eq("username", username);
        Document document = collection.find(filter).first();
        if (document != null && document.getString("pwd").equals(password)) {
            // 登入成功，導向首頁
            response.sendRedirect("//todo 跳轉到登入畫面");
            return;
        }

        // 登入失敗，顯示錯誤訊息
        request.setAttribute("error", "帳戶密碼錯誤");
        request.getRequestDispatcher("WEB-INF/login.jsp").forward(request, response);
    }
}
