package dev.excelhunt.excel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class SettingController {
    @Autowired
    private SearchServicemuticore searchServicemuticore;

    @GetMapping("/setting")
    public String showSettingForm(Model model) {
        List<String> allCollections = searchServicemuticore.getCollections();
        List<String> selectedCollections = searchServicemuticore.getSelectedCollections();
        List<String> selectedFields = searchServicemuticore.getSelectedFields();

        for (String collection : allCollections) {
            Set<String> fields = searchServicemuticore.getFieldsForCollection(collection);
            model.addAttribute(collection, fields);
        }

        model.addAttribute("selectedFields", selectedFields);
        model.addAttribute("collections", allCollections);
        model.addAttribute("selectedCollections", selectedCollections);

        return "setting/setting";
    }

    @PostMapping("/setting/fields")
    public String handleFieldsForm(@RequestParam Map<String, String> allParams) {
        List<String> selectedFields = new ArrayList<>(allParams.keySet());
        searchServicemuticore.saveSelectedFields(selectedFields);
        return "redirect:/setting";
    }
    @PostMapping("/setting/collections")
    public String handleCollectionsForm(@RequestParam Map<String, String> allParams) {
        List<String> selectedCollections = allParams.keySet().stream()
                .filter(key -> key.startsWith("collection_"))
                .map(key -> key.replace("collection_", ""))
                .collect(Collectors.toList());

        searchServicemuticore.saveSelectedCollections(selectedCollections);
        return "redirect:/setting";
    }




}
