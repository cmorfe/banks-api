package com.cmorfe.banks.api.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "branches")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 4)
    private String code;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false, length = 15)
    private String phone;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", bankId=" + (bank != null ? bank.getId() : null) +
                '}';
    }
}
