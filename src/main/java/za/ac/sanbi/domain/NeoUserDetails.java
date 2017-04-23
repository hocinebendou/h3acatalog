package za.ac.sanbi.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Created by hocine on 2017/04/15.
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@NodeEntity
public class NeoUserDetails implements UserDetails{

    @GraphId
    private Long id;

    private String username;
    private String password;
    private String email;
    private String biobankname;
    private String role;

    private List<GrantedAuthority> grantedAuthorities;

    public NeoUserDetails() {}

    public NeoUserDetails(String username, String password, String[] authorities) {
        this.username = username;
        this.password = password;
        this.grantedAuthorities = AuthorityUtils.createAuthorityList(authorities);
    }

    // getters
    @Override
    public String getUsername() {
        return username;
    }
    public String getEmail() {
		return email;
	}
	public String getPassword() {
        return password;
    }
	public String getBiobankname() {
		return biobankname;
	}
    public String getRole() {
        return role;
    }
    
    // setters
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
		this.email = email;
	}
    public void setPassword(String password) {
        this.password = password;
    }
	public void setBiobankName(String biobankname) {
		this.biobankname = biobankname;
	}
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
