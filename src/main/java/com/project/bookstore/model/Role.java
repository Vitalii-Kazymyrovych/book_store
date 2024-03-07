package com.project.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "role_name", columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    public Role(Long id) {
        this.id = id;
    }

    @RequiredArgsConstructor
    public enum RoleName {
        USER("user"),
        ADMIN("admin");

        private final String name;

        @Override
        public String toString() {
            return name;
        }
    }
}
