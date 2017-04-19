package za.ac.sanbi.login;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by hocine on 2017/04/14.
 */
public class LoginForm {

    @NotEmpty
    private String userName;

    @NotEmpty
    private String password;

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
