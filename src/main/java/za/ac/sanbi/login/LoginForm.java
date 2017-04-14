package za.ac.sanbi.login;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by hocine on 2017/04/14.
 */
public class LoginForm {

    @NotEmpty
    private String username;

    private String password;

    public String getUsername() {
        return username;
    }
    public void setUsername(String email) {
        this.username = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
