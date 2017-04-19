package za.ac.sanbi.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import za.ac.sanbi.domain.NeoUserDetails;
import za.ac.sanbi.services.CustomUserDetailsService;

/**
 * Created by hocine on 2017/04/15.
 */
public interface UserRepository extends GraphRepository<NeoUserDetails>, CustomUserDetailsService {
    
	// TODO: BOTH METHODS RETURNS THE SAME RESULT! KEEP THEM FOR NOW FOR TESTING
    String query = "MATCH (u: NeoUserDetails {username: {username}}) RETURN u";
	
	@Query(query)
	NeoUserDetails findUser(@Param("username") String username);
	
	NeoUserDetails findByUsername(String login);
}
