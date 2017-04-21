package za.ac.sanbi.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import za.ac.sanbi.domain.NeoSample;

public interface SampleRepository extends PagingAndSortingRepository<NeoSample, Long>{
	
}
