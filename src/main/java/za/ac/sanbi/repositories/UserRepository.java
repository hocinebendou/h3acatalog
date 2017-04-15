package za.ac.sanbi.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;
import za.ac.sanbi.domain.NeoUserDetails;
import za.ac.sanbi.services.CustomUserDetailsService;

/**
 * Created by hocine on 2017/04/15.
 */
public interface UserRepository extends GraphRepository<NeoUserDetails>, CustomUserDetailsService {
    NeoUserDetails findByUsername(String login);
}
