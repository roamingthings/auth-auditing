package de.roamingthings.authaudit.authauditing.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/05/11
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "user_account")
public class UserAccount {
    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty
    @Size(max = 50)
    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @NotEmpty
    @Column(name = "password_hash", length = 64, nullable = false)
    private String passwordHash;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    // ~ defaults to @JoinTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "user_id"),
    //     inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JoinTable(name = "USER_ACCOUNT_ROLE", joinColumns = @JoinColumn(name = "user_account_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public UserAccount(String username, String passwordHash, boolean enabled, Set<Role> roles) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.enabled = enabled;
        this.roles = roles;
    }

}
