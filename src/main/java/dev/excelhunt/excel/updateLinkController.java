package dev.excelhunt.excel;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.regex.Pattern;

@Controller
public class updateLinkController {
    @Autowired
    private linkRepository linkRepository;

    @PostMapping( "/update-link")
    public String updateLink(@RequestParam String newLink , Model Model) {
        if (!isValidGoogleSheetsUrl(newLink)) {
            Model.addAttribute("error", "請提供有效的 Google 試算表的分享網址。");
            List<link> links = linkRepository.findAll();
            if (!links.isEmpty()) {
                link uniqueLink = links.get(0);
                Model.addAttribute("link", uniqueLink.getLink());  // 將連結添加到模型中
            }
            return "/bk/bk";  // 返回到表單頁面
        }
        try {
            List<link> links = linkRepository.findAll();
                if (!links.isEmpty()) {
                    // 如果存在連結，更新它
                    link uniqueLink = links.get(0);
                    uniqueLink.setLink(newLink);
                    linkRepository.save(uniqueLink);
                } else {
                    // 如果沒有找到連結，創建一個新的
                    link newLinkObj = new link();
                    newLinkObj.setLink(newLink);
                    linkRepository.save(newLinkObj);
                }
        } catch (Exception e) {
            // 根據需要返回錯誤頁面
            return "/error/error";
        }
        // 重新導向到管理頁面
        return "redirect:/control";
    }

    private static final Pattern GOOGLE_SHEETS_URL_PATTERN =
            Pattern.compile("^https://docs\\.google\\.com/spreadsheets/d/[^/]+/edit\\?(usp=sharing|usp=drive_link).*$");

    public static boolean isValidGoogleSheetsUrl(String url) {
        return GOOGLE_SHEETS_URL_PATTERN.matcher(url).matches();
    }
}