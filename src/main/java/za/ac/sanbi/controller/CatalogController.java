package za.ac.sanbi.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import za.ac.sanbi.domain.NeoStudy;
import za.ac.sanbi.repositories.StudiesRepository;

/**
 * Created by hocine on 2017/04/13.
 */
@Controller
public class CatalogController {
	
	@Autowired 
	StudiesRepository studiesRepository;
	
    @RequestMapping("/")
    public String goTohomePage(Model model) {
    	Collection<NeoStudy> studies = studiesRepository.findAllStudies();
    	List<NeoStudy> listStudies = new ArrayList<>(studies);
    	
    	model.addAttribute("studies", listStudies);
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
