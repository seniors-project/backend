package com.seniors.domain.notification.repository;

import com.seniors.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom{
	List<Notification> findAllByUsersId(Long userId);
}
