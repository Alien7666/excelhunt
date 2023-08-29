package dev.excelhunt.excel;

//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class logoutController{

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // 创建一个名为 JSESSIONID 的新 Cookie
        Cookie cookie = new Cookie("JSESSIONID", null);
        // 设置 Max-Age 为 0，这将导致 Cookie 立即过期
        cookie.setMaxAge(0);
        // 如果您的应用使用了特定的路径或域，也需要设置这些属性
        cookie.setPath("/");
        // 将 Cookie 添加到响应中
        response.addCookie(cookie);

        // 这里可以进行其他的注销逻辑，如使 JWT 令牌失效、清除安全上下文等

        // 重定向到主页或其他页面
        return "redirect:/";
    }
}
