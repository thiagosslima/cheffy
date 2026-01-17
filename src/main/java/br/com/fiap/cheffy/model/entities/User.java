package br.com.fiap.cheffy.model.entities;

import br.com.fiap.cheffy.exceptions.NotFoundException;
import br.com.fiap.cheffy.exceptions.OperationNotAllowedException;
import br.com.fiap.cheffy.model.enums.ExceptionsKeys;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static br.com.fiap.cheffy.model.enums.ExceptionsKeys.ADDRESS_NOT_FOUND_EXCEPTION;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(
            strategy = GenerationType.UUID
    )
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_profile",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id")
    )
    private Set<Profile> profiles = new HashSet<>();

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Address> addresses = new HashSet<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    public Address findAddressByIdOrFail(Long id) {
        return this.addresses.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        ADDRESS_NOT_FOUND_EXCEPTION,
                        Address.class.getSimpleName(),
                        id.toString()));
    }

    public void setMainAddress(Address address) {
        this.addresses.forEach(a -> a.setMain(false));
        address.setMain(true);
    }

    public void addUserMainAddress(Address address) {
        this.addresses.forEach(a -> a.setMain(false));
        address.setMain(true);
        this.addresses.add(address);
        address.setUser(this);
    }

    public void addAddress(Address address) {

        if (Boolean.TRUE.equals(address.getMain())) {
            this.addresses.forEach(a -> a.setMain(false));
        }

        this.addresses.add(address);
        address.setUser(this);
    }

    public void removeAddress(Address address) {

        if (!this.addresses.contains(address)) {
            return;
        }

        boolean wasMain = Boolean.TRUE.equals(address.getMain());

        this.addresses.remove(address);
        address.setUser(null);

        if (this.addresses.isEmpty()) {
            throw new OperationNotAllowedException(ExceptionsKeys.USER_MUST_HAVE_AT_LEAST_ONE_ADDRESS);
        }

        if (wasMain) {
            this.addresses.stream()
                    .findFirst()
                    .ifPresent(a -> a.setMain(true));
        }
    }
}


