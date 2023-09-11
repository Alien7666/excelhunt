package dev.excelhunt.excel;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MenuController {

    @GetMapping("/getMenuItems")
    public ResponseEntity<List<MenuItem>> getMenuItems(@AuthenticationPrincipal UserDetails userDetails) {
        List<MenuItem> menuItems = new ArrayList<>();
        if (userDetails.getAuthorities().toString().contains("ROLE_ADMIN")) {
            menuItems.add(new MenuItem("密碼加密工具" ,"/api/password-encryption-tool"));
            menuItems.add(new MenuItem("更改密碼" ,"/change-password"));
            menuItems.add(new MenuItem("更改要顯示和搜尋的內容","/setting"));
            menuItems.add(new MenuItem("",""));
        } else if (userDetails.getAuthorities().toString().contains("ROLE_USER")) {
            menuItems.add(new MenuItem("更改密碼" ,"/change-password"));
            menuItems.add(new MenuItem("更改要顯示和搜尋的內容","/setting"));
            menuItems.add(new MenuItem("暫無功能(按下回主頁)","/"));
        }
        return ResponseEntity.ok(menuItems);
    }

    private static class MenuItem {
        private String name;
        private String url;

        public MenuItem(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public String getName() { return name; }
        public String getUrl() { return url; }
    }

}

