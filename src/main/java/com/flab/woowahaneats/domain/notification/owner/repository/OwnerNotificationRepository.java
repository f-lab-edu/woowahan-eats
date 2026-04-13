package com.flab.woowahaneats.domain.notification.owner.repository;

import com.flab.woowahaneats.domain.notification.owner.domain.OwnerNotification;
import com.flab.woowahaneats.domain.owner.domain.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OwnerNotificationRepository extends JpaRepository<OwnerNotification, Long> {

    @Query("SELECT n FROM OwnerNotification n " +
            "LEFT JOIN FETCH n.restaurant " +
            "LEFT JOIN FETCH n.owner " +
            "WHERE n.owner = :owner AND n.read = false ORDER BY n.createdAt DESC")
    List<OwnerNotification> findByOwnerAndReadFalseOrderByCreatedAtDesc(Owner owner);
}
