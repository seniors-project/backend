package com.seniors.domain.users.repository;


import com.seniors.domain.users.dto.UsersDto.GetChatUserRes;
import com.seniors.domain.users.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UsersRepositoryCustom {

	Users getOneUsers(Long userId);

	Page<GetChatUserRes> findAllChatRoom(Long userId, Pageable pageable);

}
