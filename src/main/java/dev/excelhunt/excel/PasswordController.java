package dev.excelhunt.excel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/change-password")
    public String changePasswordForm() {
        return "change-pwd/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(String currentPassword, String newPassword, String confirmPassword, RedirectAttributes redirectAttributes) {
        // 取得目前登入的使用者
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        // 驗證目前密碼是否正確
        if (!passwordEncoder.matches(currentPassword, user.getPwd())) {
            redirectAttributes.addFlashAttribute("message", "目前密碼不正確!");
            return "redirect:/change-password";
        }

        // 驗證新密碼是否與確認密碼相符
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("message", "新密碼與確認密碼不相符!");
            return "redirect:/change-password";
        }
        // 驗證新密碼是否與舊密碼相同
        if (newPassword.equals(currentPassword)) {
            redirectAttributes.addFlashAttribute("message", "新密碼與舊密碼相同!");
            return "redirect:/change-password";
        }

        // 更新密碼
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("alertMessage", "密碼更新成功，請重新登入。");
        return "redirect:/change-password";
    }
}
