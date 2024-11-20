package com.quartze.shortenerurl.services;

import com.quartze.shortenerurl.exceptions.TokenIsInvalidException;
import com.quartze.shortenerurl.exceptions.UserAlreadyExistsException;
import com.quartze.shortenerurl.exceptions.UserInvalidCredentialsException;
import com.quartze.shortenerurl.helpers.AuthUtils;
import com.quartze.shortenerurl.models.Auth;
import com.quartze.shortenerurl.models.User;
import com.quartze.shortenerurl.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class UserService {

    private final UserRepository userRep;

    @Autowired
    public UserService(UserRepository userRep) {
        this.userRep = userRep;
    }

    public User getUser(Long id) {
        return userRep.findById(id).orElse(null);
    }

    public User getUser(String email) {
        return userRep.findByEmail(email).orElse(null);
    }

    public User getUser(Long id, String secret) {
        return userRep.findByIdAndUserSecret(id, secret).orElse(null);
    }

    public User addUser(String email, String password) {
        if(userRep.findByEmail(email).isPresent()) throw new UserAlreadyExistsException();

        User user = new User();
        user.setEmail(email);
        user.setPassword(AuthUtils.hashUserPassword(password));
        user.setUserSecret(AuthUtils.generateUserSecret());
        user.setCreatedAt(new Date(System.currentTimeMillis()));

        return userRep.save(user);
    }

    public User save(User user) {
        return userRep.save(user);
    }

    public Auth login(String email, String password) {
        User user = getUser(email);
        if(user == null) throw new UserInvalidCredentialsException();

        boolean verify = AuthUtils.verifyPasswords(password, user.exposePassword());
        if(!verify) throw new UserInvalidCredentialsException();

        user.setUserSecret(AuthUtils.generateUserSecret());
        save(user);

        Auth auth = new Auth();
        auth.setToken(AuthUtils.generateUserToken(user.getId()));
        auth.setRefresh(AuthUtils.generateUserRefreshToken(user.getId(), user.exposeUserSecret()));

        return auth;
    }

    public Auth register(String email, String password) {
        User userDB = addUser(email, password);

        Auth auth = new Auth();
        auth.setToken(AuthUtils.generateUserToken(userDB.getId()));
        auth.setRefresh(AuthUtils.generateUserRefreshToken(userDB.getId(), userDB.exposeUserSecret()));

        return auth;
    }

    public Auth refreshToken(String token) {
        Long userId = AuthUtils.getIdFromToken(token);
        String secret = AuthUtils.getSecretFromToken(token);

        User user = getUser(userId, secret);
        if(user == null) throw new TokenIsInvalidException();

        Auth auth = new Auth();
        auth.setToken(AuthUtils.generateUserToken(user.getId()));

        return auth;
    }

}
