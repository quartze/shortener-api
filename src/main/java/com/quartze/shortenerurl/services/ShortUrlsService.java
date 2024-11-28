package com.quartze.shortenerurl.services;

import com.quartze.shortenerurl.exceptions.ShortsUrlNonExistException;
import com.quartze.shortenerurl.models.ShortUrls;
import com.quartze.shortenerurl.models.User;
import com.quartze.shortenerurl.repositories.ShortUrlsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Random;

@Service
public class ShortUrlsService {

    private final ShortUrlsRepository shortsUrlRep;
    private final UserService userService;

    @Autowired
    public ShortUrlsService(
            ShortUrlsRepository shortsUrlRep,
            UserService userService
    ) {
        this.shortsUrlRep = shortsUrlRep;
        this.userService = userService;
    }

    public ShortUrls createNewShortsUrl(String originalUrl, Boolean isNsfw) {
        ShortUrls urls = createNewShortsUrl(originalUrl, isNsfw, null);
        return urls;
    }

    public ShortUrls createNewShortsUrl(String originalUrl, Boolean isNsfw, Long userId) {
        Random random = new Random();

        String shortUrl = random.ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(8)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        ShortUrls shorts = new ShortUrls();
        shorts.setOriginalUrl(originalUrl);
        shorts.setCreatedAt(new Date(System.currentTimeMillis()));
        shorts.setIsNsfw(isNsfw);
        shorts.setOpenedBy(0);
        shorts.setShortUrl(shortUrl);

        if(userId != null) {
            User user = userService.getUser(userId);
            shorts.setUser(user);
        }

        shortsUrlRep.save(shorts);

        return shorts;
    }

    public ShortUrls getShortUrl(String id) {

        ShortUrls shorts = shortsUrlRep.findByShortUrl(id);
        if(shorts == null) throw new ShortsUrlNonExistException();

        shorts.setOpenedBy(shorts.getOpenedBy() + 1);
        shortsUrlRep.save(shorts);

        return shorts;
    }

    public void deleteShortUrl(String id) {
        ShortUrls shorts = shortsUrlRep.findByShortUrl(id);
        if(shorts == null) throw new ShortsUrlNonExistException();

        shortsUrlRep.delete(shorts);
    }

    public Page<ShortUrls> getShortUrlsByUser(Long userId, PageRequest page) {
        User user = userService.getUser(userId);
        return shortsUrlRep.findByUser(user, page);
    }

}
