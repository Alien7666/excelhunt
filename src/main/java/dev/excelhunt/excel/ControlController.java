package dev.excelhunt.excel;

import com.google.cloud.storage.*;
//import jakarta.servlet.http.HttpSession;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.core.io.ClassPathResource;
import java.io.FileInputStream;
import java.io.IOException;

import java.nio.charset.StandardCharsets;


@Controller
public class ControlController {

    private static final String BUCKET_NAME = "link_file";
    private static final String OBJECT_NAME = "link_file.txt";
    private Storage storage;

    public ControlController() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/excel-cloud-search-703c4ad642b3.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
        storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }

    @GetMapping("/control")
    public ModelAndView controlPage(Authentication authentication) {
        ModelAndView modelAndView;
        // 檢查authentication是否為null
        if (authentication != null) {
            // 如果用戶已登入，顯示管理頁面
            modelAndView = new ModelAndView("bk/bk");

            // Retrieve the link from GCS
            String link = readLinkFromGCS();
            modelAndView.addObject("link", link);

        } else {
            // 如果用戶未登入，重定向到登入頁面
            modelAndView = new ModelAndView("redirect:/login");
        }
        return modelAndView;
    }

    private String readLinkFromGCS() {
        Blob blob = storage.get(BlobId.of(BUCKET_NAME, OBJECT_NAME));
        if (blob == null) {

            System.out.println("Blob not found for BUCKET: " + BUCKET_NAME + " and OBJECT: " + OBJECT_NAME);

            return "無法存取連結";
        }
        return new String(blob.getContent(), StandardCharsets.UTF_8);
    }
    @PostMapping("/update-link")
    public String updateLink(@RequestParam String newLink) {
        // Update the link in GCS
        byte[] bytes = newLink.getBytes(StandardCharsets.UTF_8);
        BlobId blobId = BlobId.of(BUCKET_NAME, OBJECT_NAME);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/plain").build();
        storage.create(blobInfo, bytes);

        return "redirect:/control";
    }


}
