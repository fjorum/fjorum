package org.fjorum.model.entity;

import org.fjorum.model.entity.permission.PermissionConverter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name="name", nullable = false)
    private String name;
    @Column(name="description")
    private String descriptionKey;
    @Column(name="predefined")
    boolean predefined;

    @Convert(converter = PermissionConverter.class)
    @ElementCollection
    @CollectionTable(name="role_permissions", joinColumns=@JoinColumn(name="role_id"))
    private Set<Permission> permissions = new HashSet<>();

    Role() {}

    public Role(String name, String descriptionKey, boolean predefined) {
        this.name = name;
        this.descriptionKey = descriptionKey;
        this.predefined = predefined;
    }

    public Long getId(){ return id; }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public void setDescriptionKey(String descriptionKey) {
        this.descriptionKey = descriptionKey;
    }

    public boolean isPredefined() {
        return predefined;
    }

    public void setPredefined(boolean predefined) {
        this.predefined = predefined;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
