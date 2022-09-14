package com.hanghae.mungnayng.domain.Room;

import com.hanghae.mungnayng.domain.item.Item;
import com.hanghae.mungnayng.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    public RoomDetail(RoomInfo roomInfo, Member member,  Item item) {
        this.roomInfo = roomInfo;
        this.member = member;
        this.item = item;
    }

    @Column
    private Long chatId;

    public void updateChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Column
    private Long chatId;

    public void updateChatId(Long chatId) {
        this.chatId = chatId;
    }
}
