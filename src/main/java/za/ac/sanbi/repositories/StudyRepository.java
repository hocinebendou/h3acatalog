package za.ac.sanbi.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.neo4j.annotation.Query;
import za.ac.sanbi.domain.NeoStudy;

import java.util.Collection;

public interface StudyRepository extends PagingAndSortingRepository<NeoStudy, Long> {
	// query all studies
	String query = "MATCH (s:NeoStudy)<-[r:STUDY_DESIGN]-(d:NeoDesign) RETURN s, d, r";
	@Query(query)
	Collection<NeoStudy> findAllStudies();
	
	// find study by acronym
	NeoStudy findByAcronym(String title);
	
	// query count studies
	String queryCount = "MATCH (s:NeoStudy) RETURN count(s)";
	@Query(queryCount)
	int countStudies();
	
}
