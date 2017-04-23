package za.ac.sanbi.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@NodeEntity
public class NeoSample {
	
	@GraphId
    private Long id;
	
	private String uniqueSpecId;
	private String country;
	private String participantId;
	private String gender;
	private String study;
	private String specimenType;
	private String collectionDate;
	private String ageAtCollection;
	private String sampleVolume;
	private String dnaConcentration;
	private String dnaPurity_260_280;
	private String extractionMethod;
	private String sampleAvailable;
	
	public NeoSample() {}

	// getters
	
	public String getUniqueSpecId() {
		return uniqueSpecId;
	}
	
	public String getCountry() {
		return country;
	}

	public String getParticipantId() {
		return participantId;
	}

	public String getGender() {
		return gender;
	}

	public String getStudy() {
		return study;
	}

	public String getSpecimenType() {
		return specimenType;
	}

	public String getCollectionDate() {
		return collectionDate;
	}

	public String getAgeAtCollection() {
		return ageAtCollection;
	}

	public String getSampleVolume() {
		return sampleVolume;
	}
	
	public String getDnaConcentration() {
		return dnaConcentration;
	}

	public String getDnaPurity_260_280() {
		return dnaPurity_260_280;
	}

	public String getExtractionMethod() {
		return extractionMethod;
	}
	
	public String getSampleAvailable() {
		return sampleAvailable;
	}
	
	public void setSampleAvailable(String available) {
		this.sampleAvailable = available;
	}
}
