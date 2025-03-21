package io.github.manoelpiovesan.utils;

import io.github.manoelpiovesan.entities.Paper;
import io.github.manoelpiovesan.entities.User;
import io.github.manoelpiovesan.repositories.PaperRepository;
import io.github.manoelpiovesan.repositories.TokenRepository;
import io.github.manoelpiovesan.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * @author Manoel Rodrigues
 */
@io.quarkus.runtime.Startup
public class Startup {

    @Inject
    UserRepository userRepository;

    @Inject
    PaperRepository paperRepository;

    @PostConstruct
    @Transactional
    public void init() {
        System.out.println("Application started v1");

        if (userRepository.count() == 0) {
            User user = new User();
            user.firstName = "Manoel";
            user.lastName = "Rodrigues";
            user.username = "manoel.rodrigues";
            user.email = "manoel@manoel.com";
            user.password = "1234";
            userRepository.create(user);
            System.out.println("User created");
        }
        if (paperRepository.count() == 0) {
            User user = userRepository.findById(1L);

            for (int i = 0; i < 63; i++) {
                Paper paper = new Paper();
                paper.title = "Paper " + i;
                paper.summary = "Summary " + i;
                paper.user = user;
                paperRepository.create(paper);
                System.out.println("Paper " + i + "created");
            }
        }
    }
}
