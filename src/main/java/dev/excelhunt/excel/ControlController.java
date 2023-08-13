package dev.excelhunt.excel;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.nio.file.*;
import java.io.IOException;
@RestController
@RequestMapping("/api")
@Controller
public class ControlController {
    @GetMapping("/control")
    public ModelAndView controlPage(HttpSession session) {
        // 檢查session是否包含username
        if (session.getAttribute("username") != null) {
            // 如果用戶已登入，顯示管理頁面
            return new ModelAndView("bk/bk");
        } else {
            // 如果用戶未登入，重定向到登入頁面
            return new ModelAndView("redirect:/login");
        }
    }
    @GetMapping("/read-link")
    public ResponseEntity<String> readLink() {
        try {
            String LINK_FILE_PATH = "改成雲端連結";
            String link = Files.readString(Paths.get(LINK_FILE_PATH));
            return ResponseEntity.ok(link);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading link");
        }
    }



}
