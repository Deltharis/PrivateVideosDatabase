package stja.entities.user;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by Delth on 26.10.2015.
 */
@Entity
@Table(name = "permission")
public class Permission implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "permission", length = 30)
    private String permission;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "permissions")
    private Set<Role> roles;

    public Permission() {
    }

    public Permission(String permission) {
        this.permission = permission;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", permission='" + permission + '\'' +
                '}';
    }
}