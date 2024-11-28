package com.quartze.shortenerurl.filters;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.quartze.shortenerurl.exceptions.TokenIsInvalidException;
import com.quartze.shortenerurl.helpers.ApiError;
import com.quartze.shortenerurl.helpers.AuthUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTValidationTokenFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getHeader("Authorization") == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization").replace("Bearer ", "");

        try {
            DecodedJWT verify = AuthUtils.verifyToken(token);

            if (verify == null) throw new TokenIsInvalidException();

            Long userId = AuthUtils.getIdFromToken(verify);

            Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, AuthorityUtils.commaSeparatedStringToAuthorityList("USER"));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (TokenExpiredException ex) {
            SecurityContextHolder.clearContext();
            ApiError apiError = new ApiError(
                    HttpStatus.UNAUTHORIZED,
                    ex.getMessage()
            );

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(apiError.toString());
        }
    }
}
