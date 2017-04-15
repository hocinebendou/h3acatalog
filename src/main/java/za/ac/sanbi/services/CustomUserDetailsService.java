package za.ac.sanbi.services;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import za.ac.sanbi.domain.NeoUserDetails;
/**
 * Created by hocine on 2017/04/15.
 */
public interface CustomUserDetailsService extends UserDetailsService {

    @Override
    NeoUserDetails loadUserByUsername(String login) throws UsernameNotFoundException, DataAccessException;

}
