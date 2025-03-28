package ru.otus.java.professional.yampolskiy.ttuserservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findAllByIsActiveTrue();

    Optional<User> findByOidcSubject(String oidcSubject);

    boolean existsByOidcSubject(String oidcSubject);

    List<User> findByOidcProvider(String oidcProvider);

    Optional<User> findByOidcSubjectAndOidcProvider(String oidcSubject, String oidcProvider);
}