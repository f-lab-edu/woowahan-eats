package com.flab.woowahaneats.domain.notification.owner.repository;

import com.flab.woowahaneats.domain.notification.owner.domain.OwnerNotification;
import com.flab.woowahaneats.domain.owner.domain.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OwnerNotificationRepository extends JpaRepository<OwnerNotification, Long> {

    List<OwnerNotification> findByOwnerAndReadFalseOrderByCreatedAtDesc(Owner owner);
}
