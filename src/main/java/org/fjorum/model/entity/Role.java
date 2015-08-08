package org.fjorum.model.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description")
    private String descriptionKey;
    @Column(name = "predefined")
    boolean predefined;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "included_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "included_role_id"))
    private Set<Role> includesRoles = new HashSet<>();

    Role() {
    }

    public Role(String name, String descriptionKey, boolean predefined) {
        this.name = name;
        this.descriptionKey = descriptionKey;
        this.predefined = predefined;
    }

    public Long getId() {
        return id;
    }

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

    public Set<Role> getIncludedRoles() {
        return includesRoles;
    }

    public void setIncludedRoles(Set<Role> includedRoles) {
        this.includesRoles = includedRoles;
    }

    public Set<Role> getAllRoles() {
        return Stream.concat(Stream.of(this),
                includesRoles.stream().flatMap(role -> role.getAllRoles().stream()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Role)) return false;

        Role role = (Role) o;

        return id != null && id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
