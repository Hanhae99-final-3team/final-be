package com.hanghae.mungnayng.domain.Room;

import com.hanghae.mungnayng.domain.Timestamped;
import com.hanghae.mungnayng.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomInfo extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomDetailId")
    private RoomDetail roomDetail;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @Column(nullable = false)
    private String nickname;
}
