package com.hanghae.mungnayng.service;

import com.hanghae.mungnayng.domain.Notification.Dto.NotificationResponseDto;
import com.hanghae.mungnayng.domain.Notification.Notification;
import com.hanghae.mungnayng.domain.member.Member;
import com.hanghae.mungnayng.repository.MemberRepository;
import com.hanghae.mungnayng.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    public List<NotificationResponseDto> getNotification(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다")
        );
        List<Notification> notificationList = notificationRepository.findByMemberOrderByCreatedAtDesc(member);

        return notificationList.stream()
                .map(NotificationResponseDto::Alert)
                .collect(Collectors.toList());
    }

    @Transactional
    public String readOK(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );
        Notification n = Notification.builder()
                .readState(true)
                .build();
        notificationRepository.save(n);
        return "읽음 처리 완료";
    }

    @Transactional
    public String deleteNotification(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );
        notificationRepository.deleteByMember(member);

        return "알림 삭제";
    }
}
