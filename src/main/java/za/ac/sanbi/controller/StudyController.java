package za.ac.sanbi.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import za.ac.sanbi.domain.NeoSample;
import za.ac.sanbi.domain.NeoStudy;
import za.ac.sanbi.repositories.SampleRepository;
import za.ac.sanbi.repositories.StudyRepository;
import za.ac.sanbi.utils.SummaryCaseCtl;

@Controller
public class StudyController {
	
	@Autowired 
	SampleRepository sampleRepository;
	
	@Autowired 
	StudyRepository studyRepository;
	
	@RequestMapping("/study")
    public String studyInfo(HttpServletRequest request, Model model) {
    	String studyAcronym = request.getParameter("s");
    	NeoStudy study = studyRepository.findByAcronym(studyAcronym);
    	int countStudies = studyRepository.countStudies();
    	model.addAttribute("countStudies", countStudies);
    	int countSamples = sampleRepository.countSamples();
    	model.addAttribute("countSamples", countSamples);
    	// summary study samples
    	SummaryCaseCtl summary = new SummaryCaseCtl();
    	
    	for (NeoSample sample : study.getSamples()) {
    		switch(sample.getCaseControl()) {
    			case "Case":
    				updateSampleSummary(sample, summary, "Case");
    				break;
    			case "Control":
    				updateSampleSummary(sample, summary, "Control");
    				break;
    			default:
    				break;
    		}
    	}
    	model.addAttribute("summary", summary);
    	model.addAttribute("study", study);
    	return "study/studyPage";
    }
    
    private void updateSampleSummary(NeoSample sample, SummaryCaseCtl summary, String caseControl) {
    	if (sample.getSampleVolume() != null && Double.parseDouble(sample.getSampleVolume()) > 0) {
    		if (caseControl.equals("Case")) {
    			summary.setCasesWithVolume(summary.getCasesWithVolume() + 1);
    			summary.setCasesVolume(summary.getCasesVolume() + Double.parseDouble(sample.getSampleVolume()));
    		}else {
    			summary.setCtlsWithVolume(summary.getCtlsWithVolume() + 1);
    			summary.setCtlsVolume(summary.getCtlsVolume() + Double.parseDouble(sample.getSampleVolume()));
    		}
			sample.setSampleAvailable("Yes");
    	}else {
			sample.setSampleAvailable("No");
		}
    	if (caseControl.equals("Case")) {
    		summary.setCountCases(summary.getCountCases() + 1);
    		if (sample.getGender().equals("Male")) summary.setCountCasesMale(summary.getCountCasesMale() + 1);
    		else if (sample.getGender().equals("Female")) summary.setCountCasesFemale(summary.getCountCasesFemale() + 1);
    	}else {
    		summary.setCountCtls(summary.getCountCtls() + 1);
    		if (sample.getGender().equals("Male")) summary.setCountCtlsMale(summary.getCountCtlsMale() + 1);
    		else if (sample.getGender().equals("Female")) summary.setCountCtlsFemale(summary.getCountCtlsFemale() + 1);
    	}
    }
    
}
