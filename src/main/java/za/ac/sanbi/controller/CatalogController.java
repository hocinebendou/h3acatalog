package za.ac.sanbi.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import za.ac.sanbi.domain.NeoStudy;
import za.ac.sanbi.domain.NeoUserDetails;
import za.ac.sanbi.repositories.StudyRepository;


/**
 * Created by hocine on 2017/04/13.
 */
@Controller
public class CatalogController {
	
	@Autowired 
	StudyRepository studyRepository;
	
    @RequestMapping("/")
    public String goTohomePage(Model model) {
    	Collection<NeoStudy> studies = studyRepository.findAllStudies();
    	List<NeoStudy> listStudies = new ArrayList<>(studies);
    	
    	model.addAttribute("studies", listStudies);
        return "homePage";
    }

    @RequestMapping("/study")
    public String studyInfo(HttpServletRequest request, Model model) {
    	String studyAcronym = request.getParameter("s");
    	NeoStudy study = studyRepository.findByAcronym(studyAcronym);
    	model.addAttribute("study", study);
    	return "study/studyPage";
    }
    
    @RequestMapping("/login")
    public String goToLoginPage() {
        return "login/loginPage";
    }

    @RequestMapping("/admin")
    public String goToAdminPage(Model model) {
    	
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        NeoUserDetails user = (NeoUserDetails) auth.getPrincipal();
        model.addAttribute("user", user);
        
        return "admin/adminPage";
    }

}
