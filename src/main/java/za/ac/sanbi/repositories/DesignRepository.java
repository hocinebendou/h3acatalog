package za.ac.sanbi.repositories;

import java.util.Collection;

import org.springframework.data.repository.PagingAndSortingRepository;

import za.ac.sanbi.domain.NeoDesign;

public interface DesignRepository extends PagingAndSortingRepository<NeoDesign, Long> {
	
	Collection<NeoDesign> findAll();
}
