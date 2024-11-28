package com.quartze.shortenerurl.repositories;

import com.quartze.shortenerurl.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);

    public Optional<User> findByIdAndUserSecret(Long id, String userSecret);
}
