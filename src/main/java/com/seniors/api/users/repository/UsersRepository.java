package com.seniors.api.users.repository;

import com.seniors.api.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long>, UsersRepositoryCustom{
	Optional<Users> findByEmail(String email);
}
