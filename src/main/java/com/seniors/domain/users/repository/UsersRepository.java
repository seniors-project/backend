package com.seniors.domain.users.repository;

import com.seniors.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long>, UsersRepositoryCustom{
	Optional<Users> findByEmail(String email);
}
