package za.ac.sanbi.repositories;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import za.ac.sanbi.domain.NeoSample;

public interface SampleRepository extends PagingAndSortingRepository<NeoSample, Long>{
	// query count studies
	String queryCount = "MATCH (s:NeoSample) RETURN count(s)";
	@Query(queryCount)
	int countSamples();
	
	// Not used anywhere (just an example for now)
	String queryCase = "MATCH (s:NeoStudy {acronym: {acronym}}) -[r:HAS_SAMPLE]-> (p:NeoSample {caseControl: {name}}) RETURN p";
	@Query(queryCase)
	Collection<NeoSample> samplesByDesign(@Param("acronym") String acronym, @Param("name") String name);
}
