package dev.excelhunt.excel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class powderController {
    @GetMapping("/password-encryption-tool")
    public String passwordEncryptionPage() {
        return "pwd-encryption/password-encryption";
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/encrypt-password")
    public String encryptPassword(@RequestParam String rawPassword, Model model) {
        String encrypted = passwordEncoder.encode(rawPassword);
        model.addAttribute("encryptedPassword", encrypted);
        return "pwd-encryption/password-encryption";
    }

}
