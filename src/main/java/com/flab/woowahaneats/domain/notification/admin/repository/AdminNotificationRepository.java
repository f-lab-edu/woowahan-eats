package com.flab.woowahaneats.domain.notification.admin.repository;

import com.flab.woowahaneats.domain.notification.admin.domain.AdminNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminNotificationRepository extends JpaRepository<AdminNotification, Long> {

    @Query("SELECT n FROM AdminNotification n " +
            "LEFT JOIN FETCH n.restaurant " +
            "LEFT JOIN FETCH n.owner " +
            "WHERE n.read = false ORDER BY n.createdAt DESC")
    List<AdminNotification> findByReadFalseOrderByCreatedAtDesc();
}
