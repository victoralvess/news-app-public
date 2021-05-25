package com.github.victoralvess.newz.domain.port;

import com.github.victoralvess.newz.domain.entities.User;

public interface UserRepository {
    User getUser();
    void saveSubject(String subject);
}
