package com.hanghae.mungnayng.repository;

import com.hanghae.mungnayng.domain.Notification.Notification;
import com.hanghae.mungnayng.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByMemberOrderByCreatedAtDesc(Member member);

    void deleteByMember(Member member);
}
