package za.ac.sanbi.repositories;

import java.util.Collection;

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
    String query = "MATCH (u: NeoUserDetails {userName: {userName}}) RETURN u";
	
	@Query(query)
	NeoUserDetails findUser(@Param("userName") String userName);	
	NeoUserDetails findByUsername(String login);
	
	// find all users
	Collection<NeoUserDetails> findAll();
	
	// add user
	String addQuery = "MERGE (u: NeoUserDetails {username: {username}}) "
			+ "ON CREATE SET u.email = {email}, "
			+ "u.password = {password}, "
			+ "u.biobankname = {biobankname}, "
			+ "u.role = {role} "
			+ "ON MATCH SET u.email = {email}, "
			+ "u.password = {password}, "
			+ "u.biobankName = {biobankname}, "
			+ "u.role = {role} ";
	
	@Query(addQuery)
	void addEditUser(@Param("username") String username, 
					 @Param("email") String email, 
					 @Param("password") String password, 
					 @Param("biobankname") String biobankname, 
					 @Param("role") String role);
}
