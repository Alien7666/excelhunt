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
import java.util.concurrent.ExecutionException;


@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;
    @Autowired
    private SearchServicemuticore searchServicemuticore;

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
        // 获取当前时间
        long startTime = System.currentTimeMillis();
        // 调用搜索服务并获取结果
        List<Map<String, Object>> searchResults = null;
        try {
            searchResults = searchServicemuticore.multiCollectionSearch(query);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();

        String totalTime = "Total search time: " + (endTime - startTime) + "ms";
        // 将结果添加到Model中
        model.addAttribute("searchResults", searchResults);
        String keyword = "現在顯示的結果為:"+query;
        model.addAttribute("keyword", keyword);

        // 保存搜索纪录
        String userId = "Guest";
        if (authentication != null && authentication.isAuthenticated()) {
            // 获取用户username
            userId = authentication.getName();
        }
        searchServicemuticore.saveSearchRecord(query, userId , totalTime);

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