package ru.otus.java.professional.yampolskiy.ttuserservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.java.professional.yampolskiy.ttuserservice.entities.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(String name);
    boolean existsByName(String name);

    @Query("SELECT r FROM Role r " +
            "LEFT JOIN FETCH r.permissions")
    List<Role> findAll();
}

