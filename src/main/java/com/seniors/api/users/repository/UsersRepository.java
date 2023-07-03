package com.seniors.api.users.repository;

import com.seniors.api.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long>, UsersRepositoryCustom{
}
