package za.ac.sanbi.searchform;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SearchFormSession {

	private String studyName;
	private String design;
	private String hasSamples;
	SearchForm searchForm;
	
	public void saveForm(SearchForm searchForm) {
		this.studyName = searchForm.getStudyName();
		this.design = searchForm.getDesign();
		this.hasSamples = searchForm.getHasSamples();
	}
	
	public SearchForm toForm() {
		searchForm = new SearchForm();
		searchForm.setStudyName(studyName);
		searchForm.setDesign(design);
		searchForm.setHasSamples(hasSamples);
		return searchForm;
	}
	
	public void clearForm() {
		if (searchForm != null) {
			searchForm.setStudyName("");
			searchForm.setDesign("");
			searchForm.setHasSamples("");
		}
	}
}
