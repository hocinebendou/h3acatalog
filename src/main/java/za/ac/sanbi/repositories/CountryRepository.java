package za.ac.sanbi.repositories;

import java.util.Collection;

import org.springframework.data.repository.PagingAndSortingRepository;

import za.ac.sanbi.domain.NeoCountry;

public interface CountryRepository extends PagingAndSortingRepository<NeoCountry, Long>{

	// Return country nodes
	Collection<NeoCountry> findAll();
}
