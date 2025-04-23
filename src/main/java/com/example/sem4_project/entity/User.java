package com.example.sem4_project.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @Column(name = "user_id", columnDefinition = "CHAR(36)")
    UUID userId;

    @ManyToOne
    @JoinColumn(name = "employee_id", unique = true)
    Employee employee;

    @Column(name = "username", nullable = false, unique = true)
    String username;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    Status status;

    public enum Status {
        Active,
        Inactive
    }

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    String createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    String updatedAt;
}
