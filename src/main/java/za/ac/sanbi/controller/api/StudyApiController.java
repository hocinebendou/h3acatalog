package za.ac.sanbi.controller.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import za.ac.sanbi.domain.NeoSample;
import za.ac.sanbi.repositories.SampleRepository;

@RestController
@RequestMapping("/api/study")
public class StudyApiController {

	@Autowired
	SampleRepository sampleRepository;
	
	@RequestMapping(value = "/{searchParam}", method = RequestMethod.GET)
	public Map<String, Integer> designTypeSummary(@PathVariable String searchParam) {
		String[] parts = searchParam.split("_");
		String acronym = parts[0];
		String design = parts[1];
		Collection<NeoSample> samples = sampleRepository.samplesByDesign(acronym, design);
		Map<String, Integer> summaryCountry = new HashMap<String, Integer>();
		for (NeoSample sample: samples) {
			if (summaryCountry.containsKey(sample.getCountry()))
				summaryCountry.replace(sample.getCountry(), summaryCountry.get(sample.getCountry())+1);
			else summaryCountry.put(sample.getCountry(), 1);
		}
		TreeMap<String, Integer> sortedSummaryCountry = new TreeMap<>(summaryCountry);
		return sortedSummaryCountry;
	}
}
