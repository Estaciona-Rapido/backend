package org.estaciona.rapido.persistence;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "estaciona_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    public int id;

    @Column(name = "name", nullable = false, length = 50)
    public String name;

    // TODO: think if String and Text and the best ways to store and enforce hashes.
    @Column(name = "password_hash", nullable = false)
    public String passwordHash;

    @ManyToOne
    @JoinTable(
        name = "estaciona_user_role",
        joinColumns = 
            @JoinColumn(name = "id_user", nullable = false, referencedColumnName = "id_user"),
        inverseJoinColumns = 
            @JoinColumn(name = "id_role", nullable = false, referencedColumnName = "id_role")
        )
    public List<RoleEntity> roles;
}
