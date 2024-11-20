package com.quartze.shortenerurl.helpers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.quartze.shortenerurl.exceptions.TokenIsInvalidException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

public class AuthUtils {

    private static Algorithm alg = Algorithm.HMAC256("rU$2X0~<6&v`uOB^");
    private static String issuer = "shortener0";

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
                .withExpiresAt(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .sign(alg);
    }

    /**
     * Verify a provided JWT token and throw exception if is not valid.
     *
     * @param token JWT token from header
     */
    public static void verifyToken(String token) {
        try {
            JWTVerifier verify = JWT.require(alg).withIssuer(issuer).build();
            DecodedJWT decodedJWT = verify.verify(token);
        } catch (TokenExpiredException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new TokenIsInvalidException();
        }
    }

    /**
     * Verify and get user id from provided JWT authorization token
     *
     * @param token JWT token.
     * @return ID of user from token
     */
    public static long getIdFromToken(String token) {
       try {
           JWTVerifier verify = JWT.require(alg).withIssuer(issuer).build();
           DecodedJWT decodedJWT = verify.verify(token);

           return decodedJWT.getClaim("user_id").asLong();
       } catch (TokenExpiredException ex) {
           throw ex;
       } catch (Exception ex) {
           throw new TokenIsInvalidException();
       }
    }

    /**
     * Verify and get user secret from provided JWT authorization token
     *
     * @param token JWT token.
     * @return User secret from token.
     */
    public static String getSecretFromToken(String token) {
        try {
            JWTVerifier verify = JWT.require(alg).withIssuer(issuer).build();
            DecodedJWT decodedJWT = verify.verify(token);

            return decodedJWT.getClaim("user_secret").asString();
        } catch (TokenExpiredException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new TokenIsInvalidException();
        }
    }

    /**
     * Generate hash for user password to store it to database
     * Generating method is PBKDF2WithHmacSHA1
     *
     * @param password user password
     * @return Password hash with salt
     */
    public static String hashUserPassword(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            String encodedSalt = Base64.getEncoder().encodeToString(salt);
            String encodedHash = Base64.getEncoder().encodeToString(hash);

            return encodedSalt + ":" + encodedHash;

        } catch (Exception ex) {
            throw new RuntimeException("Error while hashing password: " + ex.getMessage(), ex);
        }
    }

    /**
     * Verify password provides by user with hashed one.
     *
     * @param password user password
     * @param hashPassword hashed password
     * @return boolean of verified passwords - true if verified, false if not.
     */
    public static boolean verifyPasswords(String password, String hashPassword) {
        String[] parts = hashPassword.split(":");

        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] hash = Base64.getDecoder().decode(parts[1]);

        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] testHash = factory.generateSecret(spec).getEncoded();

            return java.util.Arrays.equals(hash, testHash);
        } catch (Exception ex) {
            throw new RuntimeException("Error while verifying password: " + ex.getMessage(), ex);
        }
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
