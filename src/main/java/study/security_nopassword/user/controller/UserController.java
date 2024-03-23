package study.security_nopassword.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.security_nopassword.user.service.UserService;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Boolean userJoin() {
        log.info("### UserController.userJoin start");
        userService.join();
        return Boolean.TRUE;
    }


}
