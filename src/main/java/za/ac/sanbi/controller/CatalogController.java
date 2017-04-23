package za.ac.sanbi.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import za.ac.sanbi.domain.NeoSample;
import za.ac.sanbi.domain.NeoStudy;
import za.ac.sanbi.domain.NeoUserDetails;
import za.ac.sanbi.repositories.DesignRepository;
import za.ac.sanbi.repositories.SampleRepository;
import za.ac.sanbi.repositories.StudyRepository;
import za.ac.sanbi.searchform.SearchForm;
import za.ac.sanbi.searchform.SearchFormSession;


/**
 * Created by hocine on 2017/04/13.
 */
@Controller
public class CatalogController {
	
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
	
	@ModelAttribute
	public SearchForm getSearchForm() {
		return searchFormSession.toForm();
	}
	
    @RequestMapping("/")
    public String goTohomePage(RedirectAttributes redirectAttributes) {
        return "redirect:/search";
    }
    
    @RequestMapping("/search")
    public String goToSearchPage(@RequestParam(name="advance", defaultValue="") String advance, Model model) {
    	if (advance.isEmpty()) {
    		searchFormSession.clearForm();
    		Collection<NeoStudy> studies = studyRepository.findAllStudies();
    		setModelAttributeHomePage(model, studies);
    	}else {
    		SearchForm searchForm = searchFormSession.toForm();
    		final Map<String, String> parameterValues = new LinkedHashMap<>();
        	parameterValues.put("studyName", searchForm.getStudyName());
        	parameterValues.put("design", searchForm.getDesign());
        	
        	String query = constructQuery(parameterValues);
        	final Map<String, String> queryParameters = removeNullParameters(parameterValues);
        	Collection<NeoStudy> studies = runNeoQuery(query, queryParameters);
        	setModelAttributeHomePage(model, studies);
    	}
    	return "homePage";
    }
    
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String advancedSearch(@Valid SearchForm searchForm, BindingResult bindingResult) {
    	if(bindingResult.hasErrors()){
			return "homePage";
		}
    	searchFormSession.saveForm(searchForm);
    	
    	return "redirect:/search?advance=advance";
    }
    
    @RequestMapping("/study")
    public String studyInfo(HttpServletRequest request, Model model) {
    	String studyAcronym = request.getParameter("s");
    	NeoStudy study = studyRepository.findByAcronym(studyAcronym);
    	int countStudies = studyRepository.countStudies();
    	model.addAttribute("countStudies", countStudies);
    	int countSamples = sampleRepository.countSamples();
    	model.addAttribute("countSamples", countSamples);
    	
    	for (NeoSample sample : study.getSamples()) {
    		if (Double.parseDouble(sample.getSampleVolume()) > 0) {
    			sample.setSampleAvailable("Yes");
    		}else {
    			sample.setSampleAvailable("No");
    		}
    	}
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
    
    @Autowired Session template;  
    
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
