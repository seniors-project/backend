package com.seniors.domain.users.service;

import com.seniors.domain.users.dto.UsersDto.GetUserDetailRes;
import com.seniors.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {

	private final UsersRepository usersRepository;

	public GetUserDetailRes findOneUsers(Long userId, String snsId, String nickname, String profileImageUrl, String email, String gender) {

		return usersRepository.getUserDetails(userId,
				snsId,
				nickname,
				profileImageUrl,
				email,
				gender);
	}
}
