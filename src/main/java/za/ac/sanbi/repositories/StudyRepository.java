package za.ac.sanbi.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.neo4j.annotation.Query;
import za.ac.sanbi.domain.NeoStudy;

import java.util.Collection;

public interface StudyRepository extends PagingAndSortingRepository<NeoStudy, Long> {
	String query = "MATCH (s:NeoStudy) RETURN s";
	
	@Query(query)
	Collection<NeoStudy> findAllStudies();
	
	NeoStudy findByAcronym(String title);
}
