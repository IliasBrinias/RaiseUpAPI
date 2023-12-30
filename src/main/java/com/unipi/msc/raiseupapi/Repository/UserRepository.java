package com.unipi.msc.raiseupapi.Repository;

import com.unipi.msc.raiseupapi.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByEmail(String email);
    Optional<User> findAllByIdIs(Long id);
    Optional<List<User>> findUsersByIdIn(List<Long> ids);
    List<User> findUsersByUsernameContainingOrEmailContainingOrFirstNameContainingOrLastNameContaining(String username, String email, String firstName, String lastName);
    boolean existsUserByUsername(String Username);
    boolean existsUserByEmail(String Email);
}