package za.ac.sanbi.searchform;

import java.util.Collection;

import za.ac.sanbi.domain.NeoCharacter;
import za.ac.sanbi.domain.NeoCountry;
import za.ac.sanbi.domain.NeoGender;
import za.ac.sanbi.domain.NeoSpecType;

public class SearchSampleForm {

	private String country;
	private String gender;
	private String specType;
	private String character;
	private Collection<NeoCountry> countries;
	private Collection<NeoGender> genders;
	private Collection<NeoSpecType> specTypes;
	private Collection<NeoCharacter> characters;
	
	public String getCountry() {
		return country;
	}
	public String getGender() {
		return gender;
	}
	public String getSpecType() {
		return specType;
	}
	public String getCharacter() {
		return character;
	}
	
	public Collection<NeoCountry> getCountries() {
		return countries;
	}
	public Collection<NeoGender> getGenders() {
		return genders;
	}
	public Collection<NeoSpecType> getSpecTypes() {
		return specTypes;
	}
	public Collection<NeoCharacter> getCharacters() {
		return characters;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public void setSpecType(String specType) {
		this.specType = specType;
	}
	public void setCharacter(String character) {
		this.character = character;
	}
	
	public void setCountries(Collection<NeoCountry> countries) {
		this.countries = countries;
	}
	public void setGenders(Collection<NeoGender> genders) {
		this.genders = genders;
	}
	public void setSpecTypes(Collection<NeoSpecType> specTypes) {
		this.specTypes = specTypes;
	}
	public void setCharacters(Collection<NeoCharacter> characters) {
		this.characters = characters;
	}
}
