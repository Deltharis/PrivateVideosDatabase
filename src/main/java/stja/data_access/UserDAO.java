package stja.data_access;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import stja.entities.user.User;

import java.util.List;

/**
 * Created by Delth on 26.10.2015.
 */
public class UserDAO extends AbstractDAO<User> {
    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    public User getUserByUsername(String username){
        List<User> list = em.createQuery("FROM User WHERE username = :username", User.class).setParameter("username", username).getResultList();
        return list.isEmpty() ? null : list.get(0);

    }

    public void registerUser(String username, String plainTextPassword) {
        User user = new User();
        user.setUsername(username);

        generatePassword(user, plainTextPassword);

        persist(user);
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
}
