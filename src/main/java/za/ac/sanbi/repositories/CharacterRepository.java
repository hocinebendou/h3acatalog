package za.ac.sanbi.repositories;

import java.util.Collection;

import org.springframework.data.repository.PagingAndSortingRepository;

import za.ac.sanbi.domain.NeoCharacter;

public interface CharacterRepository extends PagingAndSortingRepository<NeoCharacter, Long>{
	
	// Return country nodes
	Collection<NeoCharacter> findAll();
	
}
