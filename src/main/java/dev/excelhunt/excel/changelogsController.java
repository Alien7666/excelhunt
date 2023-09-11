package dev.excelhunt.excel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/changelogs")
public class changelogsController {
    @GetMapping("/230911")
    public String changelogs230911() {
        return "redirect:https://wind-dinghy-15e.notion.site/20230911-09ef721fe16145b09dfe6aa35cc93da7?pvs=4";
    }
    @GetMapping("/230909")
    public String changelogs230909() {
        return "redirect:https://wind-dinghy-15e.notion.site/9594bfd7e403404abe1038d61a4b1833?pvs=4";
    }
    @GetMapping("/230823")
    public String changelogs230823() {
        return "redirect:https://wind-dinghy-15e.notion.site/Excelhunt-dev-136a0d46fbcf47f4afb14ec518a31be1";
    }
}
