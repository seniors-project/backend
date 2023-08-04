package com.seniors.domain.notification.repository;

import com.seniors.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom{
}
