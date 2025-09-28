package org.digitalwallet.app.repository.model;

import jakarta.persistence.*;
import lombok.Data;
import org.digitalwallet.app.enumeration.Role;

@Data
@Entity
@Table(name = "USER_AUTH")
public class UserAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;
}
