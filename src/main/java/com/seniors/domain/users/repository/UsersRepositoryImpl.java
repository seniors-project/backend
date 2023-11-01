package com.seniors.domain.users.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seniors.common.exception.type.NotFoundException;
import com.seniors.common.repository.BasicRepoSupport;
import com.seniors.domain.resume.entity.Resume;
import com.seniors.domain.users.dto.UsersDto;
import com.seniors.domain.users.dto.UsersDto.GetUserDetailRes;
import com.seniors.domain.users.entity.QUsers;
import com.seniors.domain.users.entity.Users;
import jakarta.persistence.EntityManager;

import static com.seniors.domain.resume.entity.QResume.resume;

public class UsersRepositoryImpl extends BasicRepoSupport implements UsersRepositoryCustom {

	protected UsersRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager em) {
		super(jpaQueryFactory, em);
	}

	private final static QUsers users = QUsers.users;

	@Override
	public Users getOneUsers(Long userId) {
		return jpaQueryFactory
				.selectFrom(QUsers.users)
				.where(QUsers.users.id.eq(userId))
				.fetchOne();
	}

	@Override
	public GetUserDetailRes getUserDetails(Long userId, String snsId, String nickname, String profileImageUrl, String email, String gender) {
		Resume detailResume = jpaQueryFactory
				.selectFrom(resume)
				.where(resume.users.id.eq(userId).and(resume.users.snsId.eq(snsId)))
				.fetchOne();

		if (detailResume == null) {
			throw new NotFoundException("Resume Not Found");
		}

		return new GetUserDetailRes(detailResume, userId, snsId, nickname, profileImageUrl, email, gender);
	}

	@Override
	public void modifyUser(Long userId, UsersDto.SetUserDto setUserDto, String profileImageUrl) {
		jpaQueryFactory.update(users)
				.set(users.nickname, setUserDto.getNickname())
				.set(users.profileImageUrl, profileImageUrl)
				.where(users.id.eq(userId))
				.execute();
	}

}
