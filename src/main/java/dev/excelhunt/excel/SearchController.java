package dev.excelhunt.excel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class SearchController {


    @Autowired
    private StorageInformationRepository storageInformationRepository;

    @Autowired
    private ProductInformationRepository productInformationRepository;

    @Autowired
    private GrossRepository grossRepository;

    @Autowired
    private MonthlyInventoryRepository monthlyInventoryRepository;



    @GetMapping("/")
    public String showSearchForm(HttpSession session, Model model) {
        model.addAttribute("isInitialLoad", true);
        return "main/search";
    }

    @PostMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {

        List<storage_information> storageResults = new ArrayList<>();
        storageResults.addAll(storageInformationRepository.searchByPW(query));
        storageResults.addAll(storageInformationRepository.searchByname(query));
        storageResults.addAll(storageInformationRepository.searchBytypename(query));
        storageResults.addAll(storageInformationRepository.searchBystorid(query));

        List<monthly_inventory> monthlyInventoryResults = new ArrayList<>();
        monthlyInventoryResults.addAll(monthlyInventoryRepository.searchBy貨品編號(query));
        monthlyInventoryResults.addAll(monthlyInventoryRepository.searchBy貨品名稱(query));

        List<gross> grossResults = new ArrayList<>();
        grossResults.addAll(grossRepository.searchBy貨品編號(query));
        grossResults.addAll(grossRepository.searchBy貨品名稱(query));

        List<product_information> productInformationResults = new ArrayList<>();
        productInformationResults.addAll(productInformationRepository.searchBy貨品編號(query));
        productInformationResults.addAll(productInformationRepository.searchBy貨品名稱(query));

        model.addAttribute("isStorageResultsEmpty", storageResults.isEmpty());
        model.addAttribute("isMonthlyInventoryResultsEmpty", monthlyInventoryResults.isEmpty());
        model.addAttribute("isGrossResultsEmpty", grossResults.isEmpty());
        model.addAttribute("isProductInformationResultsEmpty", productInformationResults.isEmpty());

        model.addAttribute("storageResults", storageResults);
        model.addAttribute("monthlyInventoryResults", monthlyInventoryResults);
        model.addAttribute("grossResults", grossResults);
        model.addAttribute("productInformationResults", productInformationResults);

        model.addAttribute("isInitialLoad", false);
        return "main/search";
    }
}