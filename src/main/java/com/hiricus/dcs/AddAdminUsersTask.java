package com.hiricus.dcs;

import com.hiricus.dcs.model.repository.UserRepository;
import com.hiricus.dcs.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AddAdminUsersTask implements CommandLineRunner {
    private final UserRepository userRepository;
    private final AuthService authService;

    public AddAdminUsersTask(UserRepository userRepository,
                             AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.isUserExistsByLogin("Hiricus")) {
            log.info("Adding system users...");
            authService.addAdminUser("Hiricus", "2556145");
        }
    }
}
