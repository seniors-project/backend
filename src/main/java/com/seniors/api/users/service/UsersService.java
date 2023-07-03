package com.seniors.api.users.service;

import com.seniors.api.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {

	private final UsersRepository usersRepository;

}
