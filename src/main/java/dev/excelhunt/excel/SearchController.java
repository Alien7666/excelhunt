package dev.excelhunt.excel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
//import jakarta.servlet.http.HttpSession;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/")
    public String showSearchForm(HttpSession session, Model model, Authentication authentication) {
        model.addAttribute("isInitialLoad", true);
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }
        return "main/search";
    }

    @PostMapping("/search")
    public String search(@RequestParam("query") String query, Model model, Authentication authentication) {
        long startTime = System.currentTimeMillis();

        // 调用搜索服务并获取结果
        List<Map<String, Object>> searchResults = searchService.multiCollectionSearch(query);

        long endTime = System.currentTimeMillis();
        System.out.println("Total search time: " + (endTime - startTime) + "ms");
        // 将结果添加到Model中
        model.addAttribute("searchResults", searchResults);
        System.out.println("searchResults:" + searchResults);

        // 保存搜索纪录
        String userId = "Guest";
        if (authentication != null && authentication.isAuthenticated()) {
            // 获取用户username
            userId = authentication.getName();
        }
        searchService.saveSearchRecord(query, userId);

        if (authentication != null) {
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }
        model.addAttribute("isInitialLoad", false);

        // 返回search.html模板
        return "main/search";
    }
}