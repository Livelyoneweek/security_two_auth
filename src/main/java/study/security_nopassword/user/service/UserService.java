package study.security_nopassword.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.security_nopassword.user.entity.User;
import study.security_nopassword.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public void join() {
        log.info("### UserService.join");
        String encPassword = bCryptPasswordEncoder.encode("151315");
        User user = new User(encPassword);
        userRepository.save(user);
    }
}

