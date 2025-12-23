package com.houssam.SmartLogi.model;

import com.houssam.SmartLogi.enums.Provider;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;


@Entity
@Table(name= "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Livreur livreur;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ClientExpediteur clientExpediteur;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private Provider provider;

    @Size(max = 255)
    @Column(name = "provider_id")
    private String providerId;

    @NotNull
    @ColumnDefault("true")
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;
}
