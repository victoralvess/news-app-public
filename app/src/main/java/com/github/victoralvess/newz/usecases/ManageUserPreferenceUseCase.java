package com.github.victoralvess.newz.usecases;

import com.github.victoralvess.newz.domain.port.UserRepository;

public class ManageUserPreferenceUseCase {
    private final UserRepository userRepository;

    public ManageUserPreferenceUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setSubject(String subject) {
        userRepository.saveSubject(subject);
    }

    public String getSubject() {
        return userRepository.getUser().getSubject();
    }
}
