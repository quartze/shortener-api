package com.quartze.shortenerurl.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    private long id;

    @Column(name = "email", unique = true, nullable = false)
    @Getter
    @Setter
    private String email;

    @Column(name = "password", nullable = false)
    @Setter
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    @Getter
    @Setter
    @Column(name = "shorts_urls")
    private List<ShortUrls> shortsUrls;

    @Setter
    @Column(name = "user_secret", unique = true, nullable = false)
    private String userSecret;

    @Column(name = "created_at")
    @Getter
    @Setter
    private Date createdAt;


    public String exposeUserSecret() {
        return userSecret;
    }

    public String exposePassword() {
        return password;
    }
}
