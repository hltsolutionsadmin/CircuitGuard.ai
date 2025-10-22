package com.circuitguard.auth;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.circuitguard.commonservice.dto.LoggedInUser;
import com.circuitguard.commonservice.enums.ERole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtils {

	private static final String ROLES = "roles";
	private static final String JWT_SECRET = "BXNh8+ay7pPm9IhFP1PdLle3VCQ5QzDXJ0bdzITCkp3U2aZKmldRbD8B4qfMLWvj";

	private final long jwtExpirationMs = 5L * 60 * 60 * 1000; // 5 hours
	private final long refreshTokenExpirationMs = 30L * 24 * 60 * 60 * 1000; // 30 days

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
	}

	public String generateJwtToken(LoggedInUser user) throws JsonProcessingException {
		String json = serializeUser(user);
		Set<String> roles = user.getRoles().stream().map(role -> role.toString()).collect(Collectors.toSet());

		Instant now = Instant.now();
		Instant expiry = now.plusMillis(jwtExpirationMs);

		return Jwts.builder().header().type("JWT") // instead of Header.JWT_TYPE
				.and().claim("sub", json).claim("version", user.getVersion()).claim(ROLES, roles)
				.notBefore(Date.from(now)).expiration(Date.from(expiry)).signWith(getSigningKey()).compact();

	}

	public String generateRefreshToken(LoggedInUser user) throws JsonProcessingException {
		String json = serializeUser(user);
		Set<String> roles = user.getRoles().stream().map(role -> role.toString()).collect(Collectors.toSet());
		Instant now = Instant.now();
		Instant expiry = now.plusMillis(refreshTokenExpirationMs);

		return Jwts.builder().header().type("JWT").and().subject(json).claim("version", user.getVersion())
				.claim(ROLES, roles).notBefore(Date.from(now)).expiration(Date.from(expiry)).signWith(getSigningKey())
				.compact();

	}

	public boolean validateJwtToken(String token) {
		try {
			Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			log.error("JWT validation failed: {}", e.getMessage());
		}
		return false;
	}

	public LoggedInUser getUserFromToken(String token) throws JsonProcessingException {
		Claims claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();

		List<String> rolesList = claims.get(ROLES, List.class);

		ObjectMapper mapper = new ObjectMapper();
		String subjectJson = claims.get("sub", String.class);
		LoggedInUser user = mapper.readValue(subjectJson, LoggedInUser.class);

		user.setRoles(new HashSet<>());
		if (rolesList != null) {
			for (String roleName : rolesList) {
				try {
					user.getRoles().add(ERole.valueOf(roleName).name());
				} catch (IllegalArgumentException ignored) {
				}
			}
		}
		return user;
	}

	public int getTokenVersion(String token) {
		Claims claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
		return claims.get("version", Integer.class);
	}

	private String serializeUser(LoggedInUser user) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(user);
	}

	public boolean isTokenVersionValid(String token, int currentUserVersion) {
		Claims claims = getClaims(token);
		Integer tokenVersion = claims.get("version", Integer.class);
		return tokenVersion != null && tokenVersion == currentUserVersion;
	}

	public Claims getClaims(String token) {
		return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
	}

}
