package stja.shiro_stuff;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.jndi.JndiObjectFactory;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import stja.data_access.UserDAO;
import stja.entities.user.User;

import javax.sql.DataSource;


/**
 * Created by Delth on 26.10.2015.
 */
public class MyCustomRealm extends JdbcRealm {

    private UserDAO userDAO;

    public MyCustomRealm(UserDAO userDAO) {
        this.userDAO = userDAO;

        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("Sha256Hash");
        matcher.setStoredCredentialsHexEncoded(false);
        matcher.setHashIterations(1024);
        setCredentialsMatcher(matcher);

        setAuthenticationQuery("SELECT password, salt FROM User WHERE username = ?");
        setPermissionsLookupEnabled(true);
        setUserRolesQuery("select r.rolename from role r inner join UserRole ur on r.id = ur.roleId inner join User u on ur.userId = u.id where u.username = ?");
        setPermissionsQuery("select p.permission from Permission p inner join RolePermission rp on p.id = rp.permissionId inner join Role r on rp.roleId = r.id where r.roleName = ?");

        JndiObjectFactory factory = new JndiObjectFactory();
        factory.setResourceName("java:jboss/datasources/MySQLDS");
        setDataSource((DataSource) factory);
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
