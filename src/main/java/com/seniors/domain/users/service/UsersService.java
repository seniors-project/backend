package com.seniors.domain.users.service;

import com.seniors.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {

	private final UsersRepository usersRepository;

}
