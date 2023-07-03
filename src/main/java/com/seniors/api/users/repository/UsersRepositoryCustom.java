package com.seniors.api.users.repository;

import com.seniors.api.users.domain.Users;

public interface UsersRepositoryCustom {

	Users getOneUsers(Long userId);
}
