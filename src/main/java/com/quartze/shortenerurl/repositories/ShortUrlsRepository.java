package com.quartze.shortenerurl.repositories;

import com.quartze.shortenerurl.models.ShortUrls;
import com.quartze.shortenerurl.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlsRepository extends JpaRepository<ShortUrls, Long> {

    public ShortUrls findByShortUrl(String shortUrl);

    public Page<ShortUrls> findByUser(User user, Pageable pageable);
}
