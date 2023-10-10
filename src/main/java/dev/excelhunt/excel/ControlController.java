package dev.excelhunt.excel;

import com.google.cloud.storage.*;
//import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.util.List;


@Controller
public class ControlController {

    private static final String BUCKET_NAME = "link_file";
    private static final String OBJECT_NAME = "link_file.txt";
    private Storage storage;

    @Autowired
    private linkRepository linkRepository;


    public ControlController() throws IOException {
    }

    @GetMapping("/control")
    public ModelAndView controlPage(Authentication authentication) {
        ModelAndView modelAndView;
        // 檢查authentication是否為null
        if (authentication != null) {
            // 如果用戶已登入，顯示管理頁面
            modelAndView = new ModelAndView("bk/bk");

            // Retrieve the link from GCS
            String link = readlinkFromMongo();
            modelAndView.addObject("link", link);

        } else {
            // 如果用戶未登入，重定向到登入頁面
            modelAndView = new ModelAndView("redirect:/login");
        }
        return modelAndView;
    }
    private String readlinkFromMongo() {
        //取得mongo資料庫中的link
        List<link> links = linkRepository.findAll();
        if (links.isEmpty()) {
            return null;
        }
        return links.get(0).getLink();
    }
}
