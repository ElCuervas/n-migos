package com.company.n_migos.repository;

import com.company.n_migos.entity.Juego;
import com.company.n_migos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);
    @Query("SELECT u.biblioteca FROM User u WHERE u.username = :username")
    List<Juego> findBibliotecaByUsername(@Param("username") String username);
}
