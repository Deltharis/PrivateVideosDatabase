package stja.shiro_stuff;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import stja.data_access.UserDAO;
import stja.entities.user.User;

/**
 * Created by Delth on 26.10.2015.
 */
public class MyCustomRealm extends JdbcRealm {

    private UserDAO userDAO;

    public MyCustomRealm(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // identify account to log to
        UsernamePasswordToken userPassToken = (UsernamePasswordToken) token;
        final String username = userPassToken.getUsername();

        if (username == null) {
            System.out.println("Username is null.");
            return null;
        }

        // read password hash and salt from db
        final User user = userDAO.getUserByUsername(username);

        if (user == null) {
            System.out.println("No account found for user [" + username + "]");
            return null;
        }

        // return salted credentials
        SaltedAuthenticationInfo info = new MySaltedAuthenticationInfo(username, user.getPassword(), user.getSalt());

        return info;
    }
}
