package com.seniors.domain.notification.repository;

import com.seniors.domain.notification.dto.NotificationDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface NotificationRepositoryCustom {
	Slice<NotificationDto> findNotificationList(Long userId, Pageable pageable, Long lastId);

}
