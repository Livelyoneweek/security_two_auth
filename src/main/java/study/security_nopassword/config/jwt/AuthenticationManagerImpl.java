package study.security_nopassword.config.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import study.security_nopassword.config.principal.PrincipalDetails;
import study.security_nopassword.config.principal.PrincipalDetailsService;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationManagerImpl implements AuthenticationManager {

    private final PrincipalDetailsService principalDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // MobileAuthenticationToken 인증 처리
        if (authentication instanceof MobileAuthenticationToken) {
            MobileAuthenticationToken mobileAuthenticationToken = (MobileAuthenticationToken) authentication;
            String username = mobileAuthenticationToken.getPrincipal().toString();
            String mobileCheckNumber = mobileAuthenticationToken.getCredentials().toString();

            PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(username);
            if (!principalDetails.getMobileCheckNumber().equals(mobileCheckNumber)) {
                new BadCredentialsException("Mobile check number is not valid.");
            }
            log.info("mobileCheckNumber ={}", mobileCheckNumber);
            return new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        }

        // UsernamePasswordAuthenticationToken 인증 처리
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
            String username = usernamePasswordAuthenticationToken.getPrincipal().toString();
            String password = usernamePasswordAuthenticationToken.getCredentials().toString();

            PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(username);
            if (!bCryptPasswordEncoder.matches(password, principalDetails.getPassword())) {
                throw new BadCredentialsException("Password is not valid.");
            }

            return new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
        }
        // 모든 경우 커버 (유효성 검증 실패 시 예외 발생)
        throw new AuthenticationException("Invalid authentication token") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
    }
}
