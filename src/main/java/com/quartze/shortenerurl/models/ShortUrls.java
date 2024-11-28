package com.quartze.shortenerurl.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@Table(name = "short_urls")
@NoArgsConstructor
@Getter
@Setter
public class ShortUrls {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "short_url")
    private String shortUrl;

    @Column(name = "original_url")
    private String originalUrl;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "opened_by")
    private long openedBy;

    @Column(name = "is_nsfw")
    private Boolean isNsfw;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
}
