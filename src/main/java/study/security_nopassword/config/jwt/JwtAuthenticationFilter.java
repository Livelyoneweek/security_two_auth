package study.security_nopassword.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;
import study.security_nopassword.config.principal.PrincipalDetails;
import study.security_nopassword.config.secret.Secret;
import study.security_nopassword.user.dto.UserDto;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl("/v1/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        System.out.println("### JwtAuthenticationFilter.attemptAuthentication");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            UserDto.Req.Login user = objectMapper.readValue(request.getInputStream(), UserDto.Req.Login.class);

            if (user.getPassword() != null) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
                return authenticationManager.authenticate(authenticationToken);
            } else {
                MobileAuthenticationToken authenticationToken = new MobileAuthenticationToken(user.getUsername(), user.getMobileCheckNumber());
                return authenticationManager.authenticate(authenticationToken);
            }
        } catch (IOException e) {
            throw new RuntimeException("attemptAuthentication exception");
        }
    }

    @Override
    @Transactional
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        //Hash 암호 방식
        String username = String.valueOf(principalDetails.getUsername());
        String userRole = String.valueOf(principalDetails.getUserRole(principalDetails.getUser()));
        String accessToken = JWT.create()
                .withSubject("access_token")
                .withExpiresAt(new Date(System.currentTimeMillis() + Secret.AT_EXPRIRATION_TIME))
                .withClaim("username", username)
                .withClaim("userRole", userRole)
                .sign(Algorithm.HMAC512(Secret.AT_JWT_KEY));

        String refreshToken = JWT.create()
                .withSubject("refresh_token")
                .withExpiresAt(new Date(System.currentTimeMillis() + Secret.RT_EXPRIRATION_TIME))
                .withClaim("username", username)
                .sign(Algorithm.HMAC512(Secret.RT_JWT_KEY));

        response.addHeader(Secret.AT_HEADER_STRING, accessToken);
        response.addHeader(Secret.RT_HEADER_STRING, refreshToken);
        response.addHeader("Content-Type", "application/json;charset=UTF-8");

        PrintWriter writer = response.getWriter();

        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("username", username);
        jsonObj.addProperty("code", "200");
        jsonObj.addProperty(Secret.AT_HEADER_STRING, accessToken);
        jsonObj.addProperty(Secret.RT_HEADER_STRING, refreshToken);

        writer.println(jsonObj);
    }

    @Override
    protected AuthenticationFailureHandler getFailureHandler() {
        return (request, response, exception) -> {
            log.error(exception.getMessage());
            response.addHeader("Content-Type", "application/json;charset=UTF-8");
            new ObjectMapper().writeValue(response.getWriter(), "ttt");
        };
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        getFailureHandler().onAuthenticationFailure(request, response, failed);
    }

}
