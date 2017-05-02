package za.ac.sanbi.repositories;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import za.ac.sanbi.domain.NeoSample;

public interface SampleRepository extends PagingAndSortingRepository<NeoSample, Long>{
	// count number of studies
	String queryCount = "MATCH (s:NeoSample) RETURN count(s)";
	@Query(queryCount)
	int countSamples();
	
	// return study samples using study acronym and design samples (case or control)
	String queryCase = "MATCH (s:NeoStudy {acronym: {acronym}}) -[r1:HAS_SAMPLE]-> (p:NeoSample) -[r2:HAS_CHARACTER]-> (c:NeoCharacter {name: {name}}) RETURN p";
	@Query(queryCase)
	Collection<NeoSample> samplesByDesign(@Param("acronym") String acronym, @Param("name") String name);
	
	
}
