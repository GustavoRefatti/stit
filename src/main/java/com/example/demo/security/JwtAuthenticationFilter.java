package com.example.demo.security;

import com.example.demo.domain.Credentials;
import com.example.demo.domain.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.security.SecurityProperties.AUTH_LOGIN_URL;
import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private static final Logger log = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl(AUTH_LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            ObjectMapper mapper = new ObjectMapper();

            Credentials credentials = mapper.readValue(requestBody, Credentials.class);

            String login = credentials.getEmail();
            String password = credentials.getPassword();
            AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login, password);

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new BadCredentialsException("Invalid arguments");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
        User user = ((User) authentication.getPrincipal());

        List<String> roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        byte[] signingKey = SecurityProperties.JWT_SECRET.getBytes();

        String token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), HS512)
                .setHeaderParam("typ", SecurityProperties.TOKEN_TYPE)
                .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                .claim("id", user.getUsername())
                .claim("roles", roles)
                .compact();

        try{
            ObjectMapper mapper = new ObjectMapper();
            Token jwtToken = new Token(token);
            response.setContentType(APPLICATION_JSON_VALUE);
            response.getWriter().write(mapper.writeValueAsString(jwtToken));
        } catch (IOException e) {
            throw new BadCredentialsException("errrroou");
        }
    }

}
