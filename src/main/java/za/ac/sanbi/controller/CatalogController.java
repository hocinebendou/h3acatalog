package za.ac.sanbi.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.collections4.IteratorUtils;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import za.ac.sanbi.domain.NeoDesign;
import za.ac.sanbi.domain.NeoStudy;
import za.ac.sanbi.domain.NeoUserDetails;
import za.ac.sanbi.login.RegisterForm;
import za.ac.sanbi.repositories.DesignRepository;
import za.ac.sanbi.repositories.SampleRepository;
import za.ac.sanbi.repositories.StudyRepository;
import za.ac.sanbi.repositories.UserRepository;
import za.ac.sanbi.searchform.SearchForm;
import za.ac.sanbi.searchform.SearchFormSession;


/**
 * Created by hocine on 2017/04/13.
 */
@Controller
public class CatalogController {
	
	@Autowired 
	UserRepository userRepository;
	
	@Autowired 
	StudyRepository studyRepository;
	
	@Autowired
	DesignRepository designRepository;
	
	@Autowired 
	SampleRepository sampleRepository;
	
	private SearchFormSession searchFormSession;
	@Autowired
	public CatalogController(SearchFormSession searchFormSession) {
		this.searchFormSession = searchFormSession;
	}
	
	@Autowired Session template; 
	
	@ModelAttribute
	public SearchForm getSearchForm() {
		return searchFormSession.toForm();
	}
	
    @RequestMapping("/")
    public String goTohomePage(RedirectAttributes redirectAttributes) {
        return "redirect:/search";
    }
    
    private Collection<NeoStudy> showAllStudies () {
    	searchFormSession.clearForm();
		Collection<NeoStudy> studies = studyRepository.findAllStudies();
		
		return studies;
    }
    
    @RequestMapping("/search")
    public String goToSearchPage(@RequestParam(name="advance", defaultValue="") String advance, Model model) {
    	Collection<NeoStudy> studies = new ArrayList<>();
    	String bool = "";
    	if (advance.isEmpty()) {
    		studies = showAllStudies();
    	}else {
    		SearchForm searchForm = searchFormSession.toForm();
    		final Map<String, String> parameterValues = new LinkedHashMap<>();
        	parameterValues.put("studyName", searchForm.getStudyName());
        	parameterValues.put("design", searchForm.getDesign()); 
        	bool = searchForm.getHasSamples();
        	String query = constructQuery(parameterValues);
        	if (query.isEmpty()) {
        		studies = showAllStudies();
        	}else {
	        	final Map<String, String> queryParameters = removeNullParameters(parameterValues);
	        	studies = runNeoQuery(query, queryParameters);
        	}
    	}
    	studies = hasSamples(studies, bool);
    	setModelAttributeHomePage(model, studies);
    	
    	return "homePage";
    }
    
    private Collection<NeoStudy> hasSamples(Collection<NeoStudy> studies, String bool) {
    	Collection<NeoStudy> ret = new ArrayList<>();
    	for (NeoStudy s : studies) {
    		NeoStudy study = template.load(s.getClass(), s.getId());
    		if (study.getSamples().size() > 0){
    			study.setHasSamples("yes");
    			if (bool.equals("yes")) ret.add(study);
    		} else {
    			study.setHasSamples("No");
    			if (bool.equals("no")) ret.add(study);
    		}
    	}

    	if (! bool.isEmpty()) studies = ret;
    	
    	return studies;
    }
    
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String advancedSearch(@Valid SearchForm searchForm, BindingResult bindingResult) {
    	if(bindingResult.hasErrors()){
			return "homePage";
		}
    	searchFormSession.saveForm(searchForm);
    	
    	return "redirect:/search?advance=advance";
    }
    
    
    
    
    @RequestMapping("/login")
    public String goToLoginPage() {
        return "login/loginPage";
    }
    
    @RequestMapping("/register")
    public String goToRegisterPage(RegisterForm registerForm, Model model) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        NeoUserDetails user = (NeoUserDetails) auth.getPrincipal();
        Collection<NeoUserDetails> users = userRepository.findAll();
        
