package stja.data_access.security;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import stja.data_access.AbstractDAO;
import stja.entities.user.Role;
import stja.entities.user.User;

import javax.ejb.Stateless;
import java.util.List;
import java.util.Set;

/**
 * Created by Delth on 26.10.2015.
 */
@Stateless
public class UserDAO extends AbstractDAO<User> {
    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    @Deprecated
    public void persist(User user) {
        throw new RuntimeException("NOPE, wrong method dude");
    }

    public User getUserByUsername(String username){
        List<User> list = em.createQuery("FROM User WHERE username = :username", User.class).setParameter("username", username).getResultList();
        return list.isEmpty() ? null : list.get(0);

    }

    public void registerUser(String username, String plainTextPassword, Set<Role> roles) {
        User user = new User(roles);
        user.setUsername(username);

        generatePassword(user, plainTextPassword);

        merge(user);
    }

    private void generatePassword(User user, String plainTextPassword) {
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        Object salt = rng.nextBytes();

        // Now hash the plain-text password with the random salt and multiple
        // iterations and then Base64-encode the value (requires less space than Hex):
        String hashedPasswordBase64 = new Sha256Hash(plainTextPassword, salt,1024).toBase64();

        user.setPassword(hashedPasswordBase64);
        user.setSalt(salt.toString());
    }

    public void changePassword(User user, String password) {
        generatePassword(user, password);
        merge(user);
    }
}
