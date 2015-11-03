package stja.entities.user;

import com.google.gwt.thirdparty.guava.common.collect.Sets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by Delth on 26.10.2015.
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "username", length = 30, unique = true)
    private String username;

    @Column(name = "password", length = 128)
    private String password;

    @Column(name = "salt", length = 128)
    private String salt;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = {
            @JoinColumn(name = "user_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "role_id",
                    nullable = false, updatable = false)})
    private Set<Role> roles;

    public User() {
    }

    public User(Set<Role> roles) {
        this.roles = roles;
        for (Role r : roles) {
            if (r.getUsers() == null) {
                r.setUsers(Sets.newHashSet(this));
            } else {
                r.getUsers().add(this);
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", roles=" + roles +
                '}';
    }
}