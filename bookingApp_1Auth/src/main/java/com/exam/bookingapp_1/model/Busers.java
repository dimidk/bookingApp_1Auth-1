package com.exam.bookingapp_1.model;

import com.exam.bookingapp_1.model.Brole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
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
public  class Busers implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;


    @Column(name = "email")
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
        return "";
    }


//    private Set<Brole> roles;

}
