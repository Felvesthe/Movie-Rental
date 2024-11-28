package com.github.felvesthe.movierental.repositories.interfaces;

import com.github.felvesthe.movierental.models.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmail(String email);
    User save(User user);
}
