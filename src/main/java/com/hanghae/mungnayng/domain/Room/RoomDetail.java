package com.hanghae.mungnayng.domain.Room;

import com.hanghae.mungnayng.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_info_id")
    private RoomInfo roomInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public RoomDetail(RoomInfo roomInfo, Member member) {
        this.roomInfo = roomInfo;
        this.member = member;
    }

    @Column
    private Long chatId;

    public void updateChatId(Long chatId) {
        this.chatId = chatId;
    }
}
