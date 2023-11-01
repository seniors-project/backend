package com.seniors.domain.users.service;

import com.seniors.config.S3Uploader;
import com.seniors.domain.post.entity.PostMedia;
import com.seniors.domain.users.dto.UsersDto.GetUserDetailRes;
import com.seniors.domain.users.dto.UsersDto.SetUserDto;
import com.seniors.domain.users.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UsersService {

	private final UsersRepository usersRepository;
	private final S3Uploader s3Uploader;

	public GetUserDetailRes findOneUsers(Long userId, String snsId, String nickname, String profileImageUrl, String email, String gender) {

		return usersRepository.getUserDetails(userId,
				snsId,
				nickname,
				profileImageUrl,
				email,
				gender);
	}

	@Transactional
	public void modifyUsers(Long userId, SetUserDto setUserDto, MultipartFile profileImage) throws IOException {
		String dirName = "users/profileImage/" + userId.toString();
		// 기존 미디어 파일 삭제
		s3Uploader.deleteS3Object(dirName);

		String uploadImagePath = s3Uploader.upload(profileImage, dirName);
		usersRepository.modifyUser(userId, setUserDto, uploadImagePath);
	}
}
