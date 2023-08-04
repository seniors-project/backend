package com.seniors.domain.notification.repository;

import com.seniors.domain.notification.dto.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationRepositoryCustom {
	Page<NotificationDto> findAllByUsersId(Long userId, Pageable pageable);

}
