package za.ac.sanbi.searchform;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SearchFormSession {

	private String studyName;
	private String design;
	SearchForm searchForm;
	
	public void saveForm(SearchForm searchForm) {
		this.studyName = searchForm.getStudyName();
		this.design = searchForm.getDesign();
	}
	
	public SearchForm toForm() {
		searchForm = new SearchForm();
		searchForm.setStudyName(studyName);
		searchForm.setDesign(design);
		
		return searchForm;
	}
	
	public void clearForm() {
		if (searchForm != null) {
			searchForm.setStudyName("");
			searchForm.setDesign("");
		}
	}
}
