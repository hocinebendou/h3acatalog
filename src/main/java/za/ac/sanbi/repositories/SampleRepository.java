package za.ac.sanbi.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import za.ac.sanbi.domain.NeoSample;

public interface SampleRepository extends PagingAndSortingRepository<NeoSample, Long>{
	// query count studies
	String queryCount = "MATCH (s:NeoSample) RETURN count(s)";
	@Query(queryCount)
	int countSamples();
}
