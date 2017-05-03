package za.ac.sanbi.repositories;

import java.util.Collection;

import org.springframework.data.repository.PagingAndSortingRepository;

import za.ac.sanbi.domain.NeoGender;

public interface GenderRepository extends PagingAndSortingRepository<NeoGender, Long> {
	
	// Return country nodes
	Collection<NeoGender> findAll();
}
