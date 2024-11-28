package com.github.felvesthe.movierental.repositories;

import com.github.felvesthe.movierental.models.User;
import com.github.felvesthe.movierental.repositories.interfaces.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserInMemoryRepository implements UserRepository {

    public static final List<User> users = new ArrayList<>();

    @Override
    public Optional<User> findByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    @Override
    public User save(User user) {
        users.add(user);

        return user;
    }
}
