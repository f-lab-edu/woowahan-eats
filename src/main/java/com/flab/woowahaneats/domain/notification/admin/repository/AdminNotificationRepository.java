package com.flab.woowahaneats.domain.notification.admin.repository;

import com.flab.woowahaneats.domain.notification.admin.domain.AdminNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminNotificationRepository extends JpaRepository<AdminNotification, Long> {

    List<AdminNotification> findByReadFalseOrderByCreatedAtDesc();
}
