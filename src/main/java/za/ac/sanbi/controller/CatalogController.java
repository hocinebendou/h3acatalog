package za.ac.sanbi.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import za.ac.sanbi.login.LoginForm;
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


    @RequestMapping(value = "/login", params = {"enter"}, method = RequestMethod.POST)
    public String loginIn(@Valid LoginForm loginForm, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "login/loginPage";
        }
        return "admin";
    }

    @RequestMapping(value = "/login", params = {"cancel"})
    public String loginCancel() {
        return "redirect:/";
    }
}
