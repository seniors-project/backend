package com.seniors.domain.users.repository;


import com.seniors.domain.users.dto.UsersDto.GetUserDetailRes;
import com.seniors.domain.users.dto.UsersDto.SetUserDto;
import com.seniors.domain.users.entity.Users;


public interface UsersRepositoryCustom {

	Users getOneUsers(Long userId);

	GetUserDetailRes getUserDetails(Long userId, String snsId, String nickname, String profileImageUrl, String email, String gender);

	void modifyUser(Long userId, SetUserDto setUserDto, String profileImageUrl);
}
