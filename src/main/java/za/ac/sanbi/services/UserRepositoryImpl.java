package za.ac.sanbi.services;

import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import za.ac.sanbi.domain.NeoUserDetails;
import za.ac.sanbi.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hocine on 2017/04/15.
 */
public class UserRepositoryImpl implements CustomUserDetailsService {

    @Autowired private UserRepository userRepository;

    @Override
    public NeoUserDetails loadUserByUsername(String login) throws UsernameNotFoundException, DataAccessException {
        final NeoUserDetails user = findByLogin(login);

        if (user == null) throw new UsernameNotFoundException("Username not found: " + login);

        /*List<String> authorities = new ArrayList<>();
        authorities.add("USER");
        authorities.add("ADMIN");
        NeoUserDetails user1 = new NeoUserDetails("hocine", "hocine",
                authorities.toArray(new String[authorities.size()]));*/

        String[] authorities = {user.getRole()};
        NeoUserDetails userNeo = new NeoUserDetails(user.getUsername(), user.getPassword(), authorities);
        userNeo.setRole(user.getRole());

        return userNeo;
    }

    private NeoUserDetails findByLogin(String login) {
        return userRepository.findByUsername(login);
    }
}
