package stja.shiro_stuff;

import stja.data_access.UserDAO;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Created by Delth on 26.10.2015.
 */
@Singleton
@Startup
public class ShiroStartup {
    @Inject
    private UserDAO userAccess;

    @PostConstruct
    public void setup () {
        //constructor injection
        MyCustomRealm myAppRealm = new MyCustomRealm(userAccess);

        //bind it so shiro can find it!
        try {
            new InitialContext().bind("realms/MyAppRealm", myAppRealm);
        } catch (NamingException e) {
            System.out.println("NOOOOOOOO");
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy () throws NamingException {
        new InitialContext().unbind("realms/MyAppRealm");
    }
}