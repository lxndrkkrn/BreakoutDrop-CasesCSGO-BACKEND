package org.example.breakoutdrop.Repositories;

import org.example.breakoutdrop.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
