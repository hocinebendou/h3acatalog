package za.ac.sanbi.domain;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@NodeEntity
public class NeoSpecType {

	@GraphId
	private Long id;
	
	private String name;
	
	public NeoSpecType() {}
	
	public String getName() {
	    return name;
	}
}
