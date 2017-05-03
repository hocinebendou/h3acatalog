package za.ac.sanbi.repositories;

import java.util.Collection;

import org.springframework.data.repository.PagingAndSortingRepository;

import za.ac.sanbi.domain.NeoSpecType;

public interface SpecTypeRepository extends PagingAndSortingRepository<NeoSpecType, Long>{
	
	// Return country nodes
	Collection<NeoSpecType> findAll();
}
