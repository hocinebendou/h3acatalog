package za.ac.sanbi.login;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class RegisterForm {

	 @NotEmpty
	    private String username;

	    @Email
	    @NotEmpty
	    private String email;
	    
	    @NotEmpty
	    private String password;
	    
	    @NotEmpty
	    private String biobankname;

	    @NotEmpty
	    private String role;
	    
	    // getters
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
		public void setBiobankname(String biobankname) {
			this.biobankname = biobankname;
		}
		public void setRole(String role) {
			this.role = role;
		}
}
