package stja.shiro_stuff;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import stja.data_access.security.UserDAO;
import stja.entities.user.User;

import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * Created by Delth on 26.10.2015.
 */
public class MyCustomRealm extends JdbcRealm {

    private UserDAO userDAO;

    public MyCustomRealm() {
        try {
            userDAO = (UserDAO) new InitialContext().lookup("java:module/UserDAO");
        } catch (NamingException e) {
            e.printStackTrace();
        }
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("SHA-256");
        matcher.setStoredCredentialsHexEncoded(false);
        matcher.setHashIterations(1024);
        setCredentialsMatcher(matcher);

        setAuthenticationQuery("SELECT password, salt FROM user WHERE username = ?");
        setPermissionsLookupEnabled(true);
        setUserRolesQuery("select r.role_name from role r inner join user_role ur on r.id = ur.role_id inner join user u on ur.user_id = u.id where u.username = ?");
        setPermissionsQuery("select p.permission from permission p inner join role_permission rp on p.id = rp.permission_id inner join role r on rp.role_id = r.id where r.role_name = ?");
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
