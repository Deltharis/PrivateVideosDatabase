package stja.data_access;

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
}
