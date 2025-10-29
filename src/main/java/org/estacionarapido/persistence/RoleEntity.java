package org.estacionarapido.persistence;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "estaciona_role")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    public int id;

    @Column(name = "role_name", nullable = false, unique = true, length = 50)
    public String name;

    @ManyToMany(mappedBy = "roles")
    public List<UserEntity> users;
}
