package za.ac.sanbi.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import za.ac.sanbi.domain.NeoCharacter;
import za.ac.sanbi.domain.NeoCountry;
import za.ac.sanbi.domain.NeoGender;
import za.ac.sanbi.domain.NeoSample;
import za.ac.sanbi.domain.NeoSpecType;
import za.ac.sanbi.repositories.CharacterRepository;
import za.ac.sanbi.repositories.CountryRepository;
import za.ac.sanbi.repositories.GenderRepository;
import za.ac.sanbi.repositories.SampleRepository;
import za.ac.sanbi.repositories.SpecTypeRepository;
import za.ac.sanbi.repositories.StudyRepository;
import za.ac.sanbi.repositories.UserRepository;
import za.ac.sanbi.searchform.SearchSampleForm;
import za.ac.sanbi.searchform.SearchSampleFormSession;

@Controller
public class SampleController {
	
	@Autowired 
	UserRepository userRepository;
	
	@Autowired 
	StudyRepository studyRepository;
	
	@Autowired 
	SampleRepository sampleRepository;
	
	@Autowired
	CountryRepository countryRepository;
	
	@Autowired
	GenderRepository genderRepository;
	
	@Autowired
	SpecTypeRepository specTypeRepository;
	
	@Autowired
	CharacterRepository characterRepository;
	
	@Autowired
	Session session;
	
	private SearchSampleFormSession formSession;
	@Autowired
	public SampleController(SearchSampleFormSession formSession) {
		this.formSession = formSession;
	}
	
	private SearchSampleForm form;
	@ModelAttribute
	public SearchSampleForm getSearchSampleForm() {
		form = formSession.toForm();
		return form;
	}
	
	@RequestMapping("/search/samples")
    public String searchSamples(@RequestParam(name="advance", defaultValue="") String advance, Model model) {
		int total = 0, totalCase = 0, totalCtl = 0, totalMale = 0, totalFemale = 0;
		String query = "";
		if (!advance.isEmpty()) {
			SearchSampleForm form = formSession.toForm();
    		final Map<String, String> parameterValues = new LinkedHashMap<>();
        	parameterValues.put("country", form.getCountry());
        	parameterValues.put("gender", form.getGender());
        	query = constructQuery(parameterValues);
        	final Map<String, String> queryParameters = removeNullParameters(parameterValues);
        	Collection<NeoSample> samples = runNeoQuery(query, queryParameters);
        	for (NeoSample s : samples) {
        		NeoSample sample = session.load(s.getClass(), s.getId());
        		total = samples.size();
        		System.out.println("----------------------------");
            	System.out.println("----------------------------");
            	System.out.println(sample.getGender());
            	System.out.println("----------------------------");
        		if (sample.getGender().getName().equals("Male")) totalMale ++;
        		else totalFemale ++;
        		if (sample.getCharacter().getName().equals("Case")) totalCase ++;
        		else totalCtl ++;
        	}
		}
		if (query.isEmpty()) {
			Collection<NeoSample> samplesCase = sampleRepository.samplesByCharacter("Case");
			Collection<NeoSample> samplesCtl = sampleRepository.samplesByCharacter("Control");
			Collection<NeoSample> samplesMale = sampleRepository.samplesByGender("Male");
			Collection<NeoSample> samplesFemale = sampleRepository.samplesByGender("Female");
			totalCase = samplesCase.size();
			totalCtl = samplesCtl.size();
			total = totalCase + totalCtl;
			totalMale = samplesMale.size();
			totalFemale = samplesFemale.size();
		}
		model.addAttribute("Total", total);
		model.addAttribute("Case", totalCase);
		model.addAttribute("Control", totalCtl);
		model.addAttribute("Male", totalMale);
		model.addAttribute("Female", totalFemale);
		int countStudies = studyRepository.countStudies();
    	model.addAttribute("countStudies", countStudies);
    	int countSamples = sampleRepository.countSamples();
    	model.addAttribute("countSamples", countSamples);
    	
    	createFieldSelections();
    	
		return "sample/samplePage";
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
		String queryCountry = "";
		String queryGender = "";
		for (Map.Entry<String, String> entry : parameterValues.entrySet()) {
			if(entry.getValue() != "") {
				switch (entry.getKey()) {
					case "country":
						queryCountry += "MATCH (c: NeoCountry {name: {country}}) <-[r:HAS_COUNTRY]- (s:NeoSample) ";
						break;
					case "gender":
						if (queryCountry.isEmpty())
							queryGender += "MATCH (g: NeoGender {name: {gender}}) <-[rg:HAS_GENDER]- (s: NeoSample) ";
						else
							queryGender += "MATCH (g: NeoGender {name: {gender}}) <-[rg:HAS_GENDER]- (s) ";
						break;
				}
			}
		}
		query = queryCountry + queryGender + "RETURN s";
		return query;
	}
	
	private Collection<NeoSample> runNeoQuery(String query, Map<String, String> paramsQuery) {
    	Collection<NeoSample> samples = new ArrayList<>();
		Result result = session.query(query, paramsQuery);
		List<Map<String, Object>> mapSamples = IteratorUtils.toList(result.iterator());
		for (Map<String, Object> i : mapSamples) {
			for (Map.Entry<String, Object> entry : i.entrySet()) {
				samples.add((NeoSample)entry.getValue());
			}
		}
		
		return samples;
	}
	
	@RequestMapping(value="/search/samples", method=RequestMethod.POST)
	public String advancedSearch(@Valid SearchSampleForm form, BindingResult bindingResult) {
		if(bindingResult.hasErrors()){
			return "homePage";
		}
		formSession.saveForm(form);
		
		return "redirect:/search/samples?advance=advance";
	}
	
	private void createFieldSelections() {
		Collection<NeoCountry> countries = countryRepository.findAll();
		Collection<NeoGender> genders = genderRepository.findAll();
		Collection<NeoSpecType> specTypes = specTypeRepository.findAll();
		Collection<NeoCharacter> characters = characterRepository.findAll();
		form.setCountries(countries);
		form.setGenders(genders);
		form.setSpecTypes(specTypes);
		form.setCharacters(characters);
	}
}