        model.addAttribute("user", user);
        model.addAttribute("users", users);
        
        return "login/registerPage";
    }
    
    @RequestMapping(value = "/register", params = {"adduser"})
    public String registerNewUser(@Valid RegisterForm registerForm, RedirectAttributes redirectAttributes) {
    	
    	userRepository.addEditUser(registerForm.getUsername(), 
    							   registerForm.getEmail(), 
    							   registerForm.getPassword(), 
    							   registerForm.getBiobankname(), 
    							   registerForm.getRole());
    	String rawPath = "./users/" + registerForm.getUsername() + "/raw";
    	String processedPath = "./users/" + registerForm.getUsername() + "/processed";
    	File rawDir = new File(rawPath);
    	File processedDir = new File(processedPath);
    	rawDir.mkdirs();
    	processedDir.mkdirs();
        redirectAttributes.addFlashAttribute("success", "User created successfully!");
        
        return "redirect:/register";
    }
    
    
    @RequestMapping("/admin")
    public String goToAdminPage(Model model) {
    	
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        NeoUserDetails user = (NeoUserDetails) auth.getPrincipal();
        Collection<NeoUserDetails> users = userRepository.findAll();
        
        model.addAttribute("user", user);
        model.addAttribute("users", users);
        return "admin/adminPage";
    }
    
    private void setModelAttributeHomePage(Model model, Collection<NeoStudy> studies) {
    	int countStudies = studyRepository.countStudies();
    	model.addAttribute("countStudies", countStudies);
    	int countSamples = sampleRepository.countSamples();
    	model.addAttribute("countSamples", countSamples);
    	model.addAttribute("studies", studies);
    	Collection<NeoDesign> designs = designRepository.findAll();
    	model.addAttribute("designs", designs);
    }
    
    private LinkedHashMap<String, String> removeNullParameters(Map<String, String> params) {
		LinkedHashMap<String, String> paramsQuery = new LinkedHashMap<>();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if(!entry.getValue().isEmpty() && entry.getValue() != null) {
				paramsQuery.put(entry.getKey(), entry.getValue());
			}
		}
		return paramsQuery;
	}
    
    private String constructQuery(Map<String, String> parameterValues) {
		String query = "";
    	String queryStudyName = "";
		String queryDesign = "";
		for (Map.Entry<String, String> entry : parameterValues.entrySet()) {
			if(entry.getValue() != "") {
				switch (entry.getKey()) {
					case "studyName":
						queryStudyName += "WITH REDUCE(res = [], w IN SPLIT({" + entry.getKey() + "}, \" \") | ";
						queryStudyName += "CASE WHEN w <> '' THEN res + (\"(?i).*\\\\b\" + w + \"\\\\b.*\") ELSE res END) AS res ";
						break;
					case "design":
						queryDesign += "MATCH (d: NeoDesign {name: '"+ entry.getValue() +"'})-[r:STUDY_DESIGN]->(s: NeoStudy) ";
						break;
					default:
						break;
				}
			}
		}
		
		if (!queryStudyName.isEmpty()) {
			if (!queryDesign.isEmpty()) {
				query = queryStudyName + queryDesign;
				query += "WITH s, r, d ";
			} else {
				query = queryStudyName;
				query += "MATCH (s: NeoStudy) "; 
			}
			query += "WHERE ALL (regexp IN res WHERE s.description =~ regexp) ";
			query += "RETURN s";
		} else if (!queryDesign.isEmpty()) {
			query = queryDesign;
			query += "RETURN s";
		}
		
		return query;
	}
    
    private Collection<NeoStudy> runNeoQuery(String query, Map<String, String> paramsQuery) {
    	Collection<NeoStudy> studies = new ArrayList<>();
    	
		Result result = template.query(query, paramsQuery);
		List<Map<String, Object>> mapStudies = IteratorUtils.toList(result.iterator());
		for (Map<String, Object> i : mapStudies) {
			for (Map.Entry<String, Object> entry : i.entrySet()) {
				studies.add((NeoStudy)entry.getValue());
			}
		}
		
		return studies;
	}
}
