package com.hanghae.mungnayng.domain.Room;

import com.hanghae.mungnayng.domain.Timestamped;
import com.hanghae.mungnayng.domain.item.Item;
import com.hanghae.mungnayng.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomInfo extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "roomInfo", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<RoomDetail> roomDetail;


    @ManyToOne
    @JoinColumn(name = "itemId")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String recentChat;

    public void updateRecentChat(String recentChat) {
        this.recentChat = recentChat;
    }
}
