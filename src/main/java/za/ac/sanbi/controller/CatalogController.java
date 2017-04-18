package za.ac.sanbi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hocine on 2017/04/13.
 */
@Controller
public class CatalogController {

    @RequestMapping("/")
    public String goTohomePage() {
        return "homePage";
    }

    @RequestMapping("/login")
    public String goToLoginPage() {
        return "login/loginPage";
    }

    @RequestMapping("/admin")
    public String goToAdminPage() {
        return "admin/adminPage";
    }

}
