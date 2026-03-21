package com.sonesh.finance.repository;

import com.sonesh.finance.model.Notification;
import com.sonesh.finance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUserIdAndIsReadFalse(Long userId);

    // ✅ FIXED: moved inside interface
    boolean existsByUserAndTitleAndMessage(User user, String title, String message);
}