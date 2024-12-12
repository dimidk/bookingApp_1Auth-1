package com.exam.bookingapp_1.model;

import com.exam.bookingapp_1.model.Brole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name="labuser")
public class Busers implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "username")
    private String username;


    @Column(columnDefinition="email")
    private String email;

//
//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(name = "users_roles",
//        joinColumns = @JoinColumn(name = "uid", referencedColumnName = "id"),
//        inverseJoinColumns = @JoinColumn(name = "rid", referencedColumnName = "id")
//)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name="fullname")
    private String fullname;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        //return this.email;
        return "";
    }
//    private Set<Brole> roles;

}
