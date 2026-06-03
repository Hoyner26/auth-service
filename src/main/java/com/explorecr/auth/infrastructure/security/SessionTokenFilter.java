package com.explorecr.auth.infrastructure.security;

import com.explorecr.auth.domain.model.Session;
import com.explorecr.auth.domain.port.out.SessionRepositoryPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class SessionTokenFilter extends OncePerRequestFilter {
    private final SessionRepositoryPort sessionRepository;

    public SessionTokenFilter(SessionRepositoryPort sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring("Bearer ".length());
        sessionRepository.findByToken(token)
            .filter(session -> !session.isExpired(LocalDateTime.now()))
            .ifPresent(this::authenticate);

        filterChain.doFilter(request, response);
    }

    private void authenticate(Session session) {
        String authority = "ROLE_" + session.user().role().name();
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                session.user(),
                null,
                List.of(new SimpleGrantedAuthority(authority))
            );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
