package za.ac.sanbi.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hocine on 2017/04/17.
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@NodeEntity
public class NeoStudy {
	
	@Autowired Session session;
	
    @GraphId
    private Long id;

    private String acronym;
    private String title;
    private String description;
    private String hasSamples;

    @Relationship(type = "STUDY_DESIGN", direction=Relationship.INCOMING)
    private List<NeoDesign> designs = new ArrayList<>();

    @Relationship(type = "HAS_SAMPLE", direction=Relationship.OUTGOING)
    private List<NeoSample> samples = new ArrayList<>();
    
    public NeoStudy() {}

    public Long getId() {
    	return id;
    }
    
    public String getAcronym() {
        return acronym;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<NeoDesign> getDesigns() {
        return designs;
    }
    
    public List<NeoSample> getSamples() {
        return samples;
    }
    
    public String getHasSamples() {
    	return hasSamples;
    }
    
    public void setHasSamples(String bool) {
    	this.hasSamples = bool;
    }
}
