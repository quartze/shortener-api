package com.quartze.shortenerurl.helpers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.quartze.shortenerurl.exceptions.TokenIsInvalidException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;

public class AuthUtils {

    private static final Algorithm alg = Algorithm.HMAC256("rU$2X0~<6&v`uOB^");
    private static final String issuer = "SHRTL";

    /**
     * Generates a JWT token for the given user ID.
     *
     * @param userId The ID of the user.
     * @return A signed JWT token.
     */
    public static String generateUserToken(Long userId) {
        return JWT.create()
                .withIssuer(issuer)
                .withClaim("user_id", userId)
                .withSubject("Authentication")
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(Instant.now().plus(3, ChronoUnit.DAYS)))
                .sign(alg);
    }

    /**
     * Generates a JWT refresh token for the given user ID.
     *
     * @param userId The ID of the user.
     * @return A signed JWT token.
     */
    public static String generateUserRefreshToken(Long userId, String userSecret) {
        return JWT.create()
                .withIssuer(issuer)
                .withClaim("user_id", userId)
                .withClaim("user_secret", userSecret)
                .withSubject("Refresh Token")
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .sign(alg);
    }

    /**
     * Verify a provided JWT token and throw exception if is not valid.
     *
     * @param token JWT token from header
     */
    public static DecodedJWT verifyToken(String token) {
        try {
            JWTVerifier verify = JWT.require(alg).withIssuer(issuer).build();

            DecodedJWT decodedJWT = verify.verify(token);
            return decodedJWT;
        } catch (TokenExpiredException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new TokenIsInvalidException();
        }
    }

    /**
     * Verify and get user id from provided JWT authorization token
     *
     * @param token JWT decoded token.
     * @return ID of user from token
     */
    public static long getIdFromToken(DecodedJWT token) {
        return token.getClaim("user_id").asLong();
    }

    /**
     * Verify and get user secret from provided JWT authorization token
     *
     * @param token decoded JWT token.
     * @return User secret from token.
     */
    public static String getSecretFromToken(DecodedJWT token) {
        return token.getClaim("user_secret").asString();
    }

    /**
     * Generate for user a secret code
     *
     * @return Generated user secret.
     */
    public static String generateUserSecret() {
        Random random = new Random();

        String userSecret = random.ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(16)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return userSecret;
    }
}
