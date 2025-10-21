package com.skillrat.auth;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.skillrat.commonservice.dto.LoggedInUser;
import com.skillrat.commonservice.dto.UserDTO;
import com.skillrat.commonservice.enums.ERole;
import com.skillrat.commonservice.user.UserDetailsImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserServiceAdapter userServiceAdapter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                LoggedInUser loggedInUser = jwtUtils.getUserFromToken(jwt);
                UserDTO user = userServiceAdapter.getUserById(loggedInUser.getId());

                boolean isSuperAdmin = loggedInUser.getRoles() != null
                        && loggedInUser.getRoles().contains(ERole.ROLE_SUPER_ADMIN.name());

                boolean isVersionValid = user != null && jwtUtils.isTokenVersionValid(jwt, user.getVersion());

                if (isSuperAdmin || isVersionValid) {
                    List<GrantedAuthority> authorities = loggedInUser.getRoles().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UserDetails userDetails = new UserDetailsImpl(
                            loggedInUser.getId(),
                            loggedInUser.getPrimaryContact(),
                            loggedInUser.getEmail(),
                            loggedInUser.getPassword(),
                            authorities
                    );

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: ", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
